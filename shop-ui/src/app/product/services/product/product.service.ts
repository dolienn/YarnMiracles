import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../../../environments/environment.development';
import { Product } from '../../models/product/product';
import { ProductCategory } from '../../models/product-category/product-category';
import { PaginationAndSortParams } from '../../../pagination/models/pagination-and-sort-params/pagination-and-sort-params';
import { PaginatedProductResponse } from '../../models/paginated-product-response/paginated-product-response';
import { PaginationService } from '../../../pagination/services/pagination/pagination.service';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly productUrl = `${environment.url}/products`;
  private readonly categoryUrl = `${environment.url}/product-categories`;

  constructor(
    private httpClient: HttpClient,
    private paginationService: PaginationService
  ) {}

  getProductById(id: number): Observable<Product> {
    const url = `${this.productUrl}/${id}`;

    return this.httpClient.get<Product>(url);
  }

  getAllProducts(
    paginationAndSortParams: PaginationAndSortParams
  ): Observable<PaginatedProductResponse> {
    const url = this.paginationService.buildUrlWithPaginationAndSort(
      this.productUrl,
      paginationAndSortParams
    );

    return this.httpClient.get<PaginatedProductResponse>(url);
  }

  getProductsByCategoryId(
    categoryId: number,
    paginationAndSortParams: PaginationAndSortParams
  ): Observable<PaginatedProductResponse> {
    const baseUrl = `${this.productUrl}/category/${categoryId}`;
    const url = this.paginationService.buildUrlWithPaginationAndSort(
      baseUrl,
      paginationAndSortParams
    );

    return this.httpClient.get<PaginatedProductResponse>(url);
  }

  getProductsByKeyword(
    keyword: string,
    paginationAndSortParams: PaginationAndSortParams
  ): Observable<PaginatedProductResponse> {
    const baseUrl = `${this.productUrl}/search/${keyword}`;
    const url = this.paginationService.buildUrlWithPaginationAndSort(
      baseUrl,
      paginationAndSortParams
    );
    return this.httpClient.get<PaginatedProductResponse>(url);
  }

  addProduct(productRequest: FormData): Observable<Product> {
    return this.httpClient.post<Product>(this.productUrl, productRequest);
  }

  getProductCategories(): Observable<ProductCategory[]> {
    return this.httpClient.get<ProductCategory[]>(this.categoryUrl);
  }

  isProductNew(dateCreated: Date): boolean {
    const oneWeekInMilliseconds = 7 * 24 * 60 * 60 * 1000;
    const currentDate = new Date();
    const productDate = new Date(dateCreated);

    return (
      currentDate.getTime() - productDate.getTime() < oneWeekInMilliseconds
    );
  }
}
