package com.covoiturage.sn.repository.search;

import com.covoiturage.sn.domain.Annonce;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Annonce} entity.
 */
public interface AnnonceSearchRepository extends ElasticsearchRepository<Annonce, Long> {
}
