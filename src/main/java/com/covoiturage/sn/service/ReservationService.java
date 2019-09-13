package com.covoiturage.sn.service;

import com.covoiturage.sn.service.dto.ReservationDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.covoiturage.sn.domain.Reservation}.
 */
public interface ReservationService {

    /**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    ReservationDTO save(ReservationDTO reservationDTO);

    /**
     * Get all the reservations.
     *
     * @return the list of entities.
     */
    List<ReservationDTO> findAll();


    /**
     * Get the "id" reservation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReservationDTO> findOne(Long id);

    /**
     * Delete the "id" reservation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the reservation corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<ReservationDTO> search(String query);
}
