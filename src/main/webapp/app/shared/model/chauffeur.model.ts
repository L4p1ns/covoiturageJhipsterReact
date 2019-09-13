import { Moment } from 'moment';
import { IAnnonce } from 'app/shared/model/annonce.model';

export interface IChauffeur {
  id?: number;
  dateDelivranceLicence?: Moment;
  telephone?: string;
  userEmail?: string;
  userId?: number;
  annonces?: IAnnonce[];
}

export const defaultValue: Readonly<IChauffeur> = {};
