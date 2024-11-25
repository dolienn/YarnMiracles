export class RegistrationDTO {
  constructor(
    public email: string = '',
    public firstname: string = '',
    public lastname: string = '',
    public dateOfBirth: Date | null = null,
    public password: string = ''
  ) {}
}
