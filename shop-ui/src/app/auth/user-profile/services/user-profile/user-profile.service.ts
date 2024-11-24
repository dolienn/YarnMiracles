import { Injectable } from '@angular/core';
import { environment } from '../../../../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { User } from '../../../../user/models/user/user';
import { Observable } from 'rxjs';
import { UserProfileDTO } from '../../models/user-profile-dto/user-profile-dto';

@Injectable({
  providedIn: 'root',
})
export class UserProfileService {
  private readonly userProfileUrl = `${environment.url}/auth/update-user-profile`;

  constructor(private httpClient: HttpClient) {}

  updateUserProfile(userProfileDTO: UserProfileDTO): Observable<User> {
    return this.httpClient.post<User>(this.userProfileUrl, userProfileDTO);
  }
}
