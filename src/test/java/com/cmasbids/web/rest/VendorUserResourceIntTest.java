package com.cmasbids.web.rest;

import com.cmasbids.CmasBidsApp;

import com.cmasbids.domain.VendorUser;
import com.cmasbids.repository.VendorUserRepository;
import com.cmasbids.repository.search.VendorUserSearchRepository;
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

import javax.persistence.EntityManager;
import java.util.List;

import static com.cmasbids.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VendorUserResource REST controller.
 *
 * @see VendorUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CmasBidsApp.class)
public class VendorUserResourceIntTest {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    @Autowired
    private VendorUserRepository vendorUserRepository;

    @Autowired
    private VendorUserSearchRepository vendorUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVendorUserMockMvc;

    private VendorUser vendorUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VendorUserResource vendorUserResource = new VendorUserResource(vendorUserRepository, vendorUserSearchRepository);
        this.restVendorUserMockMvc = MockMvcBuilders.standaloneSetup(vendorUserResource)
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
    public static VendorUser createEntity(EntityManager em) {
        VendorUser vendorUser = new VendorUser()
            .userName(DEFAULT_USER_NAME);
        return vendorUser;
    }

    @Before
    public void initTest() {
        vendorUserSearchRepository.deleteAll();
        vendorUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createVendorUser() throws Exception {
        int databaseSizeBeforeCreate = vendorUserRepository.findAll().size();

        // Create the VendorUser
        restVendorUserMockMvc.perform(post("/api/vendor-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vendorUser)))
            .andExpect(status().isCreated());

        // Validate the VendorUser in the database
        List<VendorUser> vendorUserList = vendorUserRepository.findAll();
        assertThat(vendorUserList).hasSize(databaseSizeBeforeCreate + 1);
        VendorUser testVendorUser = vendorUserList.get(vendorUserList.size() - 1);
        assertThat(testVendorUser.getUserName()).isEqualTo(DEFAULT_USER_NAME);

        // Validate the VendorUser in Elasticsearch
        VendorUser vendorUserEs = vendorUserSearchRepository.findOne(testVendorUser.getId());
        assertThat(vendorUserEs).isEqualToIgnoringGivenFields(testVendorUser);
    }

    @Test
    @Transactional
    public void createVendorUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vendorUserRepository.findAll().size();

        // Create the VendorUser with an existing ID
        vendorUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendorUserMockMvc.perform(post("/api/vendor-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vendorUser)))
            .andExpect(status().isBadRequest());

        // Validate the VendorUser in the database
        List<VendorUser> vendorUserList = vendorUserRepository.findAll();
        assertThat(vendorUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVendorUsers() throws Exception {
        // Initialize the database
        vendorUserRepository.saveAndFlush(vendorUser);

        // Get all the vendorUserList
        restVendorUserMockMvc.perform(get("/api/vendor-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendorUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())));
    }

    @Test
    @Transactional
    public void getVendorUser() throws Exception {
        // Initialize the database
        vendorUserRepository.saveAndFlush(vendorUser);

        // Get the vendorUser
        restVendorUserMockMvc.perform(get("/api/vendor-users/{id}", vendorUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vendorUser.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVendorUser() throws Exception {
        // Get the vendorUser
        restVendorUserMockMvc.perform(get("/api/vendor-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVendorUser() throws Exception {
        // Initialize the database
        vendorUserRepository.saveAndFlush(vendorUser);
        vendorUserSearchRepository.save(vendorUser);
        int databaseSizeBeforeUpdate = vendorUserRepository.findAll().size();

        // Update the vendorUser
        VendorUser updatedVendorUser = vendorUserRepository.findOne(vendorUser.getId());
        // Disconnect from session so that the updates on updatedVendorUser are not directly saved in db
        em.detach(updatedVendorUser);
        updatedVendorUser
            .userName(UPDATED_USER_NAME);

        restVendorUserMockMvc.perform(put("/api/vendor-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVendorUser)))
            .andExpect(status().isOk());

        // Validate the VendorUser in the database
        List<VendorUser> vendorUserList = vendorUserRepository.findAll();
        assertThat(vendorUserList).hasSize(databaseSizeBeforeUpdate);
        VendorUser testVendorUser = vendorUserList.get(vendorUserList.size() - 1);
        assertThat(testVendorUser.getUserName()).isEqualTo(UPDATED_USER_NAME);

        // Validate the VendorUser in Elasticsearch
        VendorUser vendorUserEs = vendorUserSearchRepository.findOne(testVendorUser.getId());
        assertThat(vendorUserEs).isEqualToIgnoringGivenFields(testVendorUser);
    }

    @Test
    @Transactional
    public void updateNonExistingVendorUser() throws Exception {
        int databaseSizeBeforeUpdate = vendorUserRepository.findAll().size();

        // Create the VendorUser

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVendorUserMockMvc.perform(put("/api/vendor-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vendorUser)))
            .andExpect(status().isCreated());

        // Validate the VendorUser in the database
        List<VendorUser> vendorUserList = vendorUserRepository.findAll();
        assertThat(vendorUserList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVendorUser() throws Exception {
        // Initialize the database
        vendorUserRepository.saveAndFlush(vendorUser);
        vendorUserSearchRepository.save(vendorUser);
        int databaseSizeBeforeDelete = vendorUserRepository.findAll().size();

        // Get the vendorUser
        restVendorUserMockMvc.perform(delete("/api/vendor-users/{id}", vendorUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean vendorUserExistsInEs = vendorUserSearchRepository.exists(vendorUser.getId());
        assertThat(vendorUserExistsInEs).isFalse();

        // Validate the database is empty
        List<VendorUser> vendorUserList = vendorUserRepository.findAll();
        assertThat(vendorUserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVendorUser() throws Exception {
        // Initialize the database
        vendorUserRepository.saveAndFlush(vendorUser);
        vendorUserSearchRepository.save(vendorUser);

        // Search the vendorUser
        restVendorUserMockMvc.perform(get("/api/_search/vendor-users?query=id:" + vendorUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendorUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VendorUser.class);
        VendorUser vendorUser1 = new VendorUser();
        vendorUser1.setId(1L);
        VendorUser vendorUser2 = new VendorUser();
        vendorUser2.setId(vendorUser1.getId());
        assertThat(vendorUser1).isEqualTo(vendorUser2);
        vendorUser2.setId(2L);
        assertThat(vendorUser1).isNotEqualTo(vendorUser2);
        vendorUser1.setId(null);
        assertThat(vendorUser1).isNotEqualTo(vendorUser2);
    }
}
