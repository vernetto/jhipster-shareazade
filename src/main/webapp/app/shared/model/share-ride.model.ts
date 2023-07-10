import dayjs from 'dayjs';
import { IShareCity } from 'app/shared/model/share-city.model';
import { IUser } from 'app/shared/model/user.model';
import { RideType } from 'app/shared/model/enumerations/ride-type.model';

export interface IShareRide {
  id?: number;
  rideDateTime?: string | null;
  rideType?: RideType | null;
  rideComments?: string | null;
  rideCityFrom?: IShareCity | null;
  rideCityTo?: IShareCity | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IShareRide> = {};
