import { NgModule } from '@angular/core';
import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ActivateAccountComponent } from './components/activate-account/activate-account.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { HomeComponent } from './components/home/home.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { authGuard } from './services/guard/auth.guard';
import { loggedInGuard } from './services/guard/logged-in.guard';
import { UserDetailsComponent } from './components/user-details/user-details.component';
import { StarRatingModule } from 'angular-star-rating';
import { CartDetailsComponent } from './components/cart-details/cart-details.component';
import { CheckoutComponent } from './components/checkout/checkout.component';
import { checkoutGuard } from './services/guard/checkout.guard';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { SuccessfulPurchaseComponent } from './components/successful-purchase/successful-purchase.component';
import { ContactUsComponent } from './components/contact-us/contact-us.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { AdminPanelComponent } from './components/admin-panel/admin-panel.component';
import { adminPanelGuard } from './services/guard/admin-panel.guard';
import { AddProductComponent } from './components/admin-panel/add-product/add-product.component';

const routes: Routes = [
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
    path: 'category/:id/:name',
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
