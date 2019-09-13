package com.covoiturage.sn.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.covoiturage.sn.domain.enumeration.EtatAnnonce;

/**
 * A Annonce.
 */
@Entity
@Table(name = "annonce")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "annonce")
public class Annonce implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(min = 5)
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Size(min = 5)
    @Column(name = "lieu_depart", nullable = false)
    private String lieuDepart;

    @NotNull
    @Size(min = 5)
    @Column(name = "lieu_arrivee", nullable = false)
    private String lieuArrivee;

    @Column(name = "date_voyage")
    private Instant dateVoyage;

    @Column(name = "detail")
    private String detail;

    @Column(name = "music")
    private Boolean music;

    @Column(name = "fumer")
    private Boolean fumer;

    @Column(name = "radio")
    private Boolean radio;

    @Column(name = "date_creation")
    private Instant dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_annonce")
    private EtatAnnonce etatAnnonce;

    @OneToMany(mappedBy = "annonce")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> reservations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("annonces")
    private Chauffeur chauffeur;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public Annonce nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLieuDepart() {
        return lieuDepart;
    }

    public Annonce lieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
        return this;
    }

    public void setLieuDepart(String lieuDepart) {
        this.lieuDepart = lieuDepart;
    }

    public String getLieuArrivee() {
        return lieuArrivee;
    }

    public Annonce lieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
        return this;
    }

    public void setLieuArrivee(String lieuArrivee) {
        this.lieuArrivee = lieuArrivee;
    }

    public Instant getDateVoyage() {
        return dateVoyage;
    }

    public Annonce dateVoyage(Instant dateVoyage) {
        this.dateVoyage = dateVoyage;
        return this;
    }

    public void setDateVoyage(Instant dateVoyage) {
        this.dateVoyage = dateVoyage;
    }

    public String getDetail() {
        return detail;
    }

    public Annonce detail(String detail) {
        this.detail = detail;
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Boolean isMusic() {
        return music;
    }

    public Annonce music(Boolean music) {
        this.music = music;
        return this;
    }

    public void setMusic(Boolean music) {
        this.music = music;
    }

    public Boolean isFumer() {
        return fumer;
    }

    public Annonce fumer(Boolean fumer) {
        this.fumer = fumer;
        return this;
    }

    public void setFumer(Boolean fumer) {
        this.fumer = fumer;
    }

    public Boolean isRadio() {
        return radio;
    }

    public Annonce radio(Boolean radio) {
        this.radio = radio;
        return this;
    }

    public void setRadio(Boolean radio) {
        this.radio = radio;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public Annonce dateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public EtatAnnonce getEtatAnnonce() {
        return etatAnnonce;
    }

    public Annonce etatAnnonce(EtatAnnonce etatAnnonce) {
        this.etatAnnonce = etatAnnonce;
        return this;
    }

    public void setEtatAnnonce(EtatAnnonce etatAnnonce) {
        this.etatAnnonce = etatAnnonce;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public Annonce reservations(Set<Reservation> reservations) {
        this.reservations = reservations;
        return this;
    }

    public Annonce addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setAnnonce(this);
        return this;
    }

    public Annonce removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setAnnonce(null);
        return this;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Chauffeur getChauffeur() {
        return chauffeur;
    }

    public Annonce chauffeur(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
        return this;
    }

    public void setChauffeur(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Annonce)) {
            return false;
        }
        return id != null && id.equals(((Annonce) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Annonce{" +
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
            "}";
    }
}
