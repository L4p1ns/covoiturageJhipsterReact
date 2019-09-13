package com.covoiturage.sn.web.rest;

import com.covoiturage.sn.CovoijhipsterApp;
import com.covoiturage.sn.domain.Annonce;
import com.covoiturage.sn.repository.AnnonceRepository;
import com.covoiturage.sn.repository.search.AnnonceSearchRepository;
import com.covoiturage.sn.service.AnnonceService;
import com.covoiturage.sn.service.dto.AnnonceDTO;
import com.covoiturage.sn.service.mapper.AnnonceMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static com.covoiturage.sn.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.covoiturage.sn.domain.enumeration.EtatAnnonce;
/**
 * Integration tests for the {@link AnnonceResource} REST controller.
 */
@SpringBootTest(classes = CovoijhipsterApp.class)
public class AnnonceResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_LIEU_DEPART = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_DEPART = "BBBBBBBBBB";

    private static final String DEFAULT_LIEU_ARRIVEE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_ARRIVEE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_VOYAGE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_VOYAGE = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_DATE_VOYAGE = Instant.ofEpochMilli(-1L);

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_MUSIC = false;
    private static final Boolean UPDATED_MUSIC = true;

    private static final Boolean DEFAULT_FUMER = false;
    private static final Boolean UPDATED_FUMER = true;

    private static final Boolean DEFAULT_RADIO = false;
    private static final Boolean UPDATED_RADIO = true;

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);
    private static final Instant SMALLER_DATE_CREATION = Instant.ofEpochMilli(-1L);

    private static final EtatAnnonce DEFAULT_ETAT_ANNONCE = EtatAnnonce.ACTIVE;
    private static final EtatAnnonce UPDATED_ETAT_ANNONCE = EtatAnnonce.ANNULER;

    @Autowired
    private AnnonceRepository annonceRepository;

    @Autowired
    private AnnonceMapper annonceMapper;

    @Autowired
    private AnnonceService annonceService;

    /**
     * This repository is mocked in the com.covoiturage.sn.repository.search test package.
     *
     * @see com.covoiturage.sn.repository.search.AnnonceSearchRepositoryMockConfiguration
     */
    @Autowired
    private AnnonceSearchRepository mockAnnonceSearchRepository;

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

    private MockMvc restAnnonceMockMvc;

    private Annonce annonce;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnnonceResource annonceResource = new AnnonceResource(annonceService);
        this.restAnnonceMockMvc = MockMvcBuilders.standaloneSetup(annonceResource)
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
    public static Annonce createEntity(EntityManager em) {
        Annonce annonce = new Annonce()
            .nom(DEFAULT_NOM)
            .lieuDepart(DEFAULT_LIEU_DEPART)
            .lieuArrivee(DEFAULT_LIEU_ARRIVEE)
            .dateVoyage(DEFAULT_DATE_VOYAGE)
            .detail(DEFAULT_DETAIL)
            .music(DEFAULT_MUSIC)
            .fumer(DEFAULT_FUMER)
            .radio(DEFAULT_RADIO)
            .dateCreation(DEFAULT_DATE_CREATION)
            .etatAnnonce(DEFAULT_ETAT_ANNONCE);
        return annonce;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annonce createUpdatedEntity(EntityManager em) {
        Annonce annonce = new Annonce()
            .nom(UPDATED_NOM)
            .lieuDepart(UPDATED_LIEU_DEPART)
            .lieuArrivee(UPDATED_LIEU_ARRIVEE)
            .dateVoyage(UPDATED_DATE_VOYAGE)
            .detail(UPDATED_DETAIL)
            .music(UPDATED_MUSIC)
            .fumer(UPDATED_FUMER)
            .radio(UPDATED_RADIO)
            .dateCreation(UPDATED_DATE_CREATION)
            .etatAnnonce(UPDATED_ETAT_ANNONCE);
        return annonce;
    }

    @BeforeEach
    public void initTest() {
        annonce = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnnonce() throws Exception {
        int databaseSizeBeforeCreate = annonceRepository.findAll().size();

        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);
        restAnnonceMockMvc.perform(post("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonceDTO)))
            .andExpect(status().isCreated());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeCreate + 1);
        Annonce testAnnonce = annonceList.get(annonceList.size() - 1);
        assertThat(testAnnonce.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAnnonce.getLieuDepart()).isEqualTo(DEFAULT_LIEU_DEPART);
        assertThat(testAnnonce.getLieuArrivee()).isEqualTo(DEFAULT_LIEU_ARRIVEE);
        assertThat(testAnnonce.getDateVoyage()).isEqualTo(DEFAULT_DATE_VOYAGE);
        assertThat(testAnnonce.getDetail()).isEqualTo(DEFAULT_DETAIL);
        assertThat(testAnnonce.isMusic()).isEqualTo(DEFAULT_MUSIC);
        assertThat(testAnnonce.isFumer()).isEqualTo(DEFAULT_FUMER);
        assertThat(testAnnonce.isRadio()).isEqualTo(DEFAULT_RADIO);
        assertThat(testAnnonce.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testAnnonce.getEtatAnnonce()).isEqualTo(DEFAULT_ETAT_ANNONCE);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(1)).save(testAnnonce);
    }

    @Test
    @Transactional
    public void createAnnonceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = annonceRepository.findAll().size();

        // Create the Annonce with an existing ID
        annonce.setId(1L);
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnonceMockMvc.perform(post("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(0)).save(annonce);
    }


    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = annonceRepository.findAll().size();
        // set the field null
        annonce.setNom(null);

        // Create the Annonce, which fails.
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        restAnnonceMockMvc.perform(post("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonceDTO)))
            .andExpect(status().isBadRequest());

        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLieuDepartIsRequired() throws Exception {
        int databaseSizeBeforeTest = annonceRepository.findAll().size();
        // set the field null
        annonce.setLieuDepart(null);

        // Create the Annonce, which fails.
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        restAnnonceMockMvc.perform(post("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonceDTO)))
            .andExpect(status().isBadRequest());

        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLieuArriveeIsRequired() throws Exception {
        int databaseSizeBeforeTest = annonceRepository.findAll().size();
        // set the field null
        annonce.setLieuArrivee(null);

        // Create the Annonce, which fails.
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        restAnnonceMockMvc.perform(post("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonceDTO)))
            .andExpect(status().isBadRequest());

        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnnonces() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        // Get all the annonceList
        restAnnonceMockMvc.perform(get("/api/annonces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annonce.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].lieuDepart").value(hasItem(DEFAULT_LIEU_DEPART.toString())))
            .andExpect(jsonPath("$.[*].lieuArrivee").value(hasItem(DEFAULT_LIEU_ARRIVEE.toString())))
            .andExpect(jsonPath("$.[*].dateVoyage").value(hasItem(DEFAULT_DATE_VOYAGE.toString())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL.toString())))
            .andExpect(jsonPath("$.[*].music").value(hasItem(DEFAULT_MUSIC.booleanValue())))
            .andExpect(jsonPath("$.[*].fumer").value(hasItem(DEFAULT_FUMER.booleanValue())))
            .andExpect(jsonPath("$.[*].radio").value(hasItem(DEFAULT_RADIO.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].etatAnnonce").value(hasItem(DEFAULT_ETAT_ANNONCE.toString())));
    }
    
    @Test
    @Transactional
    public void getAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        // Get the annonce
        restAnnonceMockMvc.perform(get("/api/annonces/{id}", annonce.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(annonce.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.lieuDepart").value(DEFAULT_LIEU_DEPART.toString()))
            .andExpect(jsonPath("$.lieuArrivee").value(DEFAULT_LIEU_ARRIVEE.toString()))
            .andExpect(jsonPath("$.dateVoyage").value(DEFAULT_DATE_VOYAGE.toString()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL.toString()))
            .andExpect(jsonPath("$.music").value(DEFAULT_MUSIC.booleanValue()))
            .andExpect(jsonPath("$.fumer").value(DEFAULT_FUMER.booleanValue()))
            .andExpect(jsonPath("$.radio").value(DEFAULT_RADIO.booleanValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.etatAnnonce").value(DEFAULT_ETAT_ANNONCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnnonce() throws Exception {
        // Get the annonce
        restAnnonceMockMvc.perform(get("/api/annonces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();

        // Update the annonce
        Annonce updatedAnnonce = annonceRepository.findById(annonce.getId()).get();
        // Disconnect from session so that the updates on updatedAnnonce are not directly saved in db
        em.detach(updatedAnnonce);
        updatedAnnonce
            .nom(UPDATED_NOM)
            .lieuDepart(UPDATED_LIEU_DEPART)
            .lieuArrivee(UPDATED_LIEU_ARRIVEE)
            .dateVoyage(UPDATED_DATE_VOYAGE)
            .detail(UPDATED_DETAIL)
            .music(UPDATED_MUSIC)
            .fumer(UPDATED_FUMER)
            .radio(UPDATED_RADIO)
            .dateCreation(UPDATED_DATE_CREATION)
            .etatAnnonce(UPDATED_ETAT_ANNONCE);
        AnnonceDTO annonceDTO = annonceMapper.toDto(updatedAnnonce);

        restAnnonceMockMvc.perform(put("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonceDTO)))
            .andExpect(status().isOk());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);
        Annonce testAnnonce = annonceList.get(annonceList.size() - 1);
        assertThat(testAnnonce.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAnnonce.getLieuDepart()).isEqualTo(UPDATED_LIEU_DEPART);
        assertThat(testAnnonce.getLieuArrivee()).isEqualTo(UPDATED_LIEU_ARRIVEE);
        assertThat(testAnnonce.getDateVoyage()).isEqualTo(UPDATED_DATE_VOYAGE);
        assertThat(testAnnonce.getDetail()).isEqualTo(UPDATED_DETAIL);
        assertThat(testAnnonce.isMusic()).isEqualTo(UPDATED_MUSIC);
        assertThat(testAnnonce.isFumer()).isEqualTo(UPDATED_FUMER);
        assertThat(testAnnonce.isRadio()).isEqualTo(UPDATED_RADIO);
        assertThat(testAnnonce.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testAnnonce.getEtatAnnonce()).isEqualTo(UPDATED_ETAT_ANNONCE);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(1)).save(testAnnonce);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnonce() throws Exception {
        int databaseSizeBeforeUpdate = annonceRepository.findAll().size();

        // Create the Annonce
        AnnonceDTO annonceDTO = annonceMapper.toDto(annonce);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnonceMockMvc.perform(put("/api/annonces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(annonceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Annonce in the database
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(0)).save(annonce);
    }

    @Test
    @Transactional
    public void deleteAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);

        int databaseSizeBeforeDelete = annonceRepository.findAll().size();

        // Delete the annonce
        restAnnonceMockMvc.perform(delete("/api/annonces/{id}", annonce.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Annonce> annonceList = annonceRepository.findAll();
        assertThat(annonceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Annonce in Elasticsearch
        verify(mockAnnonceSearchRepository, times(1)).deleteById(annonce.getId());
    }

    @Test
    @Transactional
    public void searchAnnonce() throws Exception {
        // Initialize the database
        annonceRepository.saveAndFlush(annonce);
        when(mockAnnonceSearchRepository.search(queryStringQuery("id:" + annonce.getId())))
            .thenReturn(Collections.singletonList(annonce));
        // Search the annonce
        restAnnonceMockMvc.perform(get("/api/_search/annonces?query=id:" + annonce.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annonce.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].lieuDepart").value(hasItem(DEFAULT_LIEU_DEPART)))
            .andExpect(jsonPath("$.[*].lieuArrivee").value(hasItem(DEFAULT_LIEU_ARRIVEE)))
            .andExpect(jsonPath("$.[*].dateVoyage").value(hasItem(DEFAULT_DATE_VOYAGE.toString())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)))
            .andExpect(jsonPath("$.[*].music").value(hasItem(DEFAULT_MUSIC.booleanValue())))
            .andExpect(jsonPath("$.[*].fumer").value(hasItem(DEFAULT_FUMER.booleanValue())))
            .andExpect(jsonPath("$.[*].radio").value(hasItem(DEFAULT_RADIO.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].etatAnnonce").value(hasItem(DEFAULT_ETAT_ANNONCE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annonce.class);
        Annonce annonce1 = new Annonce();
        annonce1.setId(1L);
        Annonce annonce2 = new Annonce();
        annonce2.setId(annonce1.getId());
        assertThat(annonce1).isEqualTo(annonce2);
        annonce2.setId(2L);
        assertThat(annonce1).isNotEqualTo(annonce2);
        annonce1.setId(null);
        assertThat(annonce1).isNotEqualTo(annonce2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnonceDTO.class);
        AnnonceDTO annonceDTO1 = new AnnonceDTO();
        annonceDTO1.setId(1L);
        AnnonceDTO annonceDTO2 = new AnnonceDTO();
        assertThat(annonceDTO1).isNotEqualTo(annonceDTO2);
        annonceDTO2.setId(annonceDTO1.getId());
        assertThat(annonceDTO1).isEqualTo(annonceDTO2);
        annonceDTO2.setId(2L);
        assertThat(annonceDTO1).isNotEqualTo(annonceDTO2);
        annonceDTO1.setId(null);
        assertThat(annonceDTO1).isNotEqualTo(annonceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(annonceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(annonceMapper.fromId(null)).isNull();
    }
}
