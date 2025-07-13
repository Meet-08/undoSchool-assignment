package com.meet.undoschool.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class CourseQueryRequest {
    private String query;
    private String category;
    private String type;
    private Integer minAge;
    private Integer maxAge;
    private Double minPrice;
    private Double maxPrice;
    private ZonedDateTime nextSessionFrom;
    private String sort;
    private int size = 10;
    private int page = 0;
}
