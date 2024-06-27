import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../services/token.service';
import { User } from '../../common/user/user';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.scss',
})
export class UserDetailsComponent implements OnInit {
  user: User = new User();

  constructor(private tokenService: TokenService) {}

  ngOnInit(): void {
    this.tokenService.getUserInfo()?.subscribe((data) => {
      console.log(data.firstname);
      this.user.id = data.id;
      this.user.firstname = data.firstname;
      this.user.lastname = data.lastname;
      this.user.email = data.email;
    });
  }
}
