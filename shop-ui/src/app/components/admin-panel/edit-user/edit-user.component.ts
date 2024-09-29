import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user/user.service';
import { User } from '../../../common/user/user';
import { NotificationService } from '../../../services/notification/notification.service';
import { AdminService } from '../../../services/admin/admin.service';
import { UserEditDTO } from '../../../common/user-edit-dto/user-edit-dto';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrl: './edit-user.component.scss',
})
export class EditUserComponent implements OnInit {
  currentUserId: number = 0;
  user: UserEditDTO = {
    id: 0,
    firstname: '',
    lastname: '',
    email: '',
    password: '',
    dateOfBirth: new Date(),
    accountLocked: false,
  };

  errorMsg: Array<string> = [];

  isLoading: boolean = true;
  editUserLoading: boolean = false;

  constructor(
    private adminService: AdminService,
    private userService: UserService,
    private notificationService: NotificationService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getUser();
  }

  editUser() {
    this.editUserLoading = true;
    this.adminService.editUser(this.user).subscribe({
      next: () => {
        this.editUserLoading = false;
        this.user = {
          id: 0,
          firstname: '',
          lastname: '',
          email: '',
          password: '',
          dateOfBirth: new Date(),
          accountLocked: false,
        };
        this.notificationService.showMessage('User edited successfully!');
        this.router.navigateByUrl('/admin-panel');
      },

      error: (err: any) => {
        this.editUserLoading = false;
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.error);
        }
      },
    });
  }

  getUser() {
    this.isLoading = true;
    const hasUserId: boolean = this.route.snapshot.paramMap.has('id');

    if (hasUserId) {
      this.currentUserId = +this.route.snapshot.paramMap.get('id')!;

      this.userService.getById(this.currentUserId).subscribe((data) => {
        this.user.id = data.id;
        this.user.firstname = data.firstname;
        this.user.lastname = data.lastname;
        this.user.email = data.email;
        this.user.dateOfBirth = data.dateOfBirth;
        this.user.accountLocked = data.accountLocked;
        this.isLoading = false;
      });
    } else {
      this.router.navigateByUrl('/admin-panel');
    }
  }
}
