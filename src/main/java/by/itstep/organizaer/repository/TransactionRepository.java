package by.itstep.organizaer.repository;

import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select tx from Transaction tx where (tx.sourceAccount = :account or tx.targetAccount = :account) and tx.dateTime < :before")
    List<Transaction> findByAccount(final Account account, final LocalDate before);



}
