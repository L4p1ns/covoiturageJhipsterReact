package com.covoiturage.sn.service.mapper;

import com.covoiturage.sn.domain.*;
import com.covoiturage.sn.service.dto.ChauffeurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Chauffeur} and its DTO {@link ChauffeurDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChauffeurMapper extends EntityMapper<ChauffeurDTO, Chauffeur> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    ChauffeurDTO toDto(Chauffeur chauffeur);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "annonces", ignore = true)
    @Mapping(target = "removeAnnonce", ignore = true)
    Chauffeur toEntity(ChauffeurDTO chauffeurDTO);

    default Chauffeur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Chauffeur chauffeur = new Chauffeur();
        chauffeur.setId(id);
        return chauffeur;
    }
}
