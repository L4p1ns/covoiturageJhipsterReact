package com.covoiturage.sn.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.covoiturage.sn.domain.Passager} entity.
 */
public class PassagerDTO implements Serializable {

    private Long id;

    private Boolean fumeur;

    private Boolean acceptMusic;

    private Boolean acceptRadio;

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

    public Boolean isFumeur() {
        return fumeur;
    }

    public void setFumeur(Boolean fumeur) {
        this.fumeur = fumeur;
    }

    public Boolean isAcceptMusic() {
        return acceptMusic;
    }

    public void setAcceptMusic(Boolean acceptMusic) {
        this.acceptMusic = acceptMusic;
    }

    public Boolean isAcceptRadio() {
        return acceptRadio;
    }

    public void setAcceptRadio(Boolean acceptRadio) {
        this.acceptRadio = acceptRadio;
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

        PassagerDTO passagerDTO = (PassagerDTO) o;
        if (passagerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), passagerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PassagerDTO{" +
            "id=" + getId() +
            ", fumeur='" + isFumeur() + "'" +
            ", acceptMusic='" + isAcceptMusic() + "'" +
            ", acceptRadio='" + isAcceptRadio() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserEmail() + "'" +
            "}";
    }
}
