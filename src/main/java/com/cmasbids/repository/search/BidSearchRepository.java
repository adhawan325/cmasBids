package com.cmasbids.repository.search;

import com.cmasbids.domain.Bid;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bid entity.
 */
public interface BidSearchRepository extends ElasticsearchRepository<Bid, Long> {
}
