import { Component, OnInit } from '@angular/core';
import { DashboardData } from '../../models/dashboard-data/dashboard-data';
import { AdminService } from '../../services/admin/admin.service';
import { User } from '../../../user/models/user/user';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.scss',
})
export class AdminPanelComponent implements OnInit {
  dashboardData: DashboardData = {
    totalUsers: 0,
    totalOrders: 0,
    totalCustomerFeedback: 0,
    productsSell: 0,
    thisMonthRevenue: 0,
  };

  users: User[] = [];

  pageNumber: number = 1;
  pageSize: number = 20;
  totalElements: number = 0;

  isLoading: boolean = true;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.getDashboardData();
    this.getUsers();
  }

  getDashboardData() {
    this.isLoading = true;
    this.adminService
      .getDashboardData()
      .subscribe((dashboardData: DashboardData) => {
        this.dashboardData = dashboardData;
        this.isLoading = false;
      });
  }

  getUsers() {
    this.adminService
      .getUsers(this.pageNumber - 1, this.pageSize)
      .subscribe((data: any) => {
        this.users = data._embedded.users;
        this.pageNumber = data.page.number + 1;
        this.pageSize = data.page.size;
        this.totalElements = data.page.totalElements;
        this.users.forEach((user) => {
          this.adminService.getPurchasedProducts(user.id).subscribe((data) => {
            user.purchasedProducts = data._embedded.products;
          });
        });
      });
  }

  getRole(user: User) {
    return user.roles.some((role) => role.name === 'ADMIN') ? 'Admin' : 'User';
  }

  getPurchasedProductsSize(user: User) {
    return Array.isArray(user.purchasedProducts)
      ? user.purchasedProducts.length
      : 0;
  }
}
