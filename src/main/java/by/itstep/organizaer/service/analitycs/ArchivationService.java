package by.itstep.organizaer.service.analitycs;

import by.itstep.organizaer.config.ProjectConfiguration;
import by.itstep.organizaer.exceptions.AccountNotFoundException;
import by.itstep.organizaer.exceptions.BadRequestException;
import by.itstep.organizaer.model.dto.AllArchiveStatsDto;
import by.itstep.organizaer.model.dto.ArchiveStatsDto;
import by.itstep.organizaer.model.dto.SingleArchiveStatsDto;
import by.itstep.organizaer.model.dto.enums.ArchiveStatsType;
import by.itstep.organizaer.model.entity.Account;
import by.itstep.organizaer.model.entity.Archive;
import by.itstep.organizaer.repository.AccountRepository;
import by.itstep.organizaer.repository.ArchiveRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArchivationService {

    AccountRepository accountRepository;

    ArchiveRepository archiveRepository;

    ProjectConfiguration projectConfiguration;

    AccountArchivationProcessor accountArchivationProcessor;


    @Async
    @Transactional
    @Scheduled(cron = "${project.business.sheduling.evening-cron}")
    public void archiveEvening() {
        doArchive();
    }

    @Scheduled(cron = "${project.business.sheduling.morning-cron}")
    @Async
    public void doArchive() {
        LocalDate before = LocalDate.now().minusDays(projectConfiguration.getBusiness().getArchivationPeriodDays());
        LocalDate dateFrom = archiveRepository
                .findLast()
                .map(Archive::getTil)
                .orElse(null);
        Long lastAccId = accountRepository.findCurrentSeq();
        List<Long> listId = new ArrayList<>();
        for (long i = 1; i <= lastAccId; i++) {
            listId.addAll(accountArchivationProcessor.processAccountArchive(i, before, dateFrom));
        }
        accountArchivationProcessor.delete(listId);
    }


    public ArchiveStatsDto getStats(Long id, ArchiveStatsType type) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return archiveRepository.findByAccount(account).map(archive -> {
            switch (type) {
                case ALL:
                    return AllArchiveStatsDto.builder()
                            .income(archive.getIncome())
                            .spend(archive.getSpend())
                            .dateTo(archive.getTil())
                            .dateFrom(archive.getDateFrom())
                            .accountName(account.getName())
                            .build();
                case INCOME:
                    return SingleArchiveStatsDto.builder()
                            .ammount(archive.getIncome())
                            .dateTo(archive.getTil())
                            .dateFrom(archive.getDateFrom())
                            .accountName(account.getName())
                            .build();
                case SPEND:
                    return SingleArchiveStatsDto.builder()
                            .ammount(archive.getSpend())
                            .dateTo(archive.getTil())
                            .dateFrom(archive.getDateFrom())
                            .accountName(account.getName())
                            .build();
                default:
                    throw new BadRequestException("Ошибка сериализации");
            }
        }).orElseGet(() -> ArchiveStatsDto.builder()
                .accountName(account.getName())
                .build());
    }
}
