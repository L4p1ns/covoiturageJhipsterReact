package com.covoiturage.sn.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.covoiturage.sn.domain.enumeration.StatusReservation;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "reservation")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "date_reservation", nullable = false)
    private Instant dateReservation;

    @NotNull
    @Column(name = "nombre_de_place", nullable = false)
    private Integer nombreDePlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusReservation status;

    @ManyToOne
    @JsonIgnoreProperties("reservations")
    private Annonce annonce;

    @ManyToOne
    @JsonIgnoreProperties("reservations")
    private Passager passager;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateReservation() {
        return dateReservation;
    }

    public Reservation dateReservation(Instant dateReservation) {
        this.dateReservation = dateReservation;
        return this;
    }

    public void setDateReservation(Instant dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Integer getNombreDePlace() {
        return nombreDePlace;
    }

    public Reservation nombreDePlace(Integer nombreDePlace) {
        this.nombreDePlace = nombreDePlace;
        return this;
    }

    public void setNombreDePlace(Integer nombreDePlace) {
        this.nombreDePlace = nombreDePlace;
    }

    public StatusReservation getStatus() {
        return status;
    }

    public Reservation status(StatusReservation status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatusReservation status) {
        this.status = status;
    }

    public Annonce getAnnonce() {
        return annonce;
    }

    public Reservation annonce(Annonce annonce) {
        this.annonce = annonce;
        return this;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Passager getPassager() {
        return passager;
    }

    public Reservation passager(Passager passager) {
        this.passager = passager;
        return this;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return id != null && id.equals(((Reservation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", dateReservation='" + getDateReservation() + "'" +
            ", nombreDePlace=" + getNombreDePlace() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
