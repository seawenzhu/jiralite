package com.seawen.jiralite.repository.search;

import com.seawen.jiralite.domain.CodeType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CodeType entity.
 */
public interface CodeTypeSearchRepository extends ElasticsearchRepository<CodeType, Long> {
}
