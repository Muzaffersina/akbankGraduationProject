import { ChangeActivitionUser } from '../app/models/changeActivition-model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { CreateBankModel } from 'src/app/models/createBank-model';

@Injectable({
  providedIn: 'root'
})
export class BankService {

  createBankUrl : string = "http://localhost:8080/api/bank";
  changeUserActivitionUserUrl : string = "http://localhost:8080/api/user/";

  constructor(private httpClient : HttpClient) { }


  createbank(bank: CreateBankModel): Observable<Object>{

    let token: string | null = localStorage.getItem("Authorization");
    let bearer : string = "Bearer "+token;
    const headers = new HttpHeaders({
      'Authorization': bearer,
    })
    return this.httpClient.post(this.createBankUrl,bank,{headers: headers}); // for post :  No overload matches this call.  WITH HEADERS?
  }

  changeUserActivition(userActivitionModel:ChangeActivitionUser , userId: string):Observable<Object>{
    let token: string | null = localStorage.getItem("Authorization");
    let bearer : string = "Bearer "+token;
    const headers = new HttpHeaders({
      'Authorization': bearer,
    })
    return this.httpClient.patch(this.changeUserActivitionUserUrl+userId ,userActivitionModel,{headers: headers});
  }
}
