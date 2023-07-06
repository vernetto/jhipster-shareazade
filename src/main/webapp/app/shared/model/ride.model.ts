import dayjs from 'dayjs';
import { IUsers } from 'app/shared/model/users.model';
import { ICity } from 'app/shared/model/city.model';
import { RideType } from 'app/shared/model/enumerations/ride-type.model';

export interface IRide {
  id?: number;
  rideDateTime?: string | null;
  rideCityFrom?: string | null;
  rideCityTo?: string | null;
  rideType?: RideType | null;
  rideComments?: string | null;
  rideUser?: IUsers | null;
  rideCityFrom?: ICity | null;
  rideCityTo?: ICity | null;
}

export const defaultValue: Readonly<IRide> = {};
