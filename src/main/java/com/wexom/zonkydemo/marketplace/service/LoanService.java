package com.wexom.zonkydemo.marketplace.service;

import com.wexom.zonkydemo.marketplace.api.ZonkyClient;
import com.wexom.zonkydemo.marketplace.api.dto.LoanDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;

/**
 * Service which is responsible for retrieving loans from Zonky API.
 */
@Service
public final class LoanService {

    private final ZonkyClient zonkyClient;

    /**
     * C-tor.
     *
     * @param zonkyClient client responsible for communication with Zonky API
     */
    @Autowired
    public LoanService(final ZonkyClient zonkyClient) {
        this.zonkyClient = zonkyClient;
    }

    /**
     * Method responsible for retrieving new loans, {@link LoanDto}.
     *
     * @param from parameter which is used to filter loans, only loans which are published after this time will be returned
     * @return loans which are filtered in case that from is present
     */
    public Flux<LoanDto> getLoans(final ZonedDateTime from) {
        return zonkyClient.getLoans(from);
    }
}
