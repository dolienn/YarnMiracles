import { Component } from '@angular/core';
import { AdminPanelRequest } from '../../common/admin-panel-request/admin-panel-request';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.scss',
})
export class AdminPanelComponent {
  adminPanelRequest: AdminPanelRequest = {
    totalUsers: 8200,
    totalOrders: 4112,
    totalCustomerFeedback: 82,
    productsSell: 5542,
    thisMonthRevenue: 4500,
  };
}
