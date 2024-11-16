export class Product {
  constructor(
    public id: number,
    public sku: string,
    public name: string,
    public description: string,
    public unitPrice: number,
    public lowestPriceWithin30Days: number,
    public imageUrl: string,
    public rate: number,
    public active: boolean,
    public unitsInStock: number,
    public dateCreated: Date,
    public lastUpdated: Date
  ) {}
}
