package com.wexom.zonkydemo.marketplace.api;

import com.wexom.zonkydemo.marketplace.api.dto.LoanDto;
import com.wexom.zonkydemo.marketplace.api.exception.ZonkyClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Client with interface for communication with Zonky API.
 */
@Component
public class ZonkyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZonkyClient.class);

    private final WebClient webClient;

    /**
     * C-tor.
     *
     * @param webClient web client for https communication.
     */
    @Autowired
    public ZonkyClient(final WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Method which gets all loans or loans which occured after specified datetime from Zonky API.
     *
     * @param from zoned date time which limits retrieved loans from API. (retrieved loans date time published > from)
     * @return stream of loans
     */
    public Flux<LoanDto> getLoans(final ZonedDateTime from) {
        LOGGER.debug("Executing loan retrieval. From limit: '{}'", from);
        return webClient.get()
                .uri(uriBuilder -> createUriForGetLoans(uriBuilder, from))
                .retrieve().bodyToFlux(LoanDto.class)
                .onErrorResume(throwable -> Flux.error(
                        new ZonkyClientException("Unable to retrieve data from response.", throwable)
                ));
    }

    /**
     * Method responsible for correct uri creation for get loans method.
     *
     * @param uriBuilder uri builder
     * @param from       zoned date time which limits retrieved loans from API. (retrieved loans date time published > from)
     * @return final uri which is used to retrieve loans
     */
    private URI createUriForGetLoans(final UriBuilder uriBuilder, final ZonedDateTime from) {
        LOGGER.debug("Creating uri for loan retrieval. From limit: '{}'", from);
        UriBuilder path = uriBuilder.path("/loans/marketplace");

        Optional.ofNullable(from)
                .ifPresent(
                        zonedDateTime -> path.queryParam(
                                "datePublished__gt",
                                zonedDateTime.withZoneSameInstant(ZoneId.of("Europe/Prague"))
                                        .toLocalDateTime()
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))
                        )
                );

        final URI finalPath = path.build();
        LOGGER.debug("Created URI for loan retrieval is: '{}'", finalPath);

        return finalPath;
    }
}
