package com.covoiturage.sn.web.rest;

import com.covoiturage.sn.service.PassagerService;
import com.covoiturage.sn.web.rest.errors.BadRequestAlertException;
import com.covoiturage.sn.service.dto.PassagerDTO;

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
 * REST controller for managing {@link com.covoiturage.sn.domain.Passager}.
 */
@RestController
@RequestMapping("/api")
public class PassagerResource {

    private final Logger log = LoggerFactory.getLogger(PassagerResource.class);

    private static final String ENTITY_NAME = "passager";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassagerService passagerService;

    public PassagerResource(PassagerService passagerService) {
        this.passagerService = passagerService;
    }

    /**
     * {@code POST  /passagers} : Create a new passager.
     *
     * @param passagerDTO the passagerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passagerDTO, or with status {@code 400 (Bad Request)} if the passager has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/passagers")
    public ResponseEntity<PassagerDTO> createPassager(@Valid @RequestBody PassagerDTO passagerDTO) throws URISyntaxException {
        log.debug("REST request to save Passager : {}", passagerDTO);
        if (passagerDTO.getId() != null) {
            throw new BadRequestAlertException("A new passager cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PassagerDTO result = passagerService.save(passagerDTO);
        return ResponseEntity.created(new URI("/api/passagers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /passagers} : Updates an existing passager.
     *
     * @param passagerDTO the passagerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passagerDTO,
     * or with status {@code 400 (Bad Request)} if the passagerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passagerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/passagers")
    public ResponseEntity<PassagerDTO> updatePassager(@Valid @RequestBody PassagerDTO passagerDTO) throws URISyntaxException {
        log.debug("REST request to update Passager : {}", passagerDTO);
        if (passagerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PassagerDTO result = passagerService.save(passagerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passagerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /passagers} : get all the passagers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passagers in body.
     */
    @GetMapping("/passagers")
    public List<PassagerDTO> getAllPassagers() {
        log.debug("REST request to get all Passagers");
        return passagerService.findAll();
    }

    /**
     * {@code GET  /passagers/:id} : get the "id" passager.
     *
     * @param id the id of the passagerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passagerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/passagers/{id}")
    public ResponseEntity<PassagerDTO> getPassager(@PathVariable Long id) {
        log.debug("REST request to get Passager : {}", id);
        Optional<PassagerDTO> passagerDTO = passagerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passagerDTO);
    }

    /**
     * {@code DELETE  /passagers/:id} : delete the "id" passager.
     *
     * @param id the id of the passagerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passagers/{id}")
    public ResponseEntity<Void> deletePassager(@PathVariable Long id) {
        log.debug("REST request to delete Passager : {}", id);
        passagerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/passagers?query=:query} : search for the passager corresponding
     * to the query.
     *
     * @param query the query of the passager search.
     * @return the result of the search.
     */
    @GetMapping("/_search/passagers")
    public List<PassagerDTO> searchPassagers(@RequestParam String query) {
        log.debug("REST request to search Passagers for query {}", query);
        return passagerService.search(query);
    }

}
