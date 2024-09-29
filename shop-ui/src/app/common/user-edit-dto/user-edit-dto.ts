export class UserEditDTO {
  public id: number = 0;
  public firstname: string = '';
  public lastname: string = '';
  public email: string = '';
  public password: string = '';
  public dateOfBirth: Date = new Date();
  public accountLocked: boolean = false;
}
