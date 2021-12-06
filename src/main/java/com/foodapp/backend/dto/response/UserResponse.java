package com.foodapp.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String email;
    private String userName;
    private String password;
    private String status;
    private List<Long> roleIds;
}
