export class RegistrationRequest {
  email: string = '';
  firstname: string = '';
  lastname: string = '';
  dateOfBirth: Date | null = new Date();
  password: string = '';
}
