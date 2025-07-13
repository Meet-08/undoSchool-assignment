package com.meet.undoschool;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meet.undoschool.model.CourseDocument;
import com.meet.undoschool.repository.CourseDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final CourseDocumentRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Loading courses from sample-courses.json...");

        try (InputStream inputStream = new ClassPathResource("sample-course.json").getInputStream()) {
            List<CourseDocument> courses = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<CourseDocument>>() {
                    }
            );

            courseRepository.saveAll(courses);

            log.info("Successfully indexed {} courses into Elasticsearch.", courses.size());
        } catch (Exception e) {
            log.error("Failed to load or index course data", e);
        }
    }
}
