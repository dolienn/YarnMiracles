import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../user/services/user/user.service';
import { NotificationService } from '../../../notification/services/notification/notification.service';
import { AdminService } from '../../services/admin/admin.service';
import { UserRequestDTO } from '../../../user/models/user-request-dto/user-request-dto';
import { RoleService } from '../../../role/services/role/role.service';
import { User } from '../../../user/models/user/user';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrl: './edit-user.component.scss',
})
export class EditUserComponent implements OnInit {
  currentUserId: number = 0;
  user: UserRequestDTO = new UserRequestDTO();

  errorMsg: string[] = [];
  isLoading: boolean = true;
  isEditUserLoading: boolean = false;

  constructor(
    private userService: UserService,
    private notificationService: NotificationService,
    private roleService: RoleService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUser();
  }

  editUser(): void {
    if (!this.user.id) return;

    this.isEditUserLoading = true;

    this.userService.editUser(this.user).subscribe({
      next: () => this.handleEditSuccess(),
      error: (err) => this.handleEditError(err),
    });
  }

  addAdminRole(): void {
    this.modifyRole(
      'ADMIN',
      true,
      'Admin role added',
      'Failed to add admin role'
    );
  }

  removeAdminRole(): void {
    this.modifyRole(
      'ADMIN',
      false,
      'Admin role removed',
      'Failed to remove admin role'
    );
  }

  hasAdminRole(userRequest: UserRequestDTO): boolean {
    return this.roleService.hasAdminRole(this.toUser(userRequest));
  }

  private loadUser(): void {
    const userIdFromRoute = this.route.snapshot.paramMap.get('id');

    if (!userIdFromRoute) {
      this.navigateToAdminPanel();
      return;
    }

    this.currentUserId = +userIdFromRoute;
    this.fetchUserDetails(this.currentUserId);
  }

  private navigateToAdminPanel(): void {
    this.router.navigateByUrl('/admin-panel');
  }

  private fetchUserDetails(userId: number): void {
    this.isLoading = true;

    this.userService.getUserById(userId).subscribe({
      next: (data) => this.populateUserDetails(data),
      error: () => this.navigateToAdminPanel(),
    });
  }

  private populateUserDetails(data: User): void {
    Object.assign(this.user, data);
    this.isLoading = false;
  }

  private handleEditSuccess(): void {
    this.isEditUserLoading = false;
    this.resetUserForm();
    this.notificationService.showMessage('User edited successfully', true);
    this.navigateToAdminPanel();
  }

  private resetUserForm(): void {
    this.user = new UserRequestDTO();
  }

  private handleEditError(err: any): void {
    this.isEditUserLoading = false;

    if (err.error.validationErrors) {
      this.errorMsg = err.error.validationErrors;
    } else if (err.error.error) {
      this.errorMsg = [err.error.error];
    }
  }

  private modifyRole(
    role: string,
    add: boolean,
    successMessage: string,
    errorMessage: string
  ): void {
    const roleOperation = add
      ? this.userService.addRoleToUser(this.user.email, role)
      : this.userService.removeRoleFromUser(this.user.email, role);

    (roleOperation as Observable<void>).subscribe({
      next: () => {
        this.notificationService.showMessage(successMessage, true);
        this.navigateToAdminPanel();
      },
      error: () => {
        this.notificationService.showMessage(errorMessage, false);
      },
    });
  }

  private toUser(userRequest: UserRequestDTO) {
    return new User(
      userRequest.id,
      userRequest.firstname,
      userRequest.lastname,
      userRequest.email,
      userRequest.dateOfBirth,
      userRequest.accountLocked,
      [],
      0,
      userRequest.roles
    );
  }
}
