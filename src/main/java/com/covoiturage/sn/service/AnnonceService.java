package com.covoiturage.sn.service;

import com.covoiturage.sn.service.dto.AnnonceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.covoiturage.sn.domain.Annonce}.
 */
public interface AnnonceService {

    /**
     * Save a annonce.
     *
     * @param annonceDTO the entity to save.
     * @return the persisted entity.
     */
    AnnonceDTO save(AnnonceDTO annonceDTO);

    /**
     * Get all the annonces.
     *
     * @return the list of entities.
     */
    List<AnnonceDTO> findAll();


    /**
     * Get the "id" annonce.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnnonceDTO> findOne(Long id);

    /**
     * Delete the "id" annonce.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the annonce corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<AnnonceDTO> search(String query);
}
