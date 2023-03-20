package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.*;
import by.itstep.organizaer.model.dto.BillDto;
import by.itstep.organizaer.model.dto.CreateTxRequestDto;
import by.itstep.organizaer.model.dto.TxDto;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Friend;
import by.itstep.organizaer.model.entity.Transaction;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.mapping.TransactionMapper;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.FriendRepository;
import by.itstep.organizaer.repository.TransactionRepository;
import by.itstep.organizaer.utils.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Validated
@Qualifier("pureTxService")
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionService {

    TransactionRepository repository;

    TransactionMapper mapper;

    FriendRepository friendRepository;

    AccountRepository accountRepository;

    CurrencyExchengeService exchengeService;

    public TxDto getTx(final Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional
    public TxDto doTransact(@Valid CreateTxRequestDto request) {
        return doTransferTx(request);
    }

    private TxDto doTransferTx(CreateTxRequestDto request) {
        User currentUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Account sourceAccount = accountRepository.findByIdAndUser(request.getSourceAccountId(), currentUser)
                .orElseThrow(() -> new AccountNotFoundException(request.getSourceAccountId()));
        Account targetAccount = accountRepository.findById(request.getTargetAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getTargetAccountId()));
        if (sourceAccount.getCurrency() == targetAccount.getCurrency()) {
            return transactAndSave(sourceAccount, targetAccount, request);
        } else if (request.getIsAutoConverted()) {
            request.setAmount(exchengeService.exchange(request.getAmount(), sourceAccount.getCurrency(), targetAccount.getCurrency()));
            return transactAndSave(sourceAccount, targetAccount, request);
        } else throw new TransactionException("Валюты счетов не совпадают");
    }

    private TxDto transactAndSave(Account sourceAccount, Account targetAccount, CreateTxRequestDto request) {
        return Optional.of(sourceAccount)
                .filter(account -> account.getAmmount() >= request.getAmount())
                .map(account -> {
                    account.setAmmount(account.getAmmount() - request.getAmount());
                    targetAccount.setAmmount(targetAccount.getAmmount() + request.getAmount());
                    accountRepository.save(account);
                    accountRepository.save(targetAccount);

                    return mapper.toDto(createTransaction(request, getFriend(targetAccount), account, targetAccount));

                })
                .orElseThrow(() -> new NotEnoughFoundsException(sourceAccount.getName()));
    }

    private Transaction createTransaction(CreateTxRequestDto request, Friend friend, Account sourceAccount, Account targetAccount) {
        return repository.save(Transaction
                .builder()
                .amount(request.getAmount())
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .dateTime(LocalDateTime.now())
                .friend(friend)
                .build());
    }

    private Friend getFriend(Account targetAccount) {
        return Optional.ofNullable(targetAccount.getUser())
                .map(user -> {
                    if (SecurityUtil.getCurrentUser()
                            .map(User::getId)
                            .stream()
                            .anyMatch(id -> !id.equals(user.getId()))) {
                        return friendRepository.findByUuidAndUser(user.getUuid(), SecurityUtil.getCurrentUser().get())
                                .orElseGet(() -> SecurityUtil.getCurrentUser()
                                        .map(self -> friendRepository.save(Friend.builder()
                                                .user(self)
                                                .contacts(user.getContacts())
                                                .birthday(user.getBirthDay())
                                                .uuid(user.getUuid())
                                                .name(user.getName())
                                                .build()))
                                        .orElse(null));
                    }
                    return null;
                })
                .orElse(null);

    }

    public BillDto fillAccount(BillDto billDto) {
        return accountRepository.findById(billDto.getId())
                .map(account -> {
                    account.setAmmount(account.getAmmount() + exchengeService.exchange(billDto.getAmount(), billDto.getCurrency(), account.getCurrency()));
                    accountRepository.save(account);
                    Transaction tx = createTransaction(CreateTxRequestDto
                            .builder()
                            .amount(billDto.getAmount())
                            .build(), null, null, account);
                    return BillDto.builder()
                            .accountName(account.getName())
                            .currency(account.getCurrency())
                            .transactionDate(tx.getDateTime())
                            .id(tx.getId())
                            .build();
                })
                .orElseThrow(() -> new AccountNotFoundException(billDto.getId()));
    }

}
