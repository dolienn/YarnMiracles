import { Injectable } from '@angular/core';
import { PaginationAndSortParams } from '../../models/pagination-and-sort-params/pagination-and-sort-params';

@Injectable({
  providedIn: 'root',
})
export class PaginationService {
  constructor() {}

  buildUrlWithPaginationAndSort(
    baseUrl: string,
    paginationAndSortParams: PaginationAndSortParams
  ): string {
    const { page, size, sortBy } = paginationAndSortParams;

    const urlParts: string[] = [`${baseUrl}?page=${page}&size=${size}`];

    if (sortBy) {
      urlParts.push(`sortBy=${sortBy}`);
    }

    return urlParts.join('&');
  }
}
