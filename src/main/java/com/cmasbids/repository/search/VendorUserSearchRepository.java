package com.cmasbids.repository.search;

import com.cmasbids.domain.VendorUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the VendorUser entity.
 */
public interface VendorUserSearchRepository extends ElasticsearchRepository<VendorUser, Long> {
}
