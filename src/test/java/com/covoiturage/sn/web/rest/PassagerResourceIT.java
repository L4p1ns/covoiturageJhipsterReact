package com.covoiturage.sn.web.rest;

import com.covoiturage.sn.CovoijhipsterApp;
import com.covoiturage.sn.domain.Passager;
import com.covoiturage.sn.repository.PassagerRepository;
import com.covoiturage.sn.repository.search.PassagerSearchRepository;
import com.covoiturage.sn.service.PassagerService;
import com.covoiturage.sn.service.dto.PassagerDTO;
import com.covoiturage.sn.service.mapper.PassagerMapper;
import com.covoiturage.sn.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.covoiturage.sn.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PassagerResource} REST controller.
 */
@SpringBootTest(classes = CovoijhipsterApp.class)
public class PassagerResourceIT {

    private static final Boolean DEFAULT_FUMEUR = false;
    private static final Boolean UPDATED_FUMEUR = true;

    private static final Boolean DEFAULT_ACCEPT_MUSIC = false;
    private static final Boolean UPDATED_ACCEPT_MUSIC = true;

    private static final Boolean DEFAULT_ACCEPT_RADIO = false;
    private static final Boolean UPDATED_ACCEPT_RADIO = true;

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    @Autowired
    private PassagerRepository passagerRepository;

    @Autowired
    private PassagerMapper passagerMapper;

    @Autowired
    private PassagerService passagerService;

    /**
     * This repository is mocked in the com.covoiturage.sn.repository.search test package.
     *
     * @see com.covoiturage.sn.repository.search.PassagerSearchRepositoryMockConfiguration
     */
    @Autowired
    private PassagerSearchRepository mockPassagerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPassagerMockMvc;

    private Passager passager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PassagerResource passagerResource = new PassagerResource(passagerService);
        this.restPassagerMockMvc = MockMvcBuilders.standaloneSetup(passagerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passager createEntity(EntityManager em) {
        Passager passager = new Passager()
            .fumeur(DEFAULT_FUMEUR)
            .acceptMusic(DEFAULT_ACCEPT_MUSIC)
            .acceptRadio(DEFAULT_ACCEPT_RADIO)
            .telephone(DEFAULT_TELEPHONE);
        return passager;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passager createUpdatedEntity(EntityManager em) {
        Passager passager = new Passager()
            .fumeur(UPDATED_FUMEUR)
            .acceptMusic(UPDATED_ACCEPT_MUSIC)
            .acceptRadio(UPDATED_ACCEPT_RADIO)
            .telephone(UPDATED_TELEPHONE);
        return passager;
    }

    @BeforeEach
    public void initTest() {
        passager = createEntity(em);
    }

    @Test
    @Transactional
    public void createPassager() throws Exception {
        int databaseSizeBeforeCreate = passagerRepository.findAll().size();

        // Create the Passager
        PassagerDTO passagerDTO = passagerMapper.toDto(passager);
        restPassagerMockMvc.perform(post("/api/passagers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passagerDTO)))
            .andExpect(status().isCreated());

        // Validate the Passager in the database
        List<Passager> passagerList = passagerRepository.findAll();
        assertThat(passagerList).hasSize(databaseSizeBeforeCreate + 1);
        Passager testPassager = passagerList.get(passagerList.size() - 1);
        assertThat(testPassager.isFumeur()).isEqualTo(DEFAULT_FUMEUR);
        assertThat(testPassager.isAcceptMusic()).isEqualTo(DEFAULT_ACCEPT_MUSIC);
        assertThat(testPassager.isAcceptRadio()).isEqualTo(DEFAULT_ACCEPT_RADIO);
        assertThat(testPassager.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Validate the Passager in Elasticsearch
        verify(mockPassagerSearchRepository, times(1)).save(testPassager);
    }

    @Test
    @Transactional
    public void createPassagerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passagerRepository.findAll().size();

        // Create the Passager with an existing ID
        passager.setId(1L);
        PassagerDTO passagerDTO = passagerMapper.toDto(passager);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassagerMockMvc.perform(post("/api/passagers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passagerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Passager in the database
        List<Passager> passagerList = passagerRepository.findAll();
        assertThat(passagerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Passager in Elasticsearch
        verify(mockPassagerSearchRepository, times(0)).save(passager);
    }


    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = passagerRepository.findAll().size();
        // set the field null
        passager.setTelephone(null);

        // Create the Passager, which fails.
        PassagerDTO passagerDTO = passagerMapper.toDto(passager);

        restPassagerMockMvc.perform(post("/api/passagers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passagerDTO)))
            .andExpect(status().isBadRequest());

        List<Passager> passagerList = passagerRepository.findAll();
        assertThat(passagerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPassagers() throws Exception {
        // Initialize the database
        passagerRepository.saveAndFlush(passager);

        // Get all the passagerList
        restPassagerMockMvc.perform(get("/api/passagers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passager.getId().intValue())))
            .andExpect(jsonPath("$.[*].fumeur").value(hasItem(DEFAULT_FUMEUR.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptMusic").value(hasItem(DEFAULT_ACCEPT_MUSIC.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptRadio").value(hasItem(DEFAULT_ACCEPT_RADIO.booleanValue())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getPassager() throws Exception {
        // Initialize the database
        passagerRepository.saveAndFlush(passager);

        // Get the passager
        restPassagerMockMvc.perform(get("/api/passagers/{id}", passager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(passager.getId().intValue()))
            .andExpect(jsonPath("$.fumeur").value(DEFAULT_FUMEUR.booleanValue()))
            .andExpect(jsonPath("$.acceptMusic").value(DEFAULT_ACCEPT_MUSIC.booleanValue()))
            .andExpect(jsonPath("$.acceptRadio").value(DEFAULT_ACCEPT_RADIO.booleanValue()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPassager() throws Exception {
        // Get the passager
        restPassagerMockMvc.perform(get("/api/passagers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassager() throws Exception {
        // Initialize the database
        passagerRepository.saveAndFlush(passager);

        int databaseSizeBeforeUpdate = passagerRepository.findAll().size();

        // Update the passager
        Passager updatedPassager = passagerRepository.findById(passager.getId()).get();
        // Disconnect from session so that the updates on updatedPassager are not directly saved in db
        em.detach(updatedPassager);
        updatedPassager
            .fumeur(UPDATED_FUMEUR)
            .acceptMusic(UPDATED_ACCEPT_MUSIC)
            .acceptRadio(UPDATED_ACCEPT_RADIO)
            .telephone(UPDATED_TELEPHONE);
        PassagerDTO passagerDTO = passagerMapper.toDto(updatedPassager);

        restPassagerMockMvc.perform(put("/api/passagers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passagerDTO)))
            .andExpect(status().isOk());

        // Validate the Passager in the database
        List<Passager> passagerList = passagerRepository.findAll();
        assertThat(passagerList).hasSize(databaseSizeBeforeUpdate);
        Passager testPassager = passagerList.get(passagerList.size() - 1);
        assertThat(testPassager.isFumeur()).isEqualTo(UPDATED_FUMEUR);
        assertThat(testPassager.isAcceptMusic()).isEqualTo(UPDATED_ACCEPT_MUSIC);
        assertThat(testPassager.isAcceptRadio()).isEqualTo(UPDATED_ACCEPT_RADIO);
        assertThat(testPassager.getTelephone()).isEqualTo(UPDATED_TELEPHONE);

        // Validate the Passager in Elasticsearch
        verify(mockPassagerSearchRepository, times(1)).save(testPassager);
    }

    @Test
    @Transactional
    public void updateNonExistingPassager() throws Exception {
        int databaseSizeBeforeUpdate = passagerRepository.findAll().size();

        // Create the Passager
        PassagerDTO passagerDTO = passagerMapper.toDto(passager);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassagerMockMvc.perform(put("/api/passagers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passagerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Passager in the database
        List<Passager> passagerList = passagerRepository.findAll();
        assertThat(passagerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Passager in Elasticsearch
        verify(mockPassagerSearchRepository, times(0)).save(passager);
    }

    @Test
    @Transactional
    public void deletePassager() throws Exception {
        // Initialize the database
        passagerRepository.saveAndFlush(passager);

        int databaseSizeBeforeDelete = passagerRepository.findAll().size();

        // Delete the passager
        restPassagerMockMvc.perform(delete("/api/passagers/{id}", passager.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Passager> passagerList = passagerRepository.findAll();
        assertThat(passagerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Passager in Elasticsearch
        verify(mockPassagerSearchRepository, times(1)).deleteById(passager.getId());
    }

    @Test
    @Transactional
    public void searchPassager() throws Exception {
        // Initialize the database
        passagerRepository.saveAndFlush(passager);
        when(mockPassagerSearchRepository.search(queryStringQuery("id:" + passager.getId())))
            .thenReturn(Collections.singletonList(passager));
        // Search the passager
        restPassagerMockMvc.perform(get("/api/_search/passagers?query=id:" + passager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passager.getId().intValue())))
            .andExpect(jsonPath("$.[*].fumeur").value(hasItem(DEFAULT_FUMEUR.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptMusic").value(hasItem(DEFAULT_ACCEPT_MUSIC.booleanValue())))
            .andExpect(jsonPath("$.[*].acceptRadio").value(hasItem(DEFAULT_ACCEPT_RADIO.booleanValue())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Passager.class);
        Passager passager1 = new Passager();
        passager1.setId(1L);
        Passager passager2 = new Passager();
        passager2.setId(passager1.getId());
        assertThat(passager1).isEqualTo(passager2);
        passager2.setId(2L);
        assertThat(passager1).isNotEqualTo(passager2);
        passager1.setId(null);
        assertThat(passager1).isNotEqualTo(passager2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PassagerDTO.class);
        PassagerDTO passagerDTO1 = new PassagerDTO();
        passagerDTO1.setId(1L);
        PassagerDTO passagerDTO2 = new PassagerDTO();
        assertThat(passagerDTO1).isNotEqualTo(passagerDTO2);
        passagerDTO2.setId(passagerDTO1.getId());
        assertThat(passagerDTO1).isEqualTo(passagerDTO2);
        passagerDTO2.setId(2L);
        assertThat(passagerDTO1).isNotEqualTo(passagerDTO2);
        passagerDTO1.setId(null);
        assertThat(passagerDTO1).isNotEqualTo(passagerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(passagerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(passagerMapper.fromId(null)).isNull();
    }
}
