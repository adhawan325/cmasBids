package com.cmasbids.repository.search;

import com.cmasbids.domain.Candidate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Candidate entity.
 */
public interface CandidateSearchRepository extends ElasticsearchRepository<Candidate, Long> {
}
