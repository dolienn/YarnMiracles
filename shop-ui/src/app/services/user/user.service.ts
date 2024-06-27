import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { Product } from '../../common/product/product';

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
      .pipe(map((response) => response.content));
  }

  getFavouriteProductsPaginate(
    page: number,
    pageSize: number,
    userId: number
  ): Observable<GetResponseProducts> {
    const favouritesUrl = `${this.userUrl}/${userId}/favourites?page=${page}&size=${pageSize}`;

    return this.httpClient.get<GetResponseProducts>(favouritesUrl);
  }
}

interface GetResponseProducts {
  content: Product[];
  page: {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
  };
}
