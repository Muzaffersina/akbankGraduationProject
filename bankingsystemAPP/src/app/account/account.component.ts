import { BankService } from './../../services/bank-service';
import { ChangeActivitionUser } from './../models/changeActivition-model';
import { CreateBankModel } from './../models/createBank-model';
import { TransferModel } from './../models/account-transfer-model';
import { Account } from './../models/account-model';
import { UserDetails } from '../models/user-details-model';
import { Router } from '@angular/router';
import { LogInService } from './../../services/logIn-service';
import { CreateAccount } from './../models/create-account-model';
import { AccountService } from './../../services/account-service';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';



@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {


  message : string ="";
  statusCode : string ="";
  status : boolean =false;

  isAdminPage : boolean =false;
  detailsPage : boolean = false;
  eftHavalePage : boolean = false;
  createAccountPage : boolean = false;
  changeUserActivitionPage : boolean = false;
  createBankPage : boolean = false;

  user : UserDetails = new UserDetails;
  accounts : Account[] = [];

  createAccModel : CreateAccount = new CreateAccount();
  transferModel : TransferModel = new TransferModel();
  createBankModel : CreateBankModel = new CreateBankModel();
  changeUserActivitionModel : ChangeActivitionUser = new ChangeActivitionUser();


  constructor(private accountSerivce: AccountService ,private httpClient : HttpClient,private logInService :LogInService,
    private router : Router , private bankService: BankService ) {
  }


  ngOnInit(): void {
    let jwt: string | null = localStorage.getItem("Authorization");
    if(!jwt){
      this.router.navigate(['login']);
    }
    else{
      this.logInService.getLoadUser(jwt);
    }
  }




  createAccount(){

    this.accountSerivce.createAccount(this.createAccModel).subscribe(
      (data:any)=>{
        alert("Created Account")},

    (error:any) =>{
      this.statusCode = error.status ;
      this.message = error.error.message;
      this.status = error.error.success;
      alert("---Something went wrong... Check your information--- "+"\nSuccess : "+ this.status +"\nHTTP Status Code : "+this.statusCode+"\nMessages : "+this.message)}
    )
  }

  getAccountDetailsByUserId(userId : string){

    this.accountSerivce.getAccountsByUserId(userId).subscribe(
      (data:any)=>{
        this.accounts = data.data;},

    (error:any) =>{
      this.statusCode = error.status ;
      this.message = error.error.message;
      this.status = error.error.success;
      alert("---Something went wrong... Check your information--- "+"\nSuccess : "+ this.status +"\nHTTP Status Code : "+this.statusCode+"\nMessages : "+this.message)}
    )

  }

  transfer(){

    this.accountSerivce.transfer(this.transferModel, this.transferModel.senderAccountNumber).subscribe(
      (data : any)=>{
        alert("---- Trasnfer Successfully ---")
        this.accounts = data.data;},

    (error:any) =>{
      this.statusCode = error.status ;
      this.message = error.error.message;
      this.status = error.error.success;
      alert("---Something went wrong... Check your information--- "+"\nSuccess : "+ this.status +"\nHTTP Status Code : "+this.statusCode+"\nMessages : "+this.message)}
    )

  }

  //ADMIN
   createBank(){
    this.bankService.createbank(this.createBankModel).subscribe((data :any) => {
      console.log(data)
      alert(data.message +" : "+ data.data.name);
    }),
    (error:any) =>{
      this.statusCode = error.status ;
      this.message = error.error.message;
      this.status = error.error.success;
      console.log(this.message)
      alert("---Something went wrong... Check your information--- "+"\nSuccess : "+ this.status +"\nHTTP Status Code : "+this.statusCode+"\nMessages : "+this.message)};
    }

  changeUserActivition(){
    this.bankService.changeUserActivition(this.changeUserActivitionModel,this.changeUserActivitionModel.userId).subscribe( (data:any)=>{
      alert("User is : " +this.changeUserActivitionModel.enabled);
    },

  (error:any) =>{
    this.statusCode = error.status ;
    this.message = error.error.message;
    this.status = error.error.success;
    alert("---Something went wrong... Check your information--- "+"\nSuccess : "+ this.status +"\nHTTP Status Code : "+this.statusCode+"\nMessages : "+this.message)}
  );
  }



  // ONE PAGE CONTROL
  setCreateAccountPageTrue(){
    this.changeUserActivitionPage = false;
    this.createBankPage = false;
    this.detailsPage = false;
    this.eftHavalePage = false;
    this.createAccountPage = true;
  }
  setDetailsPageTrue(){
    this.changeUserActivitionPage = false;
    this.createBankPage = false;
    this.user =  this.logInService.user;
    this.getAccountDetailsByUserId(this.user.id);
    this.eftHavalePage = false;
    this.createAccountPage = false;
    this.detailsPage = true;
  }
  setEftHavalePageTrue(){
    this.changeUserActivitionPage = false;
    this.createBankPage = false;
    this.detailsPage = false;
    this.createAccountPage = false;
    this.eftHavalePage = true;
  }

  setCreateBankPageTrue(){
    this.changeUserActivitionPage = false;
    this.detailsPage = false;
    this.createAccountPage = false;
    this.eftHavalePage = false;
    this.createBankPage = true;
  }

  setChangeUserActivitionPageTrue(){
    this.createBankPage = false;
    this.detailsPage = false;
    this.createAccountPage = false;
    this.eftHavalePage = false;
    this.changeUserActivitionPage = true;
  }

  setIsAdminPageTrue(){
    this.isAdminPage = true;
  }

}
