import { IUser } from 'app/shared/model/user.model';
import { ShareCountry } from 'app/shared/model/enumerations/share-country.model';

export interface IShareCity {
  id?: number;
  cityName?: string | null;
  cityCountry?: ShareCountry | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IShareCity> = {};
