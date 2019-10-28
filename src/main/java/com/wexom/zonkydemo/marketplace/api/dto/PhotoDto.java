package com.wexom.zonkydemo.marketplace.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "PhotoDtoBuilder")
@JsonDeserialize(builder = PhotoDto.PhotoDtoBuilder.class)
public final class PhotoDto {
    private final String name;
    private final String url;

    @JsonPOJOBuilder(withPrefix = "")
    public static class PhotoDtoBuilder {
    }
}
