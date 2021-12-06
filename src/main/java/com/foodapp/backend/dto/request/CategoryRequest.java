package com.foodapp.backend.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;



@Getter
@Setter
public class CategoryRequest {
    private String name;
    private String code;
}
