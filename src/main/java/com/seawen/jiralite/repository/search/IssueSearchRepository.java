package com.seawen.jiralite.repository.search;

import com.seawen.jiralite.domain.Issue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Issue entity.
 */
public interface IssueSearchRepository extends ElasticsearchRepository<Issue, Long> {
}
