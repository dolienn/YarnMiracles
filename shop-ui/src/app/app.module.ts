import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  HTTP_INTERCEPTORS,
  HttpClient,
  HttpClientModule,
} from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { FormsModule } from '@angular/forms';
import { RegisterComponent } from './components/register/register.component';
import { ActivateAccountComponent } from './components/activate-account/activate-account.component';
import { CodeInputModule } from 'angular-code-input';
import { NavigationBarComponent } from './components/navigation-bar/navigation-bar.component';
import { HomeComponent } from './components/home/home.component';
import { ProductService } from './services/product/product.service';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ContactComponent } from './components/contact/contact.component';
import { CopyrightComponent } from './components/copyright/copyright.component';
import { SearchComponent } from './components/search/search.component';
import { ProductDetailsComponent } from './components/product-details/product-details.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CartStatusComponent } from './components/cart-status/cart-status.component';
import { HttpTokenInterceptor } from './services/interceptor/http-token.interceptor';
import { NotificationComponent } from './components/notification/notification.component';
import { UserDetailsComponent } from './components/user-details/user-details.component';
import { FavouriteProductComponent } from './components/favourite-product/favourite-product.component';
import { RatingComponent } from './components/rating/rating.component';
import { FeedbackComponent } from './components/feedback/feedback.component';
import { StarRatingConfigService, StarRatingModule } from 'angular-star-rating';
import { CustomConfigRatingService } from './services/custom-config-rating/custom-config-rating.service';
import { LoaderComponent } from './components/loader/loader.component';
import { AlertComponent } from './components/alert/alert.component';

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
    SearchComponent,
    ProductDetailsComponent,
    CartStatusComponent,
    NotificationComponent,
    UserDetailsComponent,
    FavouriteProductComponent,
    RatingComponent,
    FeedbackComponent,
    LoaderComponent,
    AlertComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CodeInputModule,
    NgbModule,
    StarRatingModule.forRoot(),
  ],
  providers: [
    ProductService,
    HttpClient,
    { provide: HTTP_INTERCEPTORS, useClass: HttpTokenInterceptor, multi: true },
    {
      provide: StarRatingConfigService,
      useClass: CustomConfigRatingService,
    },
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}
