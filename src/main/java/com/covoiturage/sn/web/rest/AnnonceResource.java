package com.covoiturage.sn.web.rest;

import com.covoiturage.sn.service.AnnonceService;
import com.covoiturage.sn.web.rest.errors.BadRequestAlertException;
import com.covoiturage.sn.service.dto.AnnonceDTO;

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
 * REST controller for managing {@link com.covoiturage.sn.domain.Annonce}.
 */
@RestController
@RequestMapping("/api")
public class AnnonceResource {

    private final Logger log = LoggerFactory.getLogger(AnnonceResource.class);

    private static final String ENTITY_NAME = "annonce";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnonceService annonceService;

    public AnnonceResource(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    /**
     * {@code POST  /annonces} : Create a new annonce.
     *
     * @param annonceDTO the annonceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annonceDTO, or with status {@code 400 (Bad Request)} if the annonce has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annonces")
    public ResponseEntity<AnnonceDTO> createAnnonce(@Valid @RequestBody AnnonceDTO annonceDTO) throws URISyntaxException {
        log.debug("REST request to save Annonce : {}", annonceDTO);
        if (annonceDTO.getId() != null) {
            throw new BadRequestAlertException("A new annonce cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnonceDTO result = annonceService.save(annonceDTO);
        return ResponseEntity.created(new URI("/api/annonces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annonces} : Updates an existing annonce.
     *
     * @param annonceDTO the annonceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annonceDTO,
     * or with status {@code 400 (Bad Request)} if the annonceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annonceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annonces")
    public ResponseEntity<AnnonceDTO> updateAnnonce(@Valid @RequestBody AnnonceDTO annonceDTO) throws URISyntaxException {
        log.debug("REST request to update Annonce : {}", annonceDTO);
        if (annonceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnonceDTO result = annonceService.save(annonceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annonceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /annonces} : get all the annonces.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annonces in body.
     */
    @GetMapping("/annonces")
    public List<AnnonceDTO> getAllAnnonces() {
        log.debug("REST request to get all Annonces");
        return annonceService.findAll();
    }

    /**
     * {@code GET  /annonces/:id} : get the "id" annonce.
     *
     * @param id the id of the annonceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annonceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annonces/{id}")
    public ResponseEntity<AnnonceDTO> getAnnonce(@PathVariable Long id) {
        log.debug("REST request to get Annonce : {}", id);
        Optional<AnnonceDTO> annonceDTO = annonceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annonceDTO);
    }

    /**
     * {@code DELETE  /annonces/:id} : delete the "id" annonce.
     *
     * @param id the id of the annonceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annonces/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        log.debug("REST request to delete Annonce : {}", id);
        annonceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/annonces?query=:query} : search for the annonce corresponding
     * to the query.
     *
     * @param query the query of the annonce search.
     * @return the result of the search.
     */
    @GetMapping("/_search/annonces")
    public List<AnnonceDTO> searchAnnonces(@RequestParam String query) {
        log.debug("REST request to search Annonces for query {}", query);
        return annonceService.search(query);
    }

}
