package com.meet.undoschool.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseQueryRequest {
    private String query;
    private String category;
    private String type;
    private Integer minAge;
    private Integer maxAge;
    private Double minPrice;
    private Double maxPrice;
    private LocalDateTime nextSessionFrom;
    private String sort;
    private final int page = 0;
    private final int size = 10;
}
