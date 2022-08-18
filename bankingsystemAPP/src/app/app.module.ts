import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {  RouterModule, Routes } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterUserComponent } from './register-user/register-user.component';
import { LoginComponent } from './login/login.component';
import {HttpClientModule} from '@angular/common/http';
import { AccountComponent } from './account/account.component';
import { NavbarComponent } from './navbar/navbar.component';

const routes : Routes=[
  {path:"login",component:LoginComponent },
  {path:"register",component:RegisterUserComponent },
  {path:"account",component:AccountComponent },
  {path:"navbar",component:NavbarComponent },
  {path:"",redirectTo:"register" ,pathMatch :"full"}
]

@NgModule({
  declarations: [
    AppComponent,
    RegisterUserComponent,
    LoginComponent,
    AccountComponent,
    NavbarComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    RouterModule.forRoot(routes),
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
