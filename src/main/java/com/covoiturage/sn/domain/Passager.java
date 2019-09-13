package com.covoiturage.sn.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Passager.
 */
@Entity
@Table(name = "passager")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "passager")
public class Passager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "fumeur")
    private Boolean fumeur;

    @Column(name = "accept_music")
    private Boolean acceptMusic;

    @Column(name = "accept_radio")
    private Boolean acceptRadio;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "passager")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> reservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isFumeur() {
        return fumeur;
    }

    public Passager fumeur(Boolean fumeur) {
        this.fumeur = fumeur;
        return this;
    }

    public void setFumeur(Boolean fumeur) {
        this.fumeur = fumeur;
    }

    public Boolean isAcceptMusic() {
        return acceptMusic;
    }

    public Passager acceptMusic(Boolean acceptMusic) {
        this.acceptMusic = acceptMusic;
        return this;
    }

    public void setAcceptMusic(Boolean acceptMusic) {
        this.acceptMusic = acceptMusic;
    }

    public Boolean isAcceptRadio() {
        return acceptRadio;
    }

    public Passager acceptRadio(Boolean acceptRadio) {
        this.acceptRadio = acceptRadio;
        return this;
    }

    public void setAcceptRadio(Boolean acceptRadio) {
        this.acceptRadio = acceptRadio;
    }

    public String getTelephone() {
        return telephone;
    }

    public Passager telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getUser() {
        return user;
    }

    public Passager user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public Passager reservations(Set<Reservation> reservations) {
        this.reservations = reservations;
        return this;
    }

    public Passager addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setPassager(this);
        return this;
    }

    public Passager removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setPassager(null);
        return this;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Passager)) {
            return false;
        }
        return id != null && id.equals(((Passager) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Passager{" +
            "id=" + getId() +
            ", fumeur='" + isFumeur() + "'" +
            ", acceptMusic='" + isAcceptMusic() + "'" +
            ", acceptRadio='" + isAcceptRadio() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
