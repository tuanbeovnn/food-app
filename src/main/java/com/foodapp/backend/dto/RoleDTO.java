package com.foodapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO extends AbstractDTO {
    private String name;
    private String code;
}
