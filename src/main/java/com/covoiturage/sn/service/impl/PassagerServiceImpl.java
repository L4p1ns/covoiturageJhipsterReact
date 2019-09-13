package com.covoiturage.sn.service.impl;

import com.covoiturage.sn.service.PassagerService;
import com.covoiturage.sn.domain.Passager;
import com.covoiturage.sn.repository.PassagerRepository;
import com.covoiturage.sn.repository.search.PassagerSearchRepository;
import com.covoiturage.sn.service.dto.PassagerDTO;
import com.covoiturage.sn.service.mapper.PassagerMapper;
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
 * Service Implementation for managing {@link Passager}.
 */
@Service
@Transactional
public class PassagerServiceImpl implements PassagerService {

    private final Logger log = LoggerFactory.getLogger(PassagerServiceImpl.class);

    private final PassagerRepository passagerRepository;

    private final PassagerMapper passagerMapper;

    private final PassagerSearchRepository passagerSearchRepository;

    public PassagerServiceImpl(PassagerRepository passagerRepository, PassagerMapper passagerMapper, PassagerSearchRepository passagerSearchRepository) {
        this.passagerRepository = passagerRepository;
        this.passagerMapper = passagerMapper;
        this.passagerSearchRepository = passagerSearchRepository;
    }

    /**
     * Save a passager.
     *
     * @param passagerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PassagerDTO save(PassagerDTO passagerDTO) {
        log.debug("Request to save Passager : {}", passagerDTO);
        Passager passager = passagerMapper.toEntity(passagerDTO);
        passager = passagerRepository.save(passager);
        PassagerDTO result = passagerMapper.toDto(passager);
        passagerSearchRepository.save(passager);
        return result;
    }

    /**
     * Get all the passagers.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PassagerDTO> findAll() {
        log.debug("Request to get all Passagers");
        return passagerRepository.findAll().stream()
            .map(passagerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one passager by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PassagerDTO> findOne(Long id) {
        log.debug("Request to get Passager : {}", id);
        return passagerRepository.findById(id)
            .map(passagerMapper::toDto);
    }

    /**
     * Delete the passager by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Passager : {}", id);
        passagerRepository.deleteById(id);
        passagerSearchRepository.deleteById(id);
    }

    /**
     * Search for the passager corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PassagerDTO> search(String query) {
        log.debug("Request to search Passagers for query {}", query);
        return StreamSupport
            .stream(passagerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(passagerMapper::toDto)
            .collect(Collectors.toList());
    }
}
