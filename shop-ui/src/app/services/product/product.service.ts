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
    pageSize: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}?page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderByUnitPriceAsc(
    page: number,
    pageSize: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findAllByOrderByUnitPriceAsc?page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderByUnitPriceDesc(
    page: number,
    pageSize: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findAllByOrderByUnitPriceDesc?page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderByRateDesc(
    page: number,
    pageSize: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findAllByOrderByRateDesc?page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderBySalesDesc(
    page: number,
    pageSize: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findAllByOrderBySalesDesc?&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateWithCategory(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryId?id=${theCategoryId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderByUnitPriceAscWithCategory(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryIdOrderByUnitPriceAsc?id=${theCategoryId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderByUnitPriceDescWithCategory(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryIdOrderByUnitPriceDesc?id=${theCategoryId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderByRateDescWithCategory(
    page: number,
    pageSize: number,
    theCategoryId: number
  ): Observable<GetResponseProducts> {
    const searchUrl = `${this.productUrl}/search/findByCategoryIdOrderByRateDesc?id=${theCategoryId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(searchUrl);
  }

  getProductListPaginateOrderBySalesDescWithCategory(
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

  getAllProductsOrderBySales(
    page: number,
    pageSize: number
  ): Observable<GetResponseProducts> {
    const salesUrl = `${this.productUrl}/search/findAllByOrderBySalesDesc?page=${page}&size=${pageSize}`;
    return this.httpClient.get<GetResponseProducts>(salesUrl);
  }

  isNewProduct(dateCreated: any): boolean {
    const oneWeekInMilliseconds = 7 * 24 * 60 * 60 * 1000;
    const currentDate = new Date();
    const productDate = new Date(dateCreated);

    return (
      currentDate.getTime() - productDate.getTime() < oneWeekInMilliseconds
    );
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
