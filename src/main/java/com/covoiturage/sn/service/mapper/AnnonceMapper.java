package com.covoiturage.sn.service.mapper;

import com.covoiturage.sn.domain.*;
import com.covoiturage.sn.service.dto.AnnonceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Annonce} and its DTO {@link AnnonceDTO}.
 */
@Mapper(componentModel = "spring", uses = {ChauffeurMapper.class})
public interface AnnonceMapper extends EntityMapper<AnnonceDTO, Annonce> {

    @Mapping(source = "chauffeur.id", target = "chauffeurId")
    AnnonceDTO toDto(Annonce annonce);

    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "removeReservation", ignore = true)
    @Mapping(source = "chauffeurId", target = "chauffeur")
    Annonce toEntity(AnnonceDTO annonceDTO);

    default Annonce fromId(Long id) {
        if (id == null) {
            return null;
        }
        Annonce annonce = new Annonce();
        annonce.setId(id);
        return annonce;
    }
}
