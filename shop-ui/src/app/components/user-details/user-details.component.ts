import { Component, OnInit } from '@angular/core';
import { TokenService } from '../../services/token/token.service';
import { User } from '../../common/user/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.scss',
})
export class UserDetailsComponent implements OnInit {
  isLoading: boolean = true;

  user: User = new User();

  constructor(private tokenService: TokenService, private router: Router) {}

  ngOnInit(): void {
    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user = data;
      this.isLoading = false;

      if (this.user.id == 0) {
        this.router.navigate(['login']);
      }
    });
  }
}
