import { UserRole } from 'app/shared/model/enumerations/user-role.model';
import { UserStatus } from 'app/shared/model/enumerations/user-status.model';

export interface IShareUser {
  id?: number;
  userName?: string | null;
  userEmail?: string | null;
  userRole?: UserRole | null;
  userPhone?: string | null;
  userStatus?: UserStatus | null;
}

export const defaultValue: Readonly<IShareUser> = {};
