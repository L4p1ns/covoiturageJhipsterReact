import { IReservation } from 'app/shared/model/reservation.model';

export interface IPassager {
  id?: number;
  fumeur?: boolean;
  acceptMusic?: boolean;
  acceptRadio?: boolean;
  telephone?: string;
  userEmail?: string;
  userId?: number;
  reservations?: IReservation[];
}

export const defaultValue: Readonly<IPassager> = {
  fumeur: false,
  acceptMusic: false,
  acceptRadio: false
};
