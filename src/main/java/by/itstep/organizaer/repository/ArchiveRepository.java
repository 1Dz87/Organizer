package by.itstep.organizaer.repository;

import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    Optional<Archive> findByAccount(Account account);
}
