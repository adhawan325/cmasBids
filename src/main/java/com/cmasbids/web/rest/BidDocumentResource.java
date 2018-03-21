package com.cmasbids.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cmasbids.domain.BidDocument;

import com.cmasbids.repository.BidDocumentRepository;
import com.cmasbids.repository.search.BidDocumentSearchRepository;
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
 * REST controller for managing BidDocument.
 */
@RestController
@RequestMapping("/api")
public class BidDocumentResource {

    private final Logger log = LoggerFactory.getLogger(BidDocumentResource.class);

    private static final String ENTITY_NAME = "bidDocument";

    private final BidDocumentRepository bidDocumentRepository;

    private final BidDocumentSearchRepository bidDocumentSearchRepository;

    public BidDocumentResource(BidDocumentRepository bidDocumentRepository, BidDocumentSearchRepository bidDocumentSearchRepository) {
        this.bidDocumentRepository = bidDocumentRepository;
        this.bidDocumentSearchRepository = bidDocumentSearchRepository;
    }

    /**
     * POST  /bid-documents : Create a new bidDocument.
     *
     * @param bidDocument the bidDocument to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bidDocument, or with status 400 (Bad Request) if the bidDocument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bid-documents")
    @Timed
    public ResponseEntity<BidDocument> createBidDocument(@Valid @RequestBody BidDocument bidDocument) throws URISyntaxException {
        log.debug("REST request to save BidDocument : {}", bidDocument);
        if (bidDocument.getId() != null) {
            throw new BadRequestAlertException("A new bidDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BidDocument result = bidDocumentRepository.save(bidDocument);
        bidDocumentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bid-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bid-documents : Updates an existing bidDocument.
     *
     * @param bidDocument the bidDocument to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bidDocument,
     * or with status 400 (Bad Request) if the bidDocument is not valid,
     * or with status 500 (Internal Server Error) if the bidDocument couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bid-documents")
    @Timed
    public ResponseEntity<BidDocument> updateBidDocument(@Valid @RequestBody BidDocument bidDocument) throws URISyntaxException {
        log.debug("REST request to update BidDocument : {}", bidDocument);
        if (bidDocument.getId() == null) {
            return createBidDocument(bidDocument);
        }
        BidDocument result = bidDocumentRepository.save(bidDocument);
        bidDocumentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bidDocument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bid-documents : get all the bidDocuments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of bidDocuments in body
     */
    @GetMapping("/bid-documents")
    @Timed
    public List<BidDocument> getAllBidDocuments() {
        log.debug("REST request to get all BidDocuments");
        return bidDocumentRepository.findAll();
        }

    /**
     * GET  /bid-documents/:id : get the "id" bidDocument.
     *
     * @param id the id of the bidDocument to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bidDocument, or with status 404 (Not Found)
     */
    @GetMapping("/bid-documents/{id}")
    @Timed
    public ResponseEntity<BidDocument> getBidDocument(@PathVariable Long id) {
        log.debug("REST request to get BidDocument : {}", id);
        BidDocument bidDocument = bidDocumentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bidDocument));
    }

    /**
     * DELETE  /bid-documents/:id : delete the "id" bidDocument.
     *
     * @param id the id of the bidDocument to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bid-documents/{id}")
    @Timed
    public ResponseEntity<Void> deleteBidDocument(@PathVariable Long id) {
        log.debug("REST request to delete BidDocument : {}", id);
        bidDocumentRepository.delete(id);
        bidDocumentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bid-documents?query=:query : search for the bidDocument corresponding
     * to the query.
     *
     * @param query the query of the bidDocument search
     * @return the result of the search
     */
    @GetMapping("/_search/bid-documents")
    @Timed
    public List<BidDocument> searchBidDocuments(@RequestParam String query) {
        log.debug("REST request to search BidDocuments for query {}", query);
        return StreamSupport
            .stream(bidDocumentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
