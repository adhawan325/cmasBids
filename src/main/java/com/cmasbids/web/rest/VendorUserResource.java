package com.cmasbids.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cmasbids.domain.VendorUser;

import com.cmasbids.repository.VendorUserRepository;
import com.cmasbids.repository.search.VendorUserSearchRepository;
import com.cmasbids.web.rest.errors.BadRequestAlertException;
import com.cmasbids.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing VendorUser.
 */
@RestController
@RequestMapping("/api")
public class VendorUserResource {

    private final Logger log = LoggerFactory.getLogger(VendorUserResource.class);

    private static final String ENTITY_NAME = "vendorUser";

    private final VendorUserRepository vendorUserRepository;

    private final VendorUserSearchRepository vendorUserSearchRepository;

    public VendorUserResource(VendorUserRepository vendorUserRepository, VendorUserSearchRepository vendorUserSearchRepository) {
        this.vendorUserRepository = vendorUserRepository;
        this.vendorUserSearchRepository = vendorUserSearchRepository;
    }

    /**
     * POST  /vendor-users : Create a new vendorUser.
     *
     * @param vendorUser the vendorUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vendorUser, or with status 400 (Bad Request) if the vendorUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vendor-users")
    @Timed
    public ResponseEntity<VendorUser> createVendorUser(@RequestBody VendorUser vendorUser) throws URISyntaxException {
        log.debug("REST request to save VendorUser : {}", vendorUser);
        if (vendorUser.getId() != null) {
            throw new BadRequestAlertException("A new vendorUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VendorUser result = vendorUserRepository.save(vendorUser);
        vendorUserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vendor-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /vendor-users : Updates an existing vendorUser.
     *
     * @param vendorUser the vendorUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vendorUser,
     * or with status 400 (Bad Request) if the vendorUser is not valid,
     * or with status 500 (Internal Server Error) if the vendorUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vendor-users")
    @Timed
    public ResponseEntity<VendorUser> updateVendorUser(@RequestBody VendorUser vendorUser) throws URISyntaxException {
        log.debug("REST request to update VendorUser : {}", vendorUser);
        if (vendorUser.getId() == null) {
            return createVendorUser(vendorUser);
        }
        VendorUser result = vendorUserRepository.save(vendorUser);
        vendorUserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vendorUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vendor-users : get all the vendorUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of vendorUsers in body
     */
    @GetMapping("/vendor-users")
    @Timed
    public List<VendorUser> getAllVendorUsers() {
        log.debug("REST request to get all VendorUsers");
        return vendorUserRepository.findAll();
        }

    /**
     * GET  /vendor-users/:id : get the "id" vendorUser.
     *
     * @param id the id of the vendorUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vendorUser, or with status 404 (Not Found)
     */
    @GetMapping("/vendor-users/{id}")
    @Timed
    public ResponseEntity<VendorUser> getVendorUser(@PathVariable Long id) {
        log.debug("REST request to get VendorUser : {}", id);
        VendorUser vendorUser = vendorUserRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vendorUser));
    }

    /**
     * DELETE  /vendor-users/:id : delete the "id" vendorUser.
     *
     * @param id the id of the vendorUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vendor-users/{id}")
    @Timed
    public ResponseEntity<Void> deleteVendorUser(@PathVariable Long id) {
        log.debug("REST request to delete VendorUser : {}", id);
        vendorUserRepository.delete(id);
        vendorUserSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/vendor-users?query=:query : search for the vendorUser corresponding
     * to the query.
     *
     * @param query the query of the vendorUser search
     * @return the result of the search
     */
    @GetMapping("/_search/vendor-users")
    @Timed
    public List<VendorUser> searchVendorUsers(@RequestParam String query) {
        log.debug("REST request to search VendorUsers for query {}", query);
        return StreamSupport
            .stream(vendorUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
