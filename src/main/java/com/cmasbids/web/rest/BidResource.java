package com.cmasbids.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cmasbids.domain.Bid;

import com.cmasbids.repository.BidRepository;
import com.cmasbids.repository.search.BidSearchRepository;
import com.cmasbids.web.rest.errors.BadRequestAlertException;
import com.cmasbids.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Bid.
 */
@RestController
@RequestMapping("/api")
public class BidResource {

    private final Logger log = LoggerFactory.getLogger(BidResource.class);

    private static final String ENTITY_NAME = "bid";

    private final BidRepository bidRepository;

    private final BidSearchRepository bidSearchRepository;

    public BidResource(BidRepository bidRepository, BidSearchRepository bidSearchRepository) {
        this.bidRepository = bidRepository;
        this.bidSearchRepository = bidSearchRepository;
    }

    /**
     * POST  /bids : Create a new bid.
     *
     * @param bid the bid to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bid, or with status 400 (Bad Request) if the bid has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bids")
    @Timed
    public ResponseEntity<Bid> createBid(@Valid @RequestBody Bid bid) throws URISyntaxException {
        log.debug("REST request to save Bid : {}", bid);
        if (bid.getId() != null) {
            throw new BadRequestAlertException("A new bid cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bid result = bidRepository.save(bid);
        bidSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bids/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bids : Updates an existing bid.
     *
     * @param bid the bid to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bid,
     * or with status 400 (Bad Request) if the bid is not valid,
     * or with status 500 (Internal Server Error) if the bid couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bids")
    @Timed
    public ResponseEntity<Bid> updateBid(@Valid @RequestBody Bid bid) throws URISyntaxException {
        log.debug("REST request to update Bid : {}", bid);
        if (bid.getId() == null) {
            return createBid(bid);
        }
        Bid result = bidRepository.save(bid);
        bidSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bid.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bids : get all the bids.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bids in body
     */
    @GetMapping("/bids")
    @Timed
    public List<Bid> getAllBids() {
        log.debug("REST request to get all Bids");
        return bidRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /bids/:id : get the "id" bid.
     *
     * @param id the id of the bid to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bid, or with status 404 (Not Found)
     */
    @GetMapping("/bids/{id}")
    @Timed
    public ResponseEntity<Bid> getBid(@PathVariable Long id) {
        log.debug("REST request to get Bid : {}", id);
        Bid bid = bidRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bid));
    }

    /**
     * DELETE  /bids/:id : delete the "id" bid.
     *
     * @param id the id of the bid to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bids/{id}")
    @Timed
    public ResponseEntity<Void> deleteBid(@PathVariable Long id) {
        log.debug("REST request to delete Bid : {}", id);
        bidRepository.delete(id);
        bidSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bids?query=:query : search for the bid corresponding
     * to the query.
     *
     * @param query the query of the bid search
     * @return the result of the search
     */
    @GetMapping("/_search/bids")
    @Timed
    public List<Bid> searchBids(@RequestParam String query) {
        log.debug("REST request to search Bids for query {}", query);
        return StreamSupport
            .stream(bidSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
