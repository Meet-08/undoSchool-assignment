package com.meet.undoschool.repository;

import com.meet.undoschool.model.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDocumentRepository extends ElasticsearchRepository<CourseDocument, Integer> {
}
