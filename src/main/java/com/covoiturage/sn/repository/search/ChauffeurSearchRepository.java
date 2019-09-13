package com.covoiturage.sn.repository.search;

import com.covoiturage.sn.domain.Chauffeur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Chauffeur} entity.
 */
public interface ChauffeurSearchRepository extends ElasticsearchRepository<Chauffeur, Long> {
}
