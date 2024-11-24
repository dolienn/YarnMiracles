export class PasswordRequestDTO {
  constructor(
    public currentPassword: string = '',
    public newPassword: string = ''
  ) {}
}
