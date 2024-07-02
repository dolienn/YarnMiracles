import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Product } from '../../common/product/product';
import { User } from '../../common/user/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly userUrl = 'http://localhost:8088/api/v1/users';

  constructor(private httpClient: HttpClient) {}

  addFavouriteProduct(userId: number, productId: number): Observable<void> {
    const favouritesUrl = `${this.userUrl}/${userId}/favourites/${productId}`;

    return this.httpClient.post<void>(favouritesUrl, {});
  }

  removeFavouriteProduct(userId: number, productId: number): Observable<void> {
    const favouritesUrl = `${this.userUrl}/${userId}/favourites/${productId}`;

    return this.httpClient.delete<void>(favouritesUrl);
  }

  getFavouriteProducts(userId: number): Observable<Product[]> {
    const favouritesUrl = `${this.userUrl}/${userId}/favourites`;

    return this.httpClient
      .get<GetResponseProducts>(favouritesUrl)
      .pipe(map((response) => response._embedded.products));
  }

  getFavouriteProductsPaginate(
    page: number,
    pageSize: number,
    userId: number
  ): Observable<GetResponseProducts> {
    const favouritesUrl = `${this.userUrl}/search/findFavouritesByUserId?userId=${userId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(favouritesUrl);
  }

  getFavouriteProductsPaginateOrderByUnitPriceAsc(
    page: number,
    pageSize: number,
    userId: number
  ): Observable<GetResponseProducts> {
    const favouritesUrl = `${this.userUrl}/search/findFavouritesByUserIdOrderAsc?userId=${userId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(favouritesUrl);
  }

  getFavouriteProductsPaginateOrderByUnitPriceDesc(
    page: number,
    pageSize: number,
    userId: number
  ): Observable<GetResponseProducts> {
    const favouritesUrl = `${this.userUrl}/search/findFavouritesByUserIdOrderDesc?userId=${userId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(favouritesUrl);
  }

  getFavouriteProductsPaginateOrderByRateDesc(
    page: number,
    pageSize: number,
    userId: number
  ): Observable<GetResponseProducts> {
    const favouritesUrl = `${this.userUrl}/search/findFavouritesByUserIdOrderByRateDesc?userId=${userId}&page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(favouritesUrl);
  }

  getById(id: number): Observable<User> {
    const findUserUrl = `${this.userUrl}/${id}`;

    return this.httpClient.get<User>(findUserUrl);
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
