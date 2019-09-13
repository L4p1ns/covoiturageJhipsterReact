package com.covoiturage.sn.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.covoiturage.sn.domain.enumeration.EtatAnnonce;

/**
 * A DTO for the {@link com.covoiturage.sn.domain.Annonce} entity.
 */
public class AnnonceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String nom;

    @NotNull
    @Size(min = 5)
    private String lieuDepart;

    @NotNull
    @Size(min = 5)
    private String lieuArrivee;

    private Instant dateVoyage;

    private String detail;

    private Boolean music;

    private Boolean fumer;

    private Boolean radio;

    private Instant dateCreation;

    private EtatAnnonce etatAnnonce;


    private Long chauffeurId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLieuDepart() {
        return lieuDepart;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    public String getLieuArrivee() {
        return lieuArrivee;
    }

    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
    }

    public Instant getDateVoyage() {
        return dateVoyage;
    }

    public void setDateVoyage(Instant dateVoyage) {
        this.dateVoyage = dateVoyage;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Boolean isMusic() {
        return music;
    }

    public void setMusic(Boolean music) {
        this.music = music;
    }

    public Boolean isFumer() {
        return fumer;
    }

    public void setFumer(Boolean fumer) {
        this.fumer = fumer;
    }

    public Boolean isRadio() {
        return radio;
    }

    public void setRadio(Boolean radio) {
        this.radio = radio;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public EtatAnnonce getEtatAnnonce() {
        return etatAnnonce;
    }

    public void setEtatAnnonce(EtatAnnonce etatAnnonce) {
        this.etatAnnonce = etatAnnonce;
    }

    public Long getChauffeurId() {
        return chauffeurId;
    }

    public void setChauffeurId(Long chauffeurId) {
        this.chauffeurId = chauffeurId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnonceDTO annonceDTO = (AnnonceDTO) o;
        if (annonceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), annonceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnnonceDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", lieuDepart='" + getLieuDepart() + "'" +
            ", lieuArrivee='" + getLieuArrivee() + "'" +
            ", dateVoyage='" + getDateVoyage() + "'" +
            ", detail='" + getDetail() + "'" +
            ", music='" + isMusic() + "'" +
            ", fumer='" + isFumer() + "'" +
            ", radio='" + isRadio() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", etatAnnonce='" + getEtatAnnonce() + "'" +
            ", chauffeur=" + getChauffeurId() +
            "}";
    }
}
