import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../../models/user/user';
import { environment } from '../../../../environments/environment.development';
import { UserRequestDTO } from '../../models/user-request-dto/user-request-dto';
import { UserResponse } from '../../models/user-reponse/user-response';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly userUrl = `${environment.url}/users`;

  constructor(private httpClient: HttpClient) {}

  editUser(user: UserRequestDTO): Observable<User> {
    return this.httpClient.put<User>(`${this.userUrl}`, user);
  }

  getUsers(page: number, pageSize: number): Observable<UserResponse> {
    return this.httpClient.get<UserResponse>(
      `${this.userUrl}?page=${page}&size=${pageSize}`
    );
  }

  getQuantityOfPurchasedProducts(id: number): Observable<any> {
    return this.httpClient.get<any>(
      `${this.userUrl}/${id}/quantityOfPurchasedProducts`
    );
  }

  getUserById(id: number): Observable<User> {
    const findUserUrl = `${this.userUrl}/${id}`;

    return this.httpClient.get<User>(findUserUrl);
  }

  addRoleToUser(email: string, roleName: string): Observable<UserRequestDTO> {
    const url = `${this.userUrl}/${email}/roles/${roleName}`;

    return this.httpClient.get<UserRequestDTO>(url);
  }

  removeRoleFromUser(email: string, roleName: string): Observable<void> {
    const url = `${this.userUrl}/${email}/roles/${roleName}`;

    return this.httpClient.delete<void>(url);
  }

  hasUserPurchasedProduct(
    userId: number,
    productId: number
  ): Observable<boolean> {
    const url = `${this.userUrl}/${userId}/purchased/${productId}`;

    return this.httpClient.get<boolean>(url);
  }
}
