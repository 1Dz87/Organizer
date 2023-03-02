package by.itstep.organizaer.repository;

import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    Optional<Archive> findByAccount(Account account);

    @Query(value = "select * from archive order by id desc limit 1", nativeQuery = true)
    Optional<Archive> findLast();

    @Query(value = "delete from archive where account_id = :account", nativeQuery = true)
    void deleteByAccId(Long account);
}
