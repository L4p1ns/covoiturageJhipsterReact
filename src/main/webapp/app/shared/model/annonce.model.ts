import { Moment } from 'moment';
import { IReservation } from 'app/shared/model/reservation.model';

export const enum EtatAnnonce {
  ACTIVE = 'ACTIVE',
  ANNULER = 'ANNULER'
}

export interface IAnnonce {
  id?: number;
  nom?: string;
  lieuDepart?: string;
  lieuArrivee?: string;
  dateVoyage?: Moment;
  detail?: string;
  music?: boolean;
  fumer?: boolean;
  radio?: boolean;
  dateCreation?: Moment;
  etatAnnonce?: EtatAnnonce;
  reservations?: IReservation[];
  chauffeurId?: number;
}

export const defaultValue: Readonly<IAnnonce> = {
  music: false,
  fumer: false,
  radio: false
};
