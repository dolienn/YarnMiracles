import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Product } from '../../common/product/product';
import { ProductCategory } from '../../common/product-category/product-category';
import { environment } from '../../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly productUrl = `${environment.url}/products`;

  private readonly categoryUrl = `${environment.url}/product-category`;

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

  getProductListPaginateOrderByUnitPriceAsc(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryIdOrderByUnitPriceAsc?id=${theCategoryId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderByUnitPriceDesc(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryIdOrderByUnitPriceDesc?id=${theCategoryId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderByRateDesc(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryIdOrderByRateDesc?id=${theCategoryId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderBySalesDesc(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryIdOrderBySalesDesc?id=${theCategoryId}&page=${page}&size=${pageSize}`;

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

  searchProductsPaginate(
    page: number,
    pageSize: number,
    keyword: string
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByNameContaining?name=${keyword}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  searchProductsPaginateOrderByUnitPriceAsc(
    page: number,
    pageSize: number,
    keyword: string
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByNameContainingOrderByUnitPriceAsc?name=${keyword}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  searchProductsPaginateOrderByUnitPriceDesc(
    page: number,
    pageSize: number,
    keyword: string
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByNameContainingOrderByUnitPriceDesc?name=${keyword}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  searchProductsPaginateOrderByRateDesc(
    page: number,
    pageSize: number,
    keyword: string
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByNameContainingOrderByRateDesc?name=${keyword}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  searchProductsPaginateOrderBySalesDesc(
    page: number,
    pageSize: number,
    keyword: string
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByNameContainingOrderBySalesDesc?name=${keyword}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
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
