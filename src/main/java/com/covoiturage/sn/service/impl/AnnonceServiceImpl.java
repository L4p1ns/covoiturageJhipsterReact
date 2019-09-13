package com.covoiturage.sn.service.impl;

import com.covoiturage.sn.service.AnnonceService;
import com.covoiturage.sn.domain.Annonce;
import com.covoiturage.sn.repository.AnnonceRepository;
import com.covoiturage.sn.repository.search.AnnonceSearchRepository;
import com.covoiturage.sn.service.dto.AnnonceDTO;
import com.covoiturage.sn.service.mapper.AnnonceMapper;
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
 * Service Implementation for managing {@link Annonce}.
 */
@Service
@Transactional
public class AnnonceServiceImpl implements AnnonceService {

    private final Logger log = LoggerFactory.getLogger(AnnonceServiceImpl.class);

    private final AnnonceRepository annonceRepository;

    private final AnnonceMapper annonceMapper;

    private final AnnonceSearchRepository annonceSearchRepository;

    public AnnonceServiceImpl(AnnonceRepository annonceRepository, AnnonceMapper annonceMapper, AnnonceSearchRepository annonceSearchRepository) {
        this.annonceRepository = annonceRepository;
        this.annonceMapper = annonceMapper;
        this.annonceSearchRepository = annonceSearchRepository;
    }

    /**
     * Save a annonce.
     *
     * @param annonceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AnnonceDTO save(AnnonceDTO annonceDTO) {
        log.debug("Request to save Annonce : {}", annonceDTO);
        Annonce annonce = annonceMapper.toEntity(annonceDTO);
        annonce = annonceRepository.save(annonce);
        AnnonceDTO result = annonceMapper.toDto(annonce);
        annonceSearchRepository.save(annonce);
        return result;
    }

    /**
     * Get all the annonces.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AnnonceDTO> findAll() {
        log.debug("Request to get all Annonces");
        return annonceRepository.findAll().stream()
            .map(annonceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one annonce by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AnnonceDTO> findOne(Long id) {
        log.debug("Request to get Annonce : {}", id);
        return annonceRepository.findById(id)
            .map(annonceMapper::toDto);
    }

    /**
     * Delete the annonce by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Annonce : {}", id);
        annonceRepository.deleteById(id);
        annonceSearchRepository.deleteById(id);
    }

    /**
     * Search for the annonce corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AnnonceDTO> search(String query) {
        log.debug("Request to search Annonces for query {}", query);
        return StreamSupport
            .stream(annonceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(annonceMapper::toDto)
            .collect(Collectors.toList());
    }
}
