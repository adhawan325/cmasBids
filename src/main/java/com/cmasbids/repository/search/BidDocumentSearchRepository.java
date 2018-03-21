package com.cmasbids.repository.search;

import com.cmasbids.domain.BidDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BidDocument entity.
 */
public interface BidDocumentSearchRepository extends ElasticsearchRepository<BidDocument, Long> {
}
