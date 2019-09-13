package com.covoiturage.sn.service.mapper;

import com.covoiturage.sn.domain.*;
import com.covoiturage.sn.service.dto.PassagerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Passager} and its DTO {@link PassagerDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PassagerMapper extends EntityMapper<PassagerDTO, Passager> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    PassagerDTO toDto(Passager passager);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "removeReservation", ignore = true)
    Passager toEntity(PassagerDTO passagerDTO);

    default Passager fromId(Long id) {
        if (id == null) {
            return null;
        }
        Passager passager = new Passager();
        passager.setId(id);
        return passager;
    }
}
