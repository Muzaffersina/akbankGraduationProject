import { RegisterUser } from './../app/models/register-user-model';


import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  userRegisterUrl: string = "http://localhost:8080/api/register";

  constructor(private httpClient : HttpClient) { }

  registerUser(user: RegisterUser): Observable<Object>{
    return this.httpClient.post(this.userRegisterUrl,user);
  }
}
