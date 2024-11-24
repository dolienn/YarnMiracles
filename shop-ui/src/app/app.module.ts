import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  HTTP_INTERCEPTORS,
  HttpClient,
  HttpClientModule,
} from '@angular/common/http';
import { LoginComponent } from './auth/login/components/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './auth/registration/components/register/register.component';
import { ActivateAccountComponent } from './auth/activation/components/activate-account.component';
import { CodeInputModule } from 'angular-code-input';
import { NavigationBarComponent } from './nav/components/navigation-bar/navigation-bar.component';
import { HomeComponent } from './home/components/home/home.component';
import { ProductService } from './product/services/product/product.service';
import { ProductListComponent } from './product/components/product-list/product-list.component';
import { ContactComponent } from './home/components/contact/contact.component';
import { CopyrightComponent } from './home/components/copyright/copyright.component';
import { ProductDetailsComponent } from './product/components/product-details/product-details.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CartStatusComponent } from './cart/components/cart-status/cart-status.component';
import { HttpTokenInterceptor } from './interceptor/http-token.interceptor';
import { NotificationComponent } from './notification/components/notification/notification.component';
import { UserDetailsComponent } from './auth/user-profile/components/user-details/user-details.component';
import { FavouriteProductComponent } from './shared/components/favourite-product/favourite-product.component';
import { RatingComponent } from './shared/components/rating/rating.component';
import { FeedbackComponent } from './feedback/components/feedback/feedback.component';
import { StarRatingConfigService, StarRatingModule } from 'angular-star-rating';
import { LoaderComponent } from './shared/components/loader/loader.component';
import { AlertComponent } from './shared/components/alert/alert.component';
import { CartDetailsComponent } from './cart/components/cart-details/cart-details.component';
import { CheckoutComponent } from './checkout/components/checkout/checkout.component';
import { OrderHistoryComponent } from './order/components/order-history/order-history.component';
import { PaginationComponent } from './pagination/components/pagination/pagination.component';
import { SuccessfulPurchaseComponent } from './purchase/components/successful-purchase/successful-purchase.component';
import { ContactUsComponent } from './support/components/contact-us/contact-us.component';
import { PageNotFoundComponent } from './shared/components/page-not-found/page-not-found.component';
import { AdminPanelComponent } from './admin/components/admin-panel/admin-panel.component';
import { AddProductComponent } from './admin/components/add-product/add-product.component';
import { NavboxComponent } from './nav/components/navbox/navbox.component';
import { EditUserComponent } from './admin/components/edit-user/edit-user.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ActivateAccountComponent,
    NavigationBarComponent,
    HomeComponent,
    ProductListComponent,
    ContactComponent,
    CopyrightComponent,
    ProductDetailsComponent,
    CartStatusComponent,
    NotificationComponent,
    UserDetailsComponent,
    FavouriteProductComponent,
    RatingComponent,
    FeedbackComponent,
    LoaderComponent,
    AlertComponent,
    CartDetailsComponent,
    CheckoutComponent,
    OrderHistoryComponent,
    PaginationComponent,
    SuccessfulPurchaseComponent,
    ContactUsComponent,
    PageNotFoundComponent,
    AdminPanelComponent,
    AddProductComponent,
    NavboxComponent,
    EditUserComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CodeInputModule,
    NgbModule,
    StarRatingModule.forRoot(),
    ReactiveFormsModule,
  ],
  providers: [
    ProductService,
    HttpClient,
    { provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true },
    {
      provide: StarRatingConfigService,
    },
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
