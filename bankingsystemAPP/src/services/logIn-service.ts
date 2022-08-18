import { UserDetails } from '../app/models/user-details-model';
import { Router } from '@angular/router';
import { LogInUser } from '../app/models/logIn-user-model';

import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';



@Injectable({
  providedIn: 'root'
})
export class LogInService {

 // private _isLoggedIn$ = new BehaviorSubject<boolean>(false);
  //isLoggedIn$ = this._isLoggedIn$.asObservable();


  logInUrl: string = "http://localhost:8080/api/auth";
  userDetailsUrl : string = "http://localhost:8080/api/user";



  user : UserDetails = new UserDetails;

  constructor(private httpClient : HttpClient, private router : Router) {
    const token = localStorage.getItem('Authorization');
    //this._isLoggedIn$.next(!!token);
   }

  LogInUser(logInUser: LogInUser): Observable<Object>{
    return this.httpClient.post(this.logInUrl,logInUser).pipe(
      tap((response :any)=>{
       // this._isLoggedIn$.next(true),
        localStorage.setItem('Authorization',response.token)
      })
    )
  }
  LogOutUser(){
    localStorage.removeItem('Authorization');
   // this._isLoggedIn$.next;
    this.router.navigate(['login']);
  }

  getLoadUser(token : string){

    let bearer : string = "Bearer "+token;
    const headers = new HttpHeaders({
      'Authorization': bearer,
    })
    return this.httpClient.get( this.userDetailsUrl , {headers: headers}).subscribe(
      (data:any)=>{
      this.user = data},

      (error:any) =>{
        alert("---Something went wrong...")}
        )
  }


}
