package com.covoiturage.sn.service;

import com.covoiturage.sn.service.dto.ChauffeurDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.covoiturage.sn.domain.Chauffeur}.
 */
public interface ChauffeurService {

    /**
     * Save a chauffeur.
     *
     * @param chauffeurDTO the entity to save.
     * @return the persisted entity.
     */
    ChauffeurDTO save(ChauffeurDTO chauffeurDTO);

    /**
     * Get all the chauffeurs.
     *
     * @return the list of entities.
     */
    List<ChauffeurDTO> findAll();


    /**
     * Get the "id" chauffeur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChauffeurDTO> findOne(Long id);

    /**
     * Delete the "id" chauffeur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the chauffeur corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<ChauffeurDTO> search(String query);
}
