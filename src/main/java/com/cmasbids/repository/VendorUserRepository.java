package com.cmasbids.repository;

import com.cmasbids.domain.VendorUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VendorUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendorUserRepository extends JpaRepository<VendorUser, Long> {

}
