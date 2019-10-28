package com.wexom.zonkydemo.marketplace.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.wexom.zonkydemo.marketplace.api.dto.domain.Purpose;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder(builderClassName = "LoanDtoBuilder")
@JsonDeserialize(builder = LoanDto.LoanDtoBuilder.class)
public final class LoanDto {
    private final Number id;
    private final String url;
    private final String name;
    private final String story;
    private final Purpose purpose;
    private final List<PhotoDto> photos;
    private final Number userId;
    private final String nickName;
    private final Number termInMonths;
    private final Number interestRate;
    private final Number revenueRate;
    private final Number annuity;
    private final Number premium;
    private final String rating;
    private final Boolean topped;
    private final Number amount;
    private final Number remainingInvestment;
    private final Number investmentRate;
    private final Boolean covered;
    private final Number reservedAmount;
    private final Number zonkyPlusAmount;
    private final ZonedDateTime datePublished;
    private final Boolean published;
    private final ZonedDateTime deadline;
    //Unable to find in documentation what type of object this is.
    private final String myOtherInvestments;
    //Unable to find in documentation what type of object this is.
    private final String borrowerRelatedInvestmentInfo;
    private final Number investmentsCount;
    private final Number questionsCount;
    private final String region;
    private final String mainIncomeType;
    private final Number activeLoansCount;
    private final Boolean insuranceActive;
    private final List<InsuranceHistoryDto> insuranceHistory;
    private final String countryOfOrigin;
    private final String currency;
    private final Boolean additionallyInsured;
    private final Number annuityWithInsurance;

    @JsonPOJOBuilder(withPrefix = "")
    public static class LoanDtoBuilder {
    }
}
