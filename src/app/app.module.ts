import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HomeComponent } from './components/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component';
import { OrderComponent } from './components/order/order.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import {
  HttpClientModule,
  HTTP_INTERCEPTORS
} from '@angular/common/http';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { AppComponent } from './components/app/app.component';
import { UserProfileComponent } from './components/user-profile/user.profile.component';
import { AppRoutingModule } from './app-routing.module';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';


@NgModule({
  declarations: [    
    HomeComponent, 
    HeaderComponent,
    FooterComponent, 
    DetailProductComponent, 
    OrderComponent, 
    OrderDetailComponent, 
    LoginComponent, 
    RegisterComponent, 
    AppComponent,
    UserProfileComponent
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    FormsModule, 
    HttpClientModule,
    AppRoutingModule,
    CommonModule,
    NgbModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [
    AppComponent
    // HomeComponent,
    // DetailProductComponent,
    // OrderComponent,
    // OrderConfirmComponent,
    // LoginComponent,
    // RegisterComponent
  ]
})
export class AppModule { }
