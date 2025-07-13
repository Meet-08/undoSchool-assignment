package com.meet.undoschool.controller;

import com.meet.undoschool.dto.CourseQueryRequest;
import com.meet.undoschool.dto.OutPutDto;
import com.meet.undoschool.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public OutPutDto search(
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
        request.setPage(page);
        request.setSize(size);
        request.setSort(sort);

        if (nextSessionFrom != null) {
            request.setNextSessionFrom(
                    ZonedDateTime.parse(nextSessionFrom)
            );
        }

        var hits = searchService.search(request);
        var courses = hits.stream()
                .map(SearchHit::getContent)
                .toList();


        OutPutDto outPutDto = new OutPutDto();

        outPutDto.setTotalHits(courses.size());
        outPutDto.setCourses(courses);

        return outPutDto;
    }

}
