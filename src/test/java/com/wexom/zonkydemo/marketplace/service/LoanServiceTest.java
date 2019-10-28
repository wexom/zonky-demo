package com.wexom.zonkydemo.marketplace.service;

import com.wexom.zonkydemo.marketplace.api.ZonkyClient;
import com.wexom.zonkydemo.marketplace.api.dto.LoanDto;
import com.wexom.zonkydemo.marketplace.api.exception.ZonkyClientException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * JUnit test for Loan Service.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoanServiceTest {

    private static final String ZONKY_CLIENT_ERROR_MESSAGE = "zonky client error message";

    private LoanService loanService;

    @Mock
    private ZonkyClient zonkyClient;

    @BeforeAll
    public void setUp() {
        loanService = new LoanService(zonkyClient);
    }

    @Test
    void givenFromNull_whenGetLoan_thenReturnLoanDto() {
        LoanDto loan = LoanDto.builder()
                .id(ThreadLocalRandom.current().nextInt(100000, 1000000))
                .build();

        when(zonkyClient.getLoans(ArgumentMatchers.isNull()))
                .thenReturn(
                        Flux.create(objectFluxSink -> {
                                    objectFluxSink.next(loan);
                                    objectFluxSink.complete();
                                }
                        )
                );

        Flux<LoanDto> loans = loanService.getLoans(null);

        StepVerifier.create(loans).expectNext(loan).verifyComplete();
    }

    @Test
    void givenFromPrague_whenGetLoan_thenReturnLoanDto() {
        LoanDto loan = LoanDto.builder()
                .id(ThreadLocalRandom.current().nextInt(100000, 1000000))
                .build();

        ZonedDateTime from = ZonedDateTime.of(2019, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Prague"));

        when(zonkyClient.getLoans(ArgumentMatchers.eq(from)))
                .thenReturn(
                        Flux.create(objectFluxSink -> {
                                    objectFluxSink.next(loan);
                                    objectFluxSink.complete();
                                }
                        )
                );

        Flux<LoanDto> loans = loanService.getLoans(from);

        StepVerifier.create(loans).expectNext(loan).verifyComplete();
    }

    @Test
    void givenFromNull_whenGetLoan_thenReturnError() {
        when(zonkyClient.getLoans(ArgumentMatchers.isNull()))
                .thenReturn(
                        Flux.create(objectFluxSink -> {
                                    objectFluxSink.error(new ZonkyClientException(ZONKY_CLIENT_ERROR_MESSAGE, null));
                                }
                        )
                );

        Flux<LoanDto> loans = loanService.getLoans(null);

        StepVerifier.create(loans).consumeErrorWith(throwable -> assertEquals(ZONKY_CLIENT_ERROR_MESSAGE, throwable.getMessage())).verify();
    }
}