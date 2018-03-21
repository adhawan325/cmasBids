package com.cmasbids.repository;

import com.cmasbids.domain.Candidate;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Candidate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    @Query("select distinct candidate from Candidate candidate left join fetch candidate.bids")
    List<Candidate> findAllWithEagerRelationships();

    @Query("select candidate from Candidate candidate left join fetch candidate.bids where candidate.id =:id")
    Candidate findOneWithEagerRelationships(@Param("id") Long id);

}
