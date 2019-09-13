package com.covoiturage.sn.service.mapper;

import com.covoiturage.sn.domain.*;
import com.covoiturage.sn.service.dto.ReservationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring", uses = {AnnonceMapper.class, PassagerMapper.class})
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {

    @Mapping(source = "annonce.id", target = "annonceId")
    @Mapping(source = "annonce.nom", target = "annonceNom")
    @Mapping(source = "passager.id", target = "passagerId")
    ReservationDTO toDto(Reservation reservation);

    @Mapping(source = "annonceId", target = "annonce")
    @Mapping(source = "passagerId", target = "passager")
    Reservation toEntity(ReservationDTO reservationDTO);

    default Reservation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(id);
        return reservation;
    }
}
