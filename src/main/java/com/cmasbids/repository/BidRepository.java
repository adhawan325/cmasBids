package com.cmasbids.repository;

import com.cmasbids.domain.Bid;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Bid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("select distinct bid from Bid bid left join fetch bid.vendors")
    List<Bid> findAllWithEagerRelationships();

    @Query("select bid from Bid bid left join fetch bid.vendors where bid.id =:id")
    Bid findOneWithEagerRelationships(@Param("id") Long id);

}
