package com.wexom.zonkydemo.marketplace.task;

import com.wexom.zonkydemo.marketplace.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Task which is responsible to get newest loans from Zonky API in specified time interval.
 */
@Component
@ConditionalOnProperty(
        value="loan.scheduled.task.enabled",
        havingValue = "true",
        matchIfMissing = true)
public final class LoanScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanScheduledTask.class);
    private static final String SEPARATOR = "================================================================================================";

    private final LoanService marketPlaceService;

    @Value("${loan.scheduled.task.fixed-rate-delay}")
    private int fixedRate;

    private boolean firstTime = true;
    private ZonedDateTime from = null;

    /**
     * C-tor.
     *
     * @param loanService service responsible for loan retrieval.
     */
    @Autowired
    public LoanScheduledTask(
            final LoanService loanService
    ) {
        this.marketPlaceService = loanService;
    }

    /**
     * Scheduled method responsible for retrieving new loans in specified intervals and logging retrieved results.
     */
    @Scheduled(fixedRateString = "${loan.scheduled.task.fixed-rate-delay}", initialDelay = 5000)
    public void scanMarketPlace() {
        LOGGER.info(SEPARATOR);
        if (!firstTime) {
            from = ZonedDateTime.now().minusNanos(fixedRate);
            LOGGER.info("Looking for new loans published after: {} :: Execution Time - {}", from, LocalDateTime.now());
        } else {
            LOGGER.info("Looking for new loans :: Execution Time - {}", LocalDateTime.now());
            firstTime = false;
        }
        LOGGER.info(SEPARATOR);

        marketPlaceService.getLoans(from)
                .collectList()
                .subscribe(
                        loans -> {
                            if (loans.isEmpty()) {
                                LOGGER.info("No new loans found.");
                            } else {
                                LOGGER.info("{} new loans found.", loans.size());
                                LOGGER.info(SEPARATOR);
                                loans.forEach(loanDto -> LOGGER.info("'{}'", loanDto));
                            }
                        });
    }
}
