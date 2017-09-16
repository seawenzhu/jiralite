package com.seawen.jiralite.repository.search;

import com.seawen.jiralite.domain.Code;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Code entity.
 */
public interface CodeSearchRepository extends ElasticsearchRepository<Code, Long> {
}
