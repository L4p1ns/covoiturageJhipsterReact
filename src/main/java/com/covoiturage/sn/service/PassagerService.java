package com.covoiturage.sn.service;

import com.covoiturage.sn.service.dto.PassagerDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.covoiturage.sn.domain.Passager}.
 */
public interface PassagerService {

    /**
     * Save a passager.
     *
     * @param passagerDTO the entity to save.
     * @return the persisted entity.
     */
    PassagerDTO save(PassagerDTO passagerDTO);

    /**
     * Get all the passagers.
     *
     * @return the list of entities.
     */
    List<PassagerDTO> findAll();


    /**
     * Get the "id" passager.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PassagerDTO> findOne(Long id);

    /**
     * Delete the "id" passager.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the passager corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<PassagerDTO> search(String query);
}
