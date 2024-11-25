import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../services/admin/admin.service';
import { User } from '../../../user/models/user/user';
import { SummaryMetrics } from '../../models/summary-metrics/summary-metrics';
import { UserService } from '../../../user/services/user/user.service';
import { Page } from '../../../shared/models/page/page';
import { UserResponse } from '../../../user/models/user-reponse/user-response';
import { forkJoin, map } from 'rxjs';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.scss',
})
export class AdminPanelComponent implements OnInit {
  summaryMetrics: SummaryMetrics = new SummaryMetrics();
  users: User[] = [];
  page: Page = { size: 15, number: 1, totalElements: 0 };
  isLoading: boolean = true;

  constructor(
    private adminService: AdminService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.getSummaryMetrics();
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsers(this.page.number - 1, this.page.size).subscribe({
      next: (data: UserResponse) => {
        this.updatePageData(data.page);
        this.fetchUsersWithProductQuantities(data.content);
      },
      error: (err) => console.error('Error fetching users', err),
    });
  }

  getRole(user: User) {
    return user.roles.some((role) => role.name === 'ADMIN') ? 'Admin' : 'User';
  }

  private getSummaryMetrics() {
    this.isLoading = true;
    this.adminService
      .getSummaryMetrics()
      .subscribe((summaryMetrics: SummaryMetrics) => {
        this.summaryMetrics = summaryMetrics;
        this.isLoading = false;
      });
  }

  private updatePageData(page: Page): void {
    this.page = {
      size: page.size,
      number: page.number + 1,
      totalElements: page.totalElements,
    };
  }

  private fetchUsersWithProductQuantities(users: User[]): void {
    const userQuantityRequests = users.map((user) =>
      this.userService.getQuantityOfPurchasedProducts(user.id).pipe(
        map((quantity) => ({
          ...user,
          quantityOfPurchasedProducts: quantity,
        }))
      )
    );

    forkJoin(userQuantityRequests).subscribe({
      next: (updatedUsers) => (this.users = updatedUsers),
      error: (err) =>
        console.error('Error fetching user product quantities', err),
    });
  }
}
