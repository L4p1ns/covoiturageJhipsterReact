package com.covoiturage.sn.repository.search;

import com.covoiturage.sn.domain.Passager;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Passager} entity.
 */
public interface PassagerSearchRepository extends ElasticsearchRepository<Passager, Long> {
}
