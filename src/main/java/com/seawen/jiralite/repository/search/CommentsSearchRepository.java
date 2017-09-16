package com.seawen.jiralite.repository.search;

import com.seawen.jiralite.domain.Comments;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Comments entity.
 */
public interface CommentsSearchRepository extends ElasticsearchRepository<Comments, Long> {
}
