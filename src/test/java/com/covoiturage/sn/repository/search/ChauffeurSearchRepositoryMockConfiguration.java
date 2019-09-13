package com.covoiturage.sn.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ChauffeurSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ChauffeurSearchRepositoryMockConfiguration {

    @MockBean
    private ChauffeurSearchRepository mockChauffeurSearchRepository;

}
