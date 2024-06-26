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

const routes: Routes = [
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
    canActivate: [authGuard],
  },
  {
    path: 'products',
    component: ProductListComponent,
    canActivate: [authGuard],
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
];

const routerOptions: ExtraOptions = {
  scrollPositionRestoration: 'top',
};

@NgModule({
  imports: [RouterModule.forRoot(routes, routerOptions)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
