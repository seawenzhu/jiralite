package com.seawen.jiralite.repository.search;

import com.seawen.jiralite.domain.ProjectMember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProjectMember entity.
 */
public interface ProjectMemberSearchRepository extends ElasticsearchRepository<ProjectMember, Long> {
}
