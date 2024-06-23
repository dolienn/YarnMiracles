import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Product } from '../../common/product/product';
import { ProductCategory } from '../../common/product-category/product-category';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly productUrl = 'http://localhost:8088/api/v1/products';

  private readonly categoryUrl =
    'http://localhost:8088/api/v1/product-category';

  constructor(private httpClient: HttpClient) {}

  getProduct(id: number): Observable<Product> {
    const productUrl = `${this.productUrl}/${id}`;

    return this.httpClient.get<Product>(productUrl);
  }

  getProductListPaginate(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryId?id=${theCategoryId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductList(theCategoryId: number): Observable<Product[]> {
    const searchUrl = `${this.productUrl}/search/findByCategoryId?id=${theCategoryId}`;

    return this.getProducts(searchUrl);
  }

  getProductCategories(): Observable<ProductCategory[]> {
    return this.httpClient
      .get<GetResponseProductCategory>(this.categoryUrl)
      .pipe(map((response) => response._embedded.productCategory));
  }

  searchProducts(theKeyword: string): Observable<Product[]> {
    const searchUrl = `${this.productUrl}/search/findByNameContaining?name=${theKeyword}`;

    return this.getProducts(searchUrl);
  }

  private getProducts(searchUrl: string): Observable<Product[]> {
    return this.httpClient
      .get<GetResponseProducts>(searchUrl)
      .pipe(map((response) => response._embedded.products));
  }
}

interface GetResponseProducts {
  _embedded: {
    products: Product[];
  };
  page: {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
  };
}

interface GetResponseProductCategory {
  _embedded: {
    productCategory: ProductCategory[];
  };
}
