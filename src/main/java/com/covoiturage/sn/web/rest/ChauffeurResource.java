package com.covoiturage.sn.web.rest;

import com.covoiturage.sn.service.ChauffeurService;
import com.covoiturage.sn.web.rest.errors.BadRequestAlertException;
import com.covoiturage.sn.service.dto.ChauffeurDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.covoiturage.sn.domain.Chauffeur}.
 */
@RestController
@RequestMapping("/api")
public class ChauffeurResource {

    private final Logger log = LoggerFactory.getLogger(ChauffeurResource.class);

    private static final String ENTITY_NAME = "chauffeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChauffeurService chauffeurService;

    public ChauffeurResource(ChauffeurService chauffeurService) {
        this.chauffeurService = chauffeurService;
    }

    /**
     * {@code POST  /chauffeurs} : Create a new chauffeur.
     *
     * @param chauffeurDTO the chauffeurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chauffeurDTO, or with status {@code 400 (Bad Request)} if the chauffeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chauffeurs")
    public ResponseEntity<ChauffeurDTO> createChauffeur(@Valid @RequestBody ChauffeurDTO chauffeurDTO) throws URISyntaxException {
        log.debug("REST request to save Chauffeur : {}", chauffeurDTO);
        if (chauffeurDTO.getId() != null) {
            throw new BadRequestAlertException("A new chauffeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChauffeurDTO result = chauffeurService.save(chauffeurDTO);
        return ResponseEntity.created(new URI("/api/chauffeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chauffeurs} : Updates an existing chauffeur.
     *
     * @param chauffeurDTO the chauffeurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chauffeurDTO,
     * or with status {@code 400 (Bad Request)} if the chauffeurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chauffeurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chauffeurs")
    public ResponseEntity<ChauffeurDTO> updateChauffeur(@Valid @RequestBody ChauffeurDTO chauffeurDTO) throws URISyntaxException {
        log.debug("REST request to update Chauffeur : {}", chauffeurDTO);
        if (chauffeurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChauffeurDTO result = chauffeurService.save(chauffeurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chauffeurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /chauffeurs} : get all the chauffeurs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chauffeurs in body.
     */
    @GetMapping("/chauffeurs")
    public List<ChauffeurDTO> getAllChauffeurs() {
        log.debug("REST request to get all Chauffeurs");
        return chauffeurService.findAll();
    }

    /**
     * {@code GET  /chauffeurs/:id} : get the "id" chauffeur.
     *
     * @param id the id of the chauffeurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chauffeurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chauffeurs/{id}")
    public ResponseEntity<ChauffeurDTO> getChauffeur(@PathVariable Long id) {
        log.debug("REST request to get Chauffeur : {}", id);
        Optional<ChauffeurDTO> chauffeurDTO = chauffeurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chauffeurDTO);
    }

    /**
     * {@code DELETE  /chauffeurs/:id} : delete the "id" chauffeur.
     *
     * @param id the id of the chauffeurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chauffeurs/{id}")
    public ResponseEntity<Void> deleteChauffeur(@PathVariable Long id) {
        log.debug("REST request to delete Chauffeur : {}", id);
        chauffeurService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/chauffeurs?query=:query} : search for the chauffeur corresponding
     * to the query.
     *
     * @param query the query of the chauffeur search.
     * @return the result of the search.
     */
    @GetMapping("/_search/chauffeurs")
    public List<ChauffeurDTO> searchChauffeurs(@RequestParam String query) {
        log.debug("REST request to search Chauffeurs for query {}", query);
        return chauffeurService.search(query);
    }

}
