package com.covoiturage.sn.web.rest;

import com.covoiturage.sn.CovoijhipsterApp;
import com.covoiturage.sn.domain.Chauffeur;
import com.covoiturage.sn.repository.ChauffeurRepository;
import com.covoiturage.sn.repository.search.ChauffeurSearchRepository;
import com.covoiturage.sn.service.ChauffeurService;
import com.covoiturage.sn.service.dto.ChauffeurDTO;
import com.covoiturage.sn.service.mapper.ChauffeurMapper;
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
 * Integration tests for the {@link ChauffeurResource} REST controller.
 */
@SpringBootTest(classes = CovoijhipsterApp.class)
public class ChauffeurResourceIT {

    private static final String DEFAULT_DATE_DELIVRANCE_LICENCE = "AAAAAAAAAA";
    private static final String UPDATED_DATE_DELIVRANCE_LICENCE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    @Autowired
    private ChauffeurRepository chauffeurRepository;

    @Autowired
    private ChauffeurMapper chauffeurMapper;

    @Autowired
    private ChauffeurService chauffeurService;

    /**
     * This repository is mocked in the com.covoiturage.sn.repository.search test package.
     *
     * @see com.covoiturage.sn.repository.search.ChauffeurSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChauffeurSearchRepository mockChauffeurSearchRepository;

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

    private MockMvc restChauffeurMockMvc;

    private Chauffeur chauffeur;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChauffeurResource chauffeurResource = new ChauffeurResource(chauffeurService);
        this.restChauffeurMockMvc = MockMvcBuilders.standaloneSetup(chauffeurResource)
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
    public static Chauffeur createEntity(EntityManager em) {
        Chauffeur chauffeur = new Chauffeur()
            .dateDelivranceLicence(DEFAULT_DATE_DELIVRANCE_LICENCE)
            .telephone(DEFAULT_TELEPHONE);
        return chauffeur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chauffeur createUpdatedEntity(EntityManager em) {
        Chauffeur chauffeur = new Chauffeur()
            .dateDelivranceLicence(UPDATED_DATE_DELIVRANCE_LICENCE)
            .telephone(UPDATED_TELEPHONE);
        return chauffeur;
    }

    @BeforeEach
    public void initTest() {
        chauffeur = createEntity(em);
    }

    @Test
    @Transactional
    public void createChauffeur() throws Exception {
        int databaseSizeBeforeCreate = chauffeurRepository.findAll().size();

        // Create the Chauffeur
        ChauffeurDTO chauffeurDTO = chauffeurMapper.toDto(chauffeur);
        restChauffeurMockMvc.perform(post("/api/chauffeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chauffeurDTO)))
            .andExpect(status().isCreated());

        // Validate the Chauffeur in the database
        List<Chauffeur> chauffeurList = chauffeurRepository.findAll();
        assertThat(chauffeurList).hasSize(databaseSizeBeforeCreate + 1);
        Chauffeur testChauffeur = chauffeurList.get(chauffeurList.size() - 1);
        assertThat(testChauffeur.getDateDelivranceLicence()).isEqualTo(DEFAULT_DATE_DELIVRANCE_LICENCE);
        assertThat(testChauffeur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Validate the Chauffeur in Elasticsearch
        verify(mockChauffeurSearchRepository, times(1)).save(testChauffeur);
    }

    @Test
    @Transactional
    public void createChauffeurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chauffeurRepository.findAll().size();

        // Create the Chauffeur with an existing ID
        chauffeur.setId(1L);
        ChauffeurDTO chauffeurDTO = chauffeurMapper.toDto(chauffeur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChauffeurMockMvc.perform(post("/api/chauffeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chauffeurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chauffeur in the database
        List<Chauffeur> chauffeurList = chauffeurRepository.findAll();
        assertThat(chauffeurList).hasSize(databaseSizeBeforeCreate);

        // Validate the Chauffeur in Elasticsearch
        verify(mockChauffeurSearchRepository, times(0)).save(chauffeur);
    }


    @Test
    @Transactional
    public void checkDateDelivranceLicenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = chauffeurRepository.findAll().size();
        // set the field null
        chauffeur.setDateDelivranceLicence(null);

        // Create the Chauffeur, which fails.
        ChauffeurDTO chauffeurDTO = chauffeurMapper.toDto(chauffeur);

        restChauffeurMockMvc.perform(post("/api/chauffeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chauffeurDTO)))
            .andExpect(status().isBadRequest());

        List<Chauffeur> chauffeurList = chauffeurRepository.findAll();
        assertThat(chauffeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelephoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = chauffeurRepository.findAll().size();
        // set the field null
        chauffeur.setTelephone(null);

        // Create the Chauffeur, which fails.
        ChauffeurDTO chauffeurDTO = chauffeurMapper.toDto(chauffeur);

        restChauffeurMockMvc.perform(post("/api/chauffeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chauffeurDTO)))
            .andExpect(status().isBadRequest());

        List<Chauffeur> chauffeurList = chauffeurRepository.findAll();
        assertThat(chauffeurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChauffeurs() throws Exception {
        // Initialize the database
        chauffeurRepository.saveAndFlush(chauffeur);

        // Get all the chauffeurList
        restChauffeurMockMvc.perform(get("/api/chauffeurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chauffeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDelivranceLicence").value(hasItem(DEFAULT_DATE_DELIVRANCE_LICENCE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getChauffeur() throws Exception {
        // Initialize the database
        chauffeurRepository.saveAndFlush(chauffeur);

        // Get the chauffeur
        restChauffeurMockMvc.perform(get("/api/chauffeurs/{id}", chauffeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chauffeur.getId().intValue()))
            .andExpect(jsonPath("$.dateDelivranceLicence").value(DEFAULT_DATE_DELIVRANCE_LICENCE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChauffeur() throws Exception {
        // Get the chauffeur
        restChauffeurMockMvc.perform(get("/api/chauffeurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChauffeur() throws Exception {
        // Initialize the database
        chauffeurRepository.saveAndFlush(chauffeur);

        int databaseSizeBeforeUpdate = chauffeurRepository.findAll().size();

        // Update the chauffeur
        Chauffeur updatedChauffeur = chauffeurRepository.findById(chauffeur.getId()).get();
        // Disconnect from session so that the updates on updatedChauffeur are not directly saved in db
        em.detach(updatedChauffeur);
        updatedChauffeur
            .dateDelivranceLicence(UPDATED_DATE_DELIVRANCE_LICENCE)
            .telephone(UPDATED_TELEPHONE);
        ChauffeurDTO chauffeurDTO = chauffeurMapper.toDto(updatedChauffeur);

        restChauffeurMockMvc.perform(put("/api/chauffeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chauffeurDTO)))
            .andExpect(status().isOk());

        // Validate the Chauffeur in the database
        List<Chauffeur> chauffeurList = chauffeurRepository.findAll();
        assertThat(chauffeurList).hasSize(databaseSizeBeforeUpdate);
        Chauffeur testChauffeur = chauffeurList.get(chauffeurList.size() - 1);
        assertThat(testChauffeur.getDateDelivranceLicence()).isEqualTo(UPDATED_DATE_DELIVRANCE_LICENCE);
        assertThat(testChauffeur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);

        // Validate the Chauffeur in Elasticsearch
        verify(mockChauffeurSearchRepository, times(1)).save(testChauffeur);
    }

    @Test
    @Transactional
    public void updateNonExistingChauffeur() throws Exception {
        int databaseSizeBeforeUpdate = chauffeurRepository.findAll().size();

        // Create the Chauffeur
        ChauffeurDTO chauffeurDTO = chauffeurMapper.toDto(chauffeur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChauffeurMockMvc.perform(put("/api/chauffeurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chauffeurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chauffeur in the database
        List<Chauffeur> chauffeurList = chauffeurRepository.findAll();
        assertThat(chauffeurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Chauffeur in Elasticsearch
        verify(mockChauffeurSearchRepository, times(0)).save(chauffeur);
    }

    @Test
    @Transactional
    public void deleteChauffeur() throws Exception {
        // Initialize the database
        chauffeurRepository.saveAndFlush(chauffeur);

        int databaseSizeBeforeDelete = chauffeurRepository.findAll().size();

        // Delete the chauffeur
        restChauffeurMockMvc.perform(delete("/api/chauffeurs/{id}", chauffeur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chauffeur> chauffeurList = chauffeurRepository.findAll();
        assertThat(chauffeurList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Chauffeur in Elasticsearch
        verify(mockChauffeurSearchRepository, times(1)).deleteById(chauffeur.getId());
    }

    @Test
    @Transactional
    public void searchChauffeur() throws Exception {
        // Initialize the database
        chauffeurRepository.saveAndFlush(chauffeur);
        when(mockChauffeurSearchRepository.search(queryStringQuery("id:" + chauffeur.getId())))
            .thenReturn(Collections.singletonList(chauffeur));
        // Search the chauffeur
        restChauffeurMockMvc.perform(get("/api/_search/chauffeurs?query=id:" + chauffeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chauffeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDelivranceLicence").value(hasItem(DEFAULT_DATE_DELIVRANCE_LICENCE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chauffeur.class);
        Chauffeur chauffeur1 = new Chauffeur();
        chauffeur1.setId(1L);
        Chauffeur chauffeur2 = new Chauffeur();
        chauffeur2.setId(chauffeur1.getId());
        assertThat(chauffeur1).isEqualTo(chauffeur2);
        chauffeur2.setId(2L);
        assertThat(chauffeur1).isNotEqualTo(chauffeur2);
        chauffeur1.setId(null);
        assertThat(chauffeur1).isNotEqualTo(chauffeur2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChauffeurDTO.class);
        ChauffeurDTO chauffeurDTO1 = new ChauffeurDTO();
        chauffeurDTO1.setId(1L);
        ChauffeurDTO chauffeurDTO2 = new ChauffeurDTO();
        assertThat(chauffeurDTO1).isNotEqualTo(chauffeurDTO2);
        chauffeurDTO2.setId(chauffeurDTO1.getId());
        assertThat(chauffeurDTO1).isEqualTo(chauffeurDTO2);
        chauffeurDTO2.setId(2L);
        assertThat(chauffeurDTO1).isNotEqualTo(chauffeurDTO2);
        chauffeurDTO1.setId(null);
        assertThat(chauffeurDTO1).isNotEqualTo(chauffeurDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chauffeurMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chauffeurMapper.fromId(null)).isNull();
    }
}
