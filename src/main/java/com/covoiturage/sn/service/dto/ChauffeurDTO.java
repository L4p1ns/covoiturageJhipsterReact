package com.covoiturage.sn.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.covoiturage.sn.domain.Chauffeur} entity.
 */
public class ChauffeurDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateDelivranceLicence;

    @NotNull
    private String telephone;


    private Long userId;

    private String userEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDelivranceLicence() {
        return dateDelivranceLicence;
    }

    public void setDateDelivranceLicence(Instant dateDelivranceLicence) {
        this.dateDelivranceLicence = dateDelivranceLicence;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChauffeurDTO chauffeurDTO = (ChauffeurDTO) o;
        if (chauffeurDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), chauffeurDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChauffeurDTO{" +
            "id=" + getId() +
            ", dateDelivranceLicence='" + getDateDelivranceLicence() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserEmail() + "'" +
            "}";
    }
}
