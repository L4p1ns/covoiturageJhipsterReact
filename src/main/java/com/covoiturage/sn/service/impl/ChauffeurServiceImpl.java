package com.covoiturage.sn.service.impl;

import com.covoiturage.sn.service.ChauffeurService;
import com.covoiturage.sn.domain.Chauffeur;
import com.covoiturage.sn.repository.ChauffeurRepository;
import com.covoiturage.sn.repository.search.ChauffeurSearchRepository;
import com.covoiturage.sn.service.dto.ChauffeurDTO;
import com.covoiturage.sn.service.mapper.ChauffeurMapper;
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
 * Service Implementation for managing {@link Chauffeur}.
 */
@Service
@Transactional
public class ChauffeurServiceImpl implements ChauffeurService {

    private final Logger log = LoggerFactory.getLogger(ChauffeurServiceImpl.class);

    private final ChauffeurRepository chauffeurRepository;

    private final ChauffeurMapper chauffeurMapper;

    private final ChauffeurSearchRepository chauffeurSearchRepository;

    public ChauffeurServiceImpl(ChauffeurRepository chauffeurRepository, ChauffeurMapper chauffeurMapper, ChauffeurSearchRepository chauffeurSearchRepository) {
        this.chauffeurRepository = chauffeurRepository;
        this.chauffeurMapper = chauffeurMapper;
        this.chauffeurSearchRepository = chauffeurSearchRepository;
    }

    /**
     * Save a chauffeur.
     *
     * @param chauffeurDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChauffeurDTO save(ChauffeurDTO chauffeurDTO) {
        log.debug("Request to save Chauffeur : {}", chauffeurDTO);
        Chauffeur chauffeur = chauffeurMapper.toEntity(chauffeurDTO);
        chauffeur = chauffeurRepository.save(chauffeur);
        ChauffeurDTO result = chauffeurMapper.toDto(chauffeur);
        chauffeurSearchRepository.save(chauffeur);
        return result;
    }

    /**
     * Get all the chauffeurs.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChauffeurDTO> findAll() {
        log.debug("Request to get all Chauffeurs");
        return chauffeurRepository.findAll().stream()
            .map(chauffeurMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one chauffeur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChauffeurDTO> findOne(Long id) {
        log.debug("Request to get Chauffeur : {}", id);
        return chauffeurRepository.findById(id)
            .map(chauffeurMapper::toDto);
    }

    /**
     * Delete the chauffeur by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Chauffeur : {}", id);
        chauffeurRepository.deleteById(id);
        chauffeurSearchRepository.deleteById(id);
    }

    /**
     * Search for the chauffeur corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChauffeurDTO> search(String query) {
        log.debug("Request to search Chauffeurs for query {}", query);
        return StreamSupport
            .stream(chauffeurSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(chauffeurMapper::toDto)
            .collect(Collectors.toList());
    }
}
