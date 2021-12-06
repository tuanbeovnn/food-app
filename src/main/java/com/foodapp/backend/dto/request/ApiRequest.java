package com.foodapp.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ApiRequest {

    private String name;
    private String httpMethod;
    private String pattern;
    private Boolean isRequiredAccessToken;
    private Boolean shouldCheckPermission;
    private List<Long> roleIds;
}
