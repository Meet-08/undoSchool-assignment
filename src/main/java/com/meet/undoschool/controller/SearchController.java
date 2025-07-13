package com.meet.undoschool.controller;

import com.meet.undoschool.dto.CourseQueryRequest;
import com.meet.undoschool.model.CourseDocument;
import com.meet.undoschool.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    public SearchHits<CourseDocument> search(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String nextSessionFrom,
            @RequestParam(defaultValue = "default") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        CourseQueryRequest request = new CourseQueryRequest();
        request.setQuery(query);
        request.setCategory(category);
        request.setType(type);
        request.setMinAge(minAge);
        request.setMaxAge(maxAge);
        request.setMinPrice(minPrice);
        request.setMaxPrice(maxPrice);
        request.setSort(sort);

        if (nextSessionFrom != null) {
            request.setNextSessionFrom(
                    LocalDateTime.parse(nextSessionFrom)
            );
        }

        return searchService.search(request);
    }

}
