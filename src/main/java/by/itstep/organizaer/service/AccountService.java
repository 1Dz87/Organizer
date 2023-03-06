package by.itstep.organizaer.service;

import by.itstep.organizaer.exceptions.AccountAlreadyExistsException;
import by.itstep.organizaer.exceptions.AccountNotFoundException;
import by.itstep.organizaer.exceptions.UserNotFoundException;
import by.itstep.organizaer.model.dto.AccountDto;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.User;
import by.itstep.organizaer.model.mapping.AccountMapper;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@SecurityRequirement(name = "Organizer security")
public class AccountService {

    AccountRepository accountRepository;

    AccountMapper accountMapper;

    UserRepository userRepository;

    @Transactional
    public AccountDto createAccount(AccountDto accountDto) {
        Account accountToSave = accountMapper.toEntity(accountDto);
        accountToSave.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        try {
            accountRepository.save(accountToSave);
        } catch (Exception ex) {
            throw new AccountAlreadyExistsException(accountDto.getName());
        }
        return accountMapper.toDto(accountToSave);
    }

    public AccountDto getAccountById(Long id) {
        return accountMapper.toDto(accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id)));
    }

    @Transactional
    public AccountDto update(String name, Long id) {
        return accountRepository.findById(id).map(account -> {
                    account.setName(name);
                    return accountRepository.save(account);
                })
                .map(accountMapper::toDto)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional
    public void delete(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);
    }


}
