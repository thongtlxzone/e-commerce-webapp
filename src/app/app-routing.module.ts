import { RouterModule, Routes } from "@angular/router";
import { HomeComponent } from "./components/home/home.component";
import { LoginComponent } from "./components/login/login.component";
import { NgModule } from "@angular/core";
import { RegisterComponent } from "./components/register/register.component";
import { DetailProductComponent } from "./components/detail-product/detail-product.component";
import { OrderComponent } from "./components/order/order.component";
import { OrderDetailComponent } from "./components/order-detail/order-detail.component";
import { UserProfileComponent } from "./components/user-profile/user.profile.component";
import { AuthGoardFn } from "./goards/auth.goards";

const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'login', component: LoginComponent },  
    { path: 'register', component: RegisterComponent },
    { path: 'products/:id', component: DetailProductComponent },
    { path: 'orders', component: OrderComponent, canActivate:[AuthGoardFn] },
    { path: 'user-profile', component: UserProfileComponent, canActivate:[AuthGoardFn] },    
    { path: 'order/:id', component: OrderDetailComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule{}