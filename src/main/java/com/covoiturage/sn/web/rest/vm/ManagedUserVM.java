package com.covoiturage.sn.web.rest.vm;

import com.covoiturage.sn.service.dto.UserDTO;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    // Ajouter les attributs du chauffeur.
    private String telephone;
    private String dateDelivranceLicence;
    private List chauffeur;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // GETTER and SETTER


    public List getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(List chauffeur) {
        this.chauffeur = chauffeur;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDateDelivranceLicence() {
        return dateDelivranceLicence;
    }

    public void setDateDelivranceLicence(String dateDelivranceLicence) {
        this.dateDelivranceLicence = dateDelivranceLicence;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
