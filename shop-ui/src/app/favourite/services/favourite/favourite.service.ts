import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaginationAndSortParams } from '../../../pagination/models/pagination-and-sort-params/pagination-and-sort-params';
import { PaginatedProductResponse } from '../../../product/models/paginated-product-response/paginated-product-response';
import { PaginationService } from '../../../pagination/services/pagination/pagination.service';

@Injectable({
  providedIn: 'root',
})
export class FavouriteService {
  private readonly userUrl = `${environment.url}/users`;

  constructor(
    private httpClient: HttpClient,
    private paginationService: PaginationService
  ) {}

  addFavouriteProduct(userId: number, productId: number): Observable<void> {
    return this.httpClient.get<void>(
      this.buildFavouritesUrl(userId, productId)
    );
  }

  removeFavouriteProduct(userId: number, productId: number): Observable<void> {
    return this.httpClient.delete<void>(
      this.buildFavouritesUrl(userId, productId)
    );
  }

  getFavourites(
    userId: number,
    paginationAndSortParams: PaginationAndSortParams
  ): Observable<PaginatedProductResponse> {
    const favouritesUrl = `${this.userUrl}/${userId}/favourites`;
    const url = this.buildUrlWithPaginationAndSort(
      favouritesUrl,
      paginationAndSortParams
    );

    return this.httpClient.get<PaginatedProductResponse>(url);
  }

  private buildUrlWithPaginationAndSort(
    baseUrl: string,
    paginationAndSortParams: PaginationAndSortParams
  ): string {
    return this.paginationService.buildUrlWithPaginationAndSort(
      baseUrl,
      paginationAndSortParams
    );
  }

  private buildFavouritesUrl(userId: number, productId: number): string {
    return `${this.userUrl}/${userId}/favourites/${productId}`;
  }
}
