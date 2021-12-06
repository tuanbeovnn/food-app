package com.foodapp.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Setter
@Getter
public class UserRequest {

    @NotEmpty(message = "User is required")
    private String userName;
    @NotEmpty(message = "Password is required")
    private String password;
    @NotEmpty(message = "Email is required")
    private String email;
    private List<Long> roleIds;
}
