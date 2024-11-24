export class Product {
  constructor(
    public id: number = 0,
    public categoryId: number = 0,
    public sku: string = '',
    public name: string = '',
    public description: string = '',
    public unitPrice: number = 0,
    public imageUrl: string = '',
    public active: boolean = false,
    public unitsInStock: number = 0,
    public rate: number = 0,
    public sales: number = 0,
    public dateCreated: Date = new Date()
  ) {}
}
