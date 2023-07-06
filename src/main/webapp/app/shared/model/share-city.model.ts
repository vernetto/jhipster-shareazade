import { ShareCountry } from 'app/shared/model/enumerations/share-country.model';

export interface IShareCity {
  id?: number;
  cityName?: string | null;
  cityCountry?: ShareCountry | null;
}

export const defaultValue: Readonly<IShareCity> = {};
