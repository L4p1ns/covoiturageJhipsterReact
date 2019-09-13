package com.covoiturage.sn.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.covoiturage.sn.domain.enumeration.StatusReservation;

/**
 * A DTO for the {@link com.covoiturage.sn.domain.Reservation} entity.
 */
public class ReservationDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateReservation;

    @NotNull
    private Integer nombreDePlace;

    private StatusReservation status;


    private Long annonceId;

    private String annonceNom;

    private Long passagerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Instant dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Integer getNombreDePlace() {
        return nombreDePlace;
    }

    public void setNombreDePlace(Integer nombreDePlace) {
        this.nombreDePlace = nombreDePlace;
    }

    public StatusReservation getStatus() {
        return status;
    }

    public void setStatus(StatusReservation status) {
        this.status = status;
    }

    public Long getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(Long annonceId) {
        this.annonceId = annonceId;
    }

    public String getAnnonceNom() {
        return annonceNom;
    }

    public void setAnnonceNom(String annonceNom) {
        this.annonceNom = annonceNom;
    }

    public Long getPassagerId() {
        return passagerId;
    }

    public void setPassagerId(Long passagerId) {
        this.passagerId = passagerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReservationDTO reservationDTO = (ReservationDTO) o;
        if (reservationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
            "id=" + getId() +
            ", dateReservation='" + getDateReservation() + "'" +
            ", nombreDePlace=" + getNombreDePlace() +
            ", status='" + getStatus() + "'" +
            ", annonce=" + getAnnonceId() +
            ", annonce='" + getAnnonceNom() + "'" +
            ", passager=" + getPassagerId() +
            "}";
    }
}
