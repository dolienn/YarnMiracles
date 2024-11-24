export class SupportMessageDTO {
  constructor(
    public from: string = '',
    public subject: string = '',
    public text: string = ''
  ) {}
}
