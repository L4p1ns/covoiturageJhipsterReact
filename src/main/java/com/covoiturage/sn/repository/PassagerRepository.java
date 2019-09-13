package com.covoiturage.sn.repository;

import com.covoiturage.sn.domain.Passager;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Passager entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PassagerRepository extends JpaRepository<Passager, Long> {

}
