package com.cmasbids.web.rest;

import com.cmasbids.CmasBidsApp;

import com.cmasbids.domain.BidDocument;
import com.cmasbids.domain.Bid;
import com.cmasbids.repository.BidDocumentRepository;
import com.cmasbids.repository.search.BidDocumentSearchRepository;
import com.cmasbids.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.cmasbids.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BidDocumentResource REST controller.
 *
 * @see BidDocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmasBidsApp.class)
public class BidDocumentResourceIntTest {

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private BidDocumentRepository bidDocumentRepository;

    @Autowired
    private BidDocumentSearchRepository bidDocumentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBidDocumentMockMvc;

    private BidDocument bidDocument;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BidDocumentResource bidDocumentResource = new BidDocumentResource(bidDocumentRepository, bidDocumentSearchRepository);
        this.restBidDocumentMockMvc = MockMvcBuilders.standaloneSetup(bidDocumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BidDocument createEntity(EntityManager em) {
        BidDocument bidDocument = new BidDocument()
            .documentName(DEFAULT_DOCUMENT_NAME)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE);
        // Add required entity
        Bid bid = BidResourceIntTest.createEntity(em);
        em.persist(bid);
        em.flush();
        bidDocument.setBid(bid);
        return bidDocument;
    }

    @Before
    public void initTest() {
        bidDocumentSearchRepository.deleteAll();
        bidDocument = createEntity(em);
    }

    @Test
    @Transactional
    public void createBidDocument() throws Exception {
        int databaseSizeBeforeCreate = bidDocumentRepository.findAll().size();

        // Create the BidDocument
        restBidDocumentMockMvc.perform(post("/api/bid-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bidDocument)))
            .andExpect(status().isCreated());

        // Validate the BidDocument in the database
        List<BidDocument> bidDocumentList = bidDocumentRepository.findAll();
        assertThat(bidDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        BidDocument testBidDocument = bidDocumentList.get(bidDocumentList.size() - 1);
        assertThat(testBidDocument.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testBidDocument.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testBidDocument.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);

        // Validate the BidDocument in Elasticsearch
        BidDocument bidDocumentEs = bidDocumentSearchRepository.findOne(testBidDocument.getId());
        assertThat(bidDocumentEs).isEqualToIgnoringGivenFields(testBidDocument);
    }

    @Test
    @Transactional
    public void createBidDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bidDocumentRepository.findAll().size();

        // Create the BidDocument with an existing ID
        bidDocument.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBidDocumentMockMvc.perform(post("/api/bid-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bidDocument)))
            .andExpect(status().isBadRequest());

        // Validate the BidDocument in the database
        List<BidDocument> bidDocumentList = bidDocumentRepository.findAll();
        assertThat(bidDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDocumentNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidDocumentRepository.findAll().size();
        // set the field null
        bidDocument.setDocumentName(null);

        // Create the BidDocument, which fails.

        restBidDocumentMockMvc.perform(post("/api/bid-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bidDocument)))
            .andExpect(status().isBadRequest());

        List<BidDocument> bidDocumentList = bidDocumentRepository.findAll();
        assertThat(bidDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidDocumentRepository.findAll().size();
        // set the field null
        bidDocument.setFile(null);

        // Create the BidDocument, which fails.

        restBidDocumentMockMvc.perform(post("/api/bid-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bidDocument)))
            .andExpect(status().isBadRequest());

        List<BidDocument> bidDocumentList = bidDocumentRepository.findAll();
        assertThat(bidDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBidDocuments() throws Exception {
        // Initialize the database
        bidDocumentRepository.saveAndFlush(bidDocument);

        // Get all the bidDocumentList
        restBidDocumentMockMvc.perform(get("/api/bid-documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bidDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }

    @Test
    @Transactional
    public void getBidDocument() throws Exception {
        // Initialize the database
        bidDocumentRepository.saveAndFlush(bidDocument);

        // Get the bidDocument
        restBidDocumentMockMvc.perform(get("/api/bid-documents/{id}", bidDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bidDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
    }

    @Test
    @Transactional
    public void getNonExistingBidDocument() throws Exception {
        // Get the bidDocument
        restBidDocumentMockMvc.perform(get("/api/bid-documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBidDocument() throws Exception {
        // Initialize the database
        bidDocumentRepository.saveAndFlush(bidDocument);
        bidDocumentSearchRepository.save(bidDocument);
        int databaseSizeBeforeUpdate = bidDocumentRepository.findAll().size();

        // Update the bidDocument
        BidDocument updatedBidDocument = bidDocumentRepository.findOne(bidDocument.getId());
        // Disconnect from session so that the updates on updatedBidDocument are not directly saved in db
        em.detach(updatedBidDocument);
        updatedBidDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restBidDocumentMockMvc.perform(put("/api/bid-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBidDocument)))
            .andExpect(status().isOk());

        // Validate the BidDocument in the database
        List<BidDocument> bidDocumentList = bidDocumentRepository.findAll();
        assertThat(bidDocumentList).hasSize(databaseSizeBeforeUpdate);
        BidDocument testBidDocument = bidDocumentList.get(bidDocumentList.size() - 1);
        assertThat(testBidDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testBidDocument.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testBidDocument.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);

        // Validate the BidDocument in Elasticsearch
        BidDocument bidDocumentEs = bidDocumentSearchRepository.findOne(testBidDocument.getId());
        assertThat(bidDocumentEs).isEqualToIgnoringGivenFields(testBidDocument);
    }

    @Test
    @Transactional
    public void updateNonExistingBidDocument() throws Exception {
        int databaseSizeBeforeUpdate = bidDocumentRepository.findAll().size();

        // Create the BidDocument

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBidDocumentMockMvc.perform(put("/api/bid-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bidDocument)))
            .andExpect(status().isCreated());

        // Validate the BidDocument in the database
        List<BidDocument> bidDocumentList = bidDocumentRepository.findAll();
        assertThat(bidDocumentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBidDocument() throws Exception {
        // Initialize the database
        bidDocumentRepository.saveAndFlush(bidDocument);
        bidDocumentSearchRepository.save(bidDocument);
        int databaseSizeBeforeDelete = bidDocumentRepository.findAll().size();

        // Get the bidDocument
        restBidDocumentMockMvc.perform(delete("/api/bid-documents/{id}", bidDocument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bidDocumentExistsInEs = bidDocumentSearchRepository.exists(bidDocument.getId());
        assertThat(bidDocumentExistsInEs).isFalse();

        // Validate the database is empty
        List<BidDocument> bidDocumentList = bidDocumentRepository.findAll();
        assertThat(bidDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBidDocument() throws Exception {
        // Initialize the database
        bidDocumentRepository.saveAndFlush(bidDocument);
        bidDocumentSearchRepository.save(bidDocument);

        // Search the bidDocument
        restBidDocumentMockMvc.perform(get("/api/_search/bid-documents?query=id:" + bidDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bidDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BidDocument.class);
        BidDocument bidDocument1 = new BidDocument();
        bidDocument1.setId(1L);
        BidDocument bidDocument2 = new BidDocument();
        bidDocument2.setId(bidDocument1.getId());
        assertThat(bidDocument1).isEqualTo(bidDocument2);
        bidDocument2.setId(2L);
        assertThat(bidDocument1).isNotEqualTo(bidDocument2);
        bidDocument1.setId(null);
        assertThat(bidDocument1).isNotEqualTo(bidDocument2);
    }
}
