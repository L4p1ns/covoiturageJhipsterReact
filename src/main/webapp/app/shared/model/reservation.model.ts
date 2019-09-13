import { Moment } from 'moment';

export const enum StatusReservation {
  ANVOYER = 'ANVOYER',
  ACCEPTER = 'ACCEPTER',
  CONFIRMER = 'CONFIRMER',
  REFUSER = 'REFUSER'
}

export interface IReservation {
  id?: number;
  dateReservation?: Moment;
  nombreDePlace?: number;
  status?: StatusReservation;
  annonceNom?: string;
  annonceId?: number;
  passagerId?: number;
}

export const defaultValue: Readonly<IReservation> = {};
