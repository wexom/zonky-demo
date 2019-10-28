package com.wexom.zonkydemo.marketplace.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wexom.zonkydemo.marketplace.api.dto.LoanDto;
import com.xebialabs.restito.semantics.Action;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.contentType;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static org.junit.Assert.assertEquals;

/**
 * Integration test for Zonky Client.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ZonkyClientTest {

    private StubServer server = new StubServer(StubServer.DEFAULT_PORT);

    @Autowired
    private ZonkyClient zonkyClient;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public void setUp() {
        server.run();
    }

    @Test
    void givenFromNull_whenGetLoans_thenReturnLoanDto() throws JsonProcessingException {
        final LoanDto loanDto = LoanDto.builder()
                .id(ThreadLocalRandom.current().nextInt(100000, 1000000))
                .build();

        whenHttp(server)
                .match(Condition.get("/loans/marketplace"))
                .then(
                        ok(),
                        jsonContent(Collections.singletonList(loanDto)),
                        contentType("application/json")
                );

        Flux<LoanDto> loans = zonkyClient.getLoans(null);

        StepVerifier.create(loans).expectNext(loanDto).verifyComplete();
    }

    @Test
    void givenFromPrague_whenGetLoans_thenReturnLoanDto() throws JsonProcessingException {
        final LoanDto loanDto = LoanDto.builder()
                .id(ThreadLocalRandom.current().nextInt(100000, 1000000))
                .build();

        whenHttp(server)
                .match(Condition.get("/loans/marketplace"), Condition.parameter("datePublished__gt", "2019-01-01T00:00:00.000"))
                .then(
                        ok(),
                        jsonContent(Collections.singletonList(loanDto)),
                        contentType("application/json")
                );

        Flux<LoanDto> loans = zonkyClient.getLoans(ZonedDateTime.of(2019, 1, 1, 0, 0, 0, 0, ZoneId.of("Europe/Prague")));

        StepVerifier.create(loans).expectNext(loanDto).verifyComplete();
    }

    @Test
    void givenFromUTC_whenGetLoans_thenReturnLoanDto() throws JsonProcessingException {
        final LoanDto loanDto = LoanDto.builder()
                .id(ThreadLocalRandom.current().nextInt(100000, 1000000))
                .build();

        whenHttp(server)
                .match(Condition.get("/loans/marketplace"), Condition.parameter("datePublished__gt", "2019-01-01T01:00:00.000"))
                .then(
                        ok(),
                        jsonContent(Collections.singletonList(loanDto)),
                        contentType("application/json")
                );

        Flux<LoanDto> loans = zonkyClient.getLoans(ZonedDateTime.of(2019, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")));

        StepVerifier.create(loans).expectNext(loanDto).verifyComplete();
    }

    @Test
    void givenFromNull_whenGetLoans_thenReturnEmpty() throws JsonProcessingException {
        whenHttp(server)
                .match(Condition.get("/loans/marketplace"))
                .then(
                        ok(),
                        jsonContent(Collections.emptyList()),
                        contentType("application/json")
                );

        Flux<LoanDto> loans = zonkyClient.getLoans(null);

        StepVerifier.create(loans).verifyComplete();
    }

    @Test
    void givenFromNull_whenGetLoans_thenReturnNotFoundError() {
        whenHttp(server)
                .match(Condition.get("/loans/marketplace"))
                .then(
                        status(HttpStatus.NOT_FOUND_404)
                );

        Flux<LoanDto> loans = zonkyClient.getLoans(null);

        StepVerifier.create(loans)
                .consumeErrorWith(throwable -> assertEquals(404, ((WebClientResponseException) throwable.getCause()).getStatusCode().value()))
                .verify();
    }

    private Action jsonContent(Object object) throws JsonProcessingException {
        return stringContent(objectMapper.writeValueAsString(object));
    }
}