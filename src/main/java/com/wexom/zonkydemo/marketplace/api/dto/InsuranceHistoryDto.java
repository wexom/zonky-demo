package com.wexom.zonkydemo.marketplace.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(builderClassName = "InsuranceHistoryDtoBuilder")
@JsonDeserialize(builder = InsuranceHistoryDto.InsuranceHistoryDtoBuilder.class)
public class InsuranceHistoryDto {
    private final LocalDate policyPeriodFrom;
    private final LocalDate policyPeriodTo;

    @JsonPOJOBuilder(withPrefix = "")
    public static class InsuranceHistoryDtoBuilder {
    }
}
