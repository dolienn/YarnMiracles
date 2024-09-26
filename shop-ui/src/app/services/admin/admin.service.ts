import { Injectable } from '@angular/core';
import { DashboardData } from '../../common/dashboard-data/dashboard-data';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { User } from '../../common/user/user';
import { ProductRequest } from '../../common/product-request/product-request';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private readonly adminUrl = `${environment.url}/admin`;
  private readonly usersUrl = `${environment.url}/users`;

  constructor(private httpClient: HttpClient) {}

  getDashboardData(): Observable<DashboardData> {
    const dashboardDataUrl = `${this.adminUrl}/get-dashboard-data`;

    return this.httpClient.get<DashboardData>(dashboardDataUrl);
  }

  getUsers(page: number, pageSize: number): Observable<GetResponseUsers> {
    return this.httpClient.get<GetResponseUsers>(
      `${this.usersUrl}?page=${page}&size=${pageSize}`
    );
  }

  getPurchasedProducts(id: number): Observable<any> {
    return this.httpClient.get<any>(`${this.usersUrl}/${id}/purchasedProducts`);
  }

  addProduct(productRequest: FormData): Observable<any> {
    return this.httpClient.post<any>(
      `${this.adminUrl}/add-product`,
      productRequest
    );
  }
}

interface GetResponseUsers {
  _embedded: {
    users: User[];
  };
  page: {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
  };
}
