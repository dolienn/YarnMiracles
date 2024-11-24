export class FeedbackRequest {
  constructor(
    public note: number = 0,
    public comment: string = '',
    public productId: number = 0
  ) {}
}
