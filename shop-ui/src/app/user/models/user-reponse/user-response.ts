import { User } from '../user/user';

export interface UserResponse {
  content: User[];
  page: {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
  };
}
