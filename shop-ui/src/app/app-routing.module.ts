import { NgModule } from '@angular/core';
import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { ActivateAccountComponent } from './auth/activation/components/activate-account.component';
import { authGuard } from './auth/guards/auth/auth.guard';
import { StarRatingModule } from 'angular-star-rating';
import { checkoutGuard } from './checkout/guards/checkout/checkout.guard';
import { SuccessfulPurchaseComponent } from './purchase/components/successful-purchase/successful-purchase.component';
import { PageNotFoundComponent } from './shared/components/page-not-found/page-not-found.component';
import { AdminPanelComponent } from './admin/components/admin-panel/admin-panel.component';
import { AddProductComponent } from './admin/components/add-product/add-product.component';
import { EditUserComponent } from './admin/components/edit-user/edit-user.component';
import { adminPanelGuard } from './admin/guards/admin-panel/admin-panel.guard';
import { ContactUsComponent } from './support/components/contact-us/contact-us.component';
import { OrderHistoryComponent } from './order/components/order-history/order-history.component';
import { CheckoutComponent } from './checkout/components/checkout/checkout.component';
import { CartDetailsComponent } from './cart/components/cart-details/cart-details.component';
import { ProductListComponent } from './product/components/product-list/product-list.component';
import { UserDetailsComponent } from './auth/user-profile/components/user-details/user-details.component';
import { ProductDetailsComponent } from './product/components/product-details/product-details.component';
import { HomeComponent } from './home/components/home/home.component';
import { RegisterComponent } from './auth/registration/components/register/register.component';
import { loggedInGuard } from './auth/guards/logged-in/logged-in.guard';
import { LoginComponent } from './auth/login/components/login/login.component';

const routes: Routes = [
  {
    path: 'admin-panel/edit-user/:id',
    component: EditUserComponent,
    canActivate: [adminPanelGuard],
  },
  {
    path: 'admin-panel/add-product',
    component: AddProductComponent,
    canActivate: [adminPanelGuard],
  },
  {
    path: 'admin-panel',
    component: AdminPanelComponent,
    canActivate: [adminPanelGuard],
  },
  {
    path: 'contact-us',
    component: ContactUsComponent,
  },
  {
    path: 'successful-purchase',
    component: SuccessfulPurchaseComponent,
  },
  {
    path: 'order-history',
    component: OrderHistoryComponent,
    canActivate: [authGuard],
  },
  {
    path: 'checkout',
    component: CheckoutComponent,
    canActivate: [checkoutGuard],
  },
  {
    path: 'cart',
    component: CartDetailsComponent,
  },
  {
    path: 'favourites',
    component: ProductListComponent,
    data: { isFavouriteRoute: true },
    canActivate: [authGuard],
  },
  {
    path: 'profile',
    component: UserDetailsComponent,
    canActivate: [authGuard],
  },
  {
    path: 'products/:id',
    component: ProductDetailsComponent,
  },
  {
    path: 'search/:keyword',
    component: ProductListComponent,
  },
  {
    path: 'category/:id',
    component: ProductListComponent,
  },
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'products',
    component: ProductListComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [loggedInGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [loggedInGuard],
  },
  {
    path: 'activate-account',
    component: ActivateAccountComponent,
    canActivate: [loggedInGuard],
  },
  { path: '**', component: PageNotFoundComponent },
];

const routerOptions: ExtraOptions = {
  scrollPositionRestoration: 'top',
};

@NgModule({
  imports: [
    RouterModule.forRoot(routes, routerOptions),
    StarRatingModule.forRoot(),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
