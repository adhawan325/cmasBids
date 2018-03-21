package com.cmasbids.web.rest;

import com.cmasbids.CmasBidsApp;

import com.cmasbids.domain.Bid;
import com.cmasbids.domain.Department;
import com.cmasbids.repository.BidRepository;
import com.cmasbids.repository.search.BidSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.cmasbids.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BidResource REST controller.
 *
 * @see BidResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmasBidsApp.class)
public class BidResourceIntTest {

    private static final String DEFAULT_BID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BID_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BID_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BID_SOW = "AAAAAAAAAA";
    private static final String UPDATED_BID_SOW = "BBBBBBBBBB";

    private static final String DEFAULT_BID_M_QS = "AAAAAAAAAA";
    private static final String UPDATED_BID_M_QS = "BBBBBBBBBB";

    private static final String DEFAULT_BID_D_QS = "AAAAAAAAAA";
    private static final String UPDATED_BID_D_QS = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private BidSearchRepository bidSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBidMockMvc;

    private Bid bid;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BidResource bidResource = new BidResource(bidRepository, bidSearchRepository);
        this.restBidMockMvc = MockMvcBuilders.standaloneSetup(bidResource)
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
    public static Bid createEntity(EntityManager em) {
        Bid bid = new Bid()
            .bidNumber(DEFAULT_BID_NUMBER)
            .bidName(DEFAULT_BID_NAME)
            .endDate(DEFAULT_END_DATE)
            .bidSOW(DEFAULT_BID_SOW)
            .bidMQs(DEFAULT_BID_M_QS)
            .bidDQs(DEFAULT_BID_D_QS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .modifiedOn(DEFAULT_MODIFIED_ON);
        // Add required entity
        Department department = DepartmentResourceIntTest.createEntity(em);
        em.persist(department);
        em.flush();
        bid.setDepartment(department);
        return bid;
    }

    @Before
    public void initTest() {
        bidSearchRepository.deleteAll();
        bid = createEntity(em);
    }

    @Test
    @Transactional
    public void createBid() throws Exception {
        int databaseSizeBeforeCreate = bidRepository.findAll().size();

        // Create the Bid
        restBidMockMvc.perform(post("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isCreated());

        // Validate the Bid in the database
        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeCreate + 1);
        Bid testBid = bidList.get(bidList.size() - 1);
        assertThat(testBid.getBidNumber()).isEqualTo(DEFAULT_BID_NUMBER);
        assertThat(testBid.getBidName()).isEqualTo(DEFAULT_BID_NAME);
        assertThat(testBid.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testBid.getBidSOW()).isEqualTo(DEFAULT_BID_SOW);
        assertThat(testBid.getBidMQs()).isEqualTo(DEFAULT_BID_M_QS);
        assertThat(testBid.getBidDQs()).isEqualTo(DEFAULT_BID_D_QS);
        assertThat(testBid.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBid.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testBid.getModifiedOn()).isEqualTo(DEFAULT_MODIFIED_ON);

        // Validate the Bid in Elasticsearch
        Bid bidEs = bidSearchRepository.findOne(testBid.getId());
        assertThat(bidEs).isEqualToIgnoringGivenFields(testBid);
    }

    @Test
    @Transactional
    public void createBidWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bidRepository.findAll().size();

        // Create the Bid with an existing ID
        bid.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBidMockMvc.perform(post("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isBadRequest());

        // Validate the Bid in the database
        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkBidNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidRepository.findAll().size();
        // set the field null
        bid.setBidNumber(null);

        // Create the Bid, which fails.

        restBidMockMvc.perform(post("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isBadRequest());

        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBidNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidRepository.findAll().size();
        // set the field null
        bid.setBidName(null);

        // Create the Bid, which fails.

        restBidMockMvc.perform(post("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isBadRequest());

        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidRepository.findAll().size();
        // set the field null
        bid.setEndDate(null);

        // Create the Bid, which fails.

        restBidMockMvc.perform(post("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isBadRequest());

        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidRepository.findAll().size();
        // set the field null
        bid.setCreatedBy(null);

        // Create the Bid, which fails.

        restBidMockMvc.perform(post("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isBadRequest());

        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidRepository.findAll().size();
        // set the field null
        bid.setCreatedOn(null);

        // Create the Bid, which fails.

        restBidMockMvc.perform(post("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isBadRequest());

        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifiedOnIsRequired() throws Exception {
        int databaseSizeBeforeTest = bidRepository.findAll().size();
        // set the field null
        bid.setModifiedOn(null);

        // Create the Bid, which fails.

        restBidMockMvc.perform(post("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isBadRequest());

        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBids() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);

        // Get all the bidList
        restBidMockMvc.perform(get("/api/bids?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bid.getId().intValue())))
            .andExpect(jsonPath("$.[*].bidNumber").value(hasItem(DEFAULT_BID_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].bidName").value(hasItem(DEFAULT_BID_NAME.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].bidSOW").value(hasItem(DEFAULT_BID_SOW.toString())))
            .andExpect(jsonPath("$.[*].bidMQs").value(hasItem(DEFAULT_BID_M_QS.toString())))
            .andExpect(jsonPath("$.[*].bidDQs").value(hasItem(DEFAULT_BID_D_QS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }

    @Test
    @Transactional
    public void getBid() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);

        // Get the bid
        restBidMockMvc.perform(get("/api/bids/{id}", bid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bid.getId().intValue()))
            .andExpect(jsonPath("$.bidNumber").value(DEFAULT_BID_NUMBER.toString()))
            .andExpect(jsonPath("$.bidName").value(DEFAULT_BID_NAME.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.bidSOW").value(DEFAULT_BID_SOW.toString()))
            .andExpect(jsonPath("$.bidMQs").value(DEFAULT_BID_M_QS.toString()))
            .andExpect(jsonPath("$.bidDQs").value(DEFAULT_BID_D_QS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBid() throws Exception {
        // Get the bid
        restBidMockMvc.perform(get("/api/bids/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBid() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);
        bidSearchRepository.save(bid);
        int databaseSizeBeforeUpdate = bidRepository.findAll().size();

        // Update the bid
        Bid updatedBid = bidRepository.findOne(bid.getId());
        // Disconnect from session so that the updates on updatedBid are not directly saved in db
        em.detach(updatedBid);
        updatedBid
            .bidNumber(UPDATED_BID_NUMBER)
            .bidName(UPDATED_BID_NAME)
            .endDate(UPDATED_END_DATE)
            .bidSOW(UPDATED_BID_SOW)
            .bidMQs(UPDATED_BID_M_QS)
            .bidDQs(UPDATED_BID_D_QS)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .modifiedOn(UPDATED_MODIFIED_ON);

        restBidMockMvc.perform(put("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBid)))
            .andExpect(status().isOk());

        // Validate the Bid in the database
        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeUpdate);
        Bid testBid = bidList.get(bidList.size() - 1);
        assertThat(testBid.getBidNumber()).isEqualTo(UPDATED_BID_NUMBER);
        assertThat(testBid.getBidName()).isEqualTo(UPDATED_BID_NAME);
        assertThat(testBid.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testBid.getBidSOW()).isEqualTo(UPDATED_BID_SOW);
        assertThat(testBid.getBidMQs()).isEqualTo(UPDATED_BID_M_QS);
        assertThat(testBid.getBidDQs()).isEqualTo(UPDATED_BID_D_QS);
        assertThat(testBid.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBid.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testBid.getModifiedOn()).isEqualTo(UPDATED_MODIFIED_ON);

        // Validate the Bid in Elasticsearch
        Bid bidEs = bidSearchRepository.findOne(testBid.getId());
        assertThat(bidEs).isEqualToIgnoringGivenFields(testBid);
    }

    @Test
    @Transactional
    public void updateNonExistingBid() throws Exception {
        int databaseSizeBeforeUpdate = bidRepository.findAll().size();

        // Create the Bid

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBidMockMvc.perform(put("/api/bids")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bid)))
            .andExpect(status().isCreated());

        // Validate the Bid in the database
        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBid() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);
        bidSearchRepository.save(bid);
        int databaseSizeBeforeDelete = bidRepository.findAll().size();

        // Get the bid
        restBidMockMvc.perform(delete("/api/bids/{id}", bid.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bidExistsInEs = bidSearchRepository.exists(bid.getId());
        assertThat(bidExistsInEs).isFalse();

        // Validate the database is empty
        List<Bid> bidList = bidRepository.findAll();
        assertThat(bidList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBid() throws Exception {
        // Initialize the database
        bidRepository.saveAndFlush(bid);
        bidSearchRepository.save(bid);

        // Search the bid
        restBidMockMvc.perform(get("/api/_search/bids?query=id:" + bid.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bid.getId().intValue())))
            .andExpect(jsonPath("$.[*].bidNumber").value(hasItem(DEFAULT_BID_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].bidName").value(hasItem(DEFAULT_BID_NAME.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].bidSOW").value(hasItem(DEFAULT_BID_SOW.toString())))
            .andExpect(jsonPath("$.[*].bidMQs").value(hasItem(DEFAULT_BID_M_QS.toString())))
            .andExpect(jsonPath("$.[*].bidDQs").value(hasItem(DEFAULT_BID_D_QS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bid.class);
        Bid bid1 = new Bid();
        bid1.setId(1L);
        Bid bid2 = new Bid();
        bid2.setId(bid1.getId());
        assertThat(bid1).isEqualTo(bid2);
        bid2.setId(2L);
        assertThat(bid1).isNotEqualTo(bid2);
        bid1.setId(null);
        assertThat(bid1).isNotEqualTo(bid2);
    }
}
