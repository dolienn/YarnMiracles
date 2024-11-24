import { User } from '../../../user/models/user/user';

export class Feedback {
  constructor(
    public id: number,
    public note: number,
    public comment: string,
    public createdDate: Date,
    public createdBy: User
  ) {}
}
