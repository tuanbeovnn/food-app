package com.foodapp.backend.pageable;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageList<T> {
    private int currentPage;
    private int pageSize;
    private long total;
    private boolean success;
    private int totalPage;
    private List<T> list;
}
