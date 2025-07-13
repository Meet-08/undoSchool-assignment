package com.meet.undoschool.dto;

import com.meet.undoschool.model.CourseDocument;
import lombok.Data;

import java.util.List;

@Data
public class OutPutDto {

    private int totalHits;

    private List<CourseDocument> courses;
}
