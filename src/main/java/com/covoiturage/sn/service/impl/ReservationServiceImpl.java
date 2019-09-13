package com.covoiturage.sn.service.impl;

import com.covoiturage.sn.service.ReservationService;
import com.covoiturage.sn.domain.Reservation;
import com.covoiturage.sn.repository.ReservationRepository;
import com.covoiturage.sn.repository.search.ReservationSearchRepository;
import com.covoiturage.sn.service.dto.ReservationDTO;
import com.covoiturage.sn.service.mapper.ReservationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Reservation}.
 */
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    private final ReservationSearchRepository reservationSearchRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper, ReservationSearchRepository reservationSearchRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.reservationSearchRepository = reservationSearchRepository;
    }

    /**
     * Save a reservation.
     *
     * @param reservationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ReservationDTO save(ReservationDTO reservationDTO) {
        log.debug("Request to save Reservation : {}", reservationDTO);
        Reservation reservation = reservationMapper.toEntity(reservationDTO);
        reservation = reservationRepository.save(reservation);
        ReservationDTO result = reservationMapper.toDto(reservation);
        reservationSearchRepository.save(reservation);
        return result;
    }

    /**
     * Get all the reservations.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> findAll() {
        log.debug("Request to get all Reservations");
        return reservationRepository.findAll().stream()
            .map(reservationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one reservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationDTO> findOne(Long id) {
        log.debug("Request to get Reservation : {}", id);
        return reservationRepository.findById(id)
            .map(reservationMapper::toDto);
    }

    /**
     * Delete the reservation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reservation : {}", id);
        reservationRepository.deleteById(id);
        reservationSearchRepository.deleteById(id);
    }

    /**
     * Search for the reservation corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReservationDTO> search(String query) {
        log.debug("Request to search Reservations for query {}", query);
        return StreamSupport
            .stream(reservationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(reservationMapper::toDto)
            .collect(Collectors.toList());
    }
}
