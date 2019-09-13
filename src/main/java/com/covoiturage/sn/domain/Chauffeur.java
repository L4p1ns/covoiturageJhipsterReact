package com.covoiturage.sn.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Chauffeur.
 */
@Entity
@Table(name = "chauffeur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "chauffeur")
public class Chauffeur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "date_delivrance_licence", nullable = false)
    private Instant dateDelivranceLicence;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "chauffeur")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Annonce> annonces = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateDelivranceLicence() {
        return dateDelivranceLicence;
    }

    public Chauffeur dateDelivranceLicence(Instant dateDelivranceLicence) {
        this.dateDelivranceLicence = dateDelivranceLicence;
        return this;
    }

    public void setDateDelivranceLicence(Instant dateDelivranceLicence) {
        this.dateDelivranceLicence = dateDelivranceLicence;
    }

    public String getTelephone() {
        return telephone;
    }

    public Chauffeur telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getUser() {
        return user;
    }

    public Chauffeur user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Annonce> getAnnonces() {
        return annonces;
    }

    public Chauffeur annonces(Set<Annonce> annonces) {
        this.annonces = annonces;
        return this;
    }

    public Chauffeur addAnnonce(Annonce annonce) {
        this.annonces.add(annonce);
        annonce.setChauffeur(this);
        return this;
    }

    public Chauffeur removeAnnonce(Annonce annonce) {
        this.annonces.remove(annonce);
        annonce.setChauffeur(null);
        return this;
    }

    public void setAnnonces(Set<Annonce> annonces) {
        this.annonces = annonces;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chauffeur)) {
            return false;
        }
        return id != null && id.equals(((Chauffeur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Chauffeur{" +
            "id=" + getId() +
            ", dateDelivranceLicence='" + getDateDelivranceLicence() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
