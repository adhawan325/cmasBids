package com.cmasbids.repository;

import com.cmasbids.domain.BidDocument;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BidDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BidDocumentRepository extends JpaRepository<BidDocument, Long> {

}
