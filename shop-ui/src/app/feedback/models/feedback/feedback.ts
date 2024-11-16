import { User } from '../../../user/models/user/user';

export class Feedback {
  public id?: number;
  public note?: number;
  public comment?: string;
  public createdDate?: Date;
  public createdBy?: number;
  public createdByUser?: User;
}
