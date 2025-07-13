package com.meet.undoschool.service;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import com.meet.undoschool.dto.CourseQueryRequest;
import com.meet.undoschool.model.CourseDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchHits<CourseDocument> search(CourseQueryRequest req) {
        Query query = buildQuery(req);
        List<SortOptions> sorts = buildSort(req.getSort());
        PageRequest page = PageRequest.of(req.getPage(), req.getSize());

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withSort(sorts)
                .withPageable(page)
                .withMaxResults(req.getSize())
                .build();

        return elasticsearchOperations.search(nativeQuery, CourseDocument.class);
    }

    private Query buildQuery(CourseQueryRequest req) {
        var bool = QueryBuilders.bool();

        if (req.getQuery() != null && !req.getQuery().isBlank()) {
            bool.must(QueryBuilders.multiMatch(m -> m
                            .fields("title", "description")
                            .query(req.getQuery())
                    )
            );
        } else {
            bool.must(QueryBuilders.matchAll().build());
        }

        if (req.getCategory() != null) {
            bool.filter(
                    QueryBuilders.
                            term(t -> t
                                    .field("category")
                                    .value(req.getCategory())
                            )
            );
        }
        if (req.getType() != null) {
            bool.filter(QueryBuilders
                    .term(t -> t
                            .field("type")
                            .value(req.getType())
                    )
            );
        }

        addNumericRange(bool, "minAge", req.getMinAge(), req.getMaxAge());
        addNumericRange(bool, "price", req.getMinPrice(), req.getMaxPrice());

        if (req.getNextSessionFrom() != null) {
            bool.filter(RangeQuery.of(r -> r
                            .date(d -> d.field("nextSession")
                                    .gte(req.getNextSessionFrom().toString())
                            )
                    )
            );
        }

        return bool.build()._toQuery();
    }

    private void addNumericRange(
            co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder bool,
            String field,
            Number min,
            Number max
    ) {
        if (min != null || max != null) {
            bool.filter(RangeQuery.of(r -> r.number(
                                    nrq -> nrq
                                            .field(field)
                                            .gte(min != null ? min.doubleValue() : null)
                                            .lte(max != null ? max.doubleValue() : null)
                            )
                    )
            );
        }
    }

    private List<SortOptions> buildSort(String sort) {
        var list = new ArrayList<SortOptions>();
        if ("priceAsc".equalsIgnoreCase(sort)) {
            list.add(orderBy("price", SortOrder.Asc));
        } else if ("priceDesc".equalsIgnoreCase(sort)) {
            list.add(orderBy("price", SortOrder.Desc));
        } else {
            list.add(orderBy("nextSession", SortOrder.Asc)); // default
        }
        return list;
    }

    private SortOptions orderBy(String field, SortOrder order) {
        return SortOptions.of(s -> s.field(f -> f.field(field).order(order)));
    }
}
