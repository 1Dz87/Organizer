package by.itstep.organizaer.service.analitycs;

import by.itstep.organizaer.model.entity.Archive;
import by.itstep.organizaer.model.entity.Transaction;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.ArchiveRepository;
import by.itstep.organizaer.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AccountArchivationProcessor {

    TransactionRepository transactionRepository;

    AccountRepository accountRepository;

    ArchiveRepository archiveRepository;

    public List<Long> processAccountArchive(Long accId, LocalDate before, LocalDate dateFrom) {
        return accountRepository.findById(accId).map(account -> {
            List<Transaction> listTransaction = transactionRepository.findByAccount(account, before);
            Float spendAmount = listTransaction
                    .stream()
                    .filter(tx -> tx.getSourceAccount() != null)
                    .filter(tx -> tx.getSourceAccount().getId().equals(account.getId()))
                    .map(Transaction::getAmount)
                    .reduce(Float::sum)
                    .orElse(0F);
            Float incomeAmount = listTransaction
                    .stream()
                    .filter(tx -> tx.getTargetAccount().getId().equals(account.getId()))
                    .map(Transaction::getAmount)
                    .reduce(Float::sum)
                    .orElse(0F);
            archiveRepository.deleteByAccId(account.getId());
            archiveRepository.save(Archive.builder()
                    .account(account)
                    .spend(spendAmount)
                    .income(incomeAmount)
                    .til(before)
                    .dateFrom(dateFrom)
                    .build());
            return listTransaction.stream()
                    .map(transaction -> transaction.getId())
                    .collect(Collectors.toList());
        }).orElse(List.of());
    }

    public void delete(List<Long> listId) {
        transactionRepository.deleteAllById(listId);
    }
}
