package com.meet.undoschool.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meet.undoschool.enums.Type;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "courses")
@Data
@Builder
public class CourseDocument {

    @Id
    private int id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private Type type;

    @Field(type = FieldType.Text)
    private String gradeRange;

    @Field(type = FieldType.Integer)
    private int minAge;

    @Field(type = FieldType.Integer)
    private int maxAge;

    @Field(type = FieldType.Double)
    private double price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime nextSession;

}
