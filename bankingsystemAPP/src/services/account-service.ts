import { Observable } from 'rxjs';
import { TransferModel } from './../app/models/account-transfer-model';
import { CreateAccount } from './../app/models/create-account-model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class AccountService {


  createAccountUrl: string = "http://localhost:8080/api/account";
  getAccountsUrl : string = "http://localhost:8080/api/accounts/";
  transferUrl : string ="http://localhost:8080/api/account/transfer/";


  constructor(private httpClient : HttpClient ) {



  }

  createAccount(account: CreateAccount): Observable<Object>{
    let token: string | null = localStorage.getItem("Authorization");
    let bearer : string = "Bearer "+token;
    const headers = new HttpHeaders({
      'Authorization': bearer,
    })
    return this.httpClient.post(this.createAccountUrl,account,{headers: headers}); // for post :  No overload matches this call.  WITH HEADERS?
  }

  transfer(transferModel: TransferModel  , senderAccountNumber : string){

    let token: string | null = localStorage.getItem("Authorization");
    let bearer : string = "Bearer "+token;
    const headers = new HttpHeaders({
      'Authorization': bearer,
    })
    return this.httpClient.put( this.transferUrl+senderAccountNumber , transferModel,{headers: headers});
  }

  getAccountsByUserId(userId : string){

    let token: string | null = localStorage.getItem("Authorization");
    let bearer : string = "Bearer "+token;
    const headers = new HttpHeaders({
      'Authorization': bearer,
    })
    return this.httpClient.get( this.getAccountsUrl+userId,{headers: headers});
  }
}


