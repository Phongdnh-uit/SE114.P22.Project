package com.se114p12.backend.vo;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageVO<T> {
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private Integer numberOfElements;
    private List<T> content;
}
