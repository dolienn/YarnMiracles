import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  HttpClient,
  provideHttpClient,
  withInterceptorsFromDi,
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
  ],
  bootstrap: [AppComponent],
  imports: [BrowserModule, AppRoutingModule, FormsModule],
  providers: [
    ProductService,
    HttpClient,
    provideHttpClient(withInterceptorsFromDi()),
  ],
})
export class AppModule {}
