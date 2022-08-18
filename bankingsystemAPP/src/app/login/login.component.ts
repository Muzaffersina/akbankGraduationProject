import { Router } from '@angular/router';
import { LogInService } from './../../services/logIn-service';
import { LogInUser } from '../models/logIn-user-model';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  logInUser : LogInUser = new LogInUser();
  message : string ="";
  statusCode : string ="";
  status : boolean =false;
  token:string ="";

  constructor(private logInService:LogInService ,  private router : Router) { }

  ngOnInit(): void {



  }
  logIn(){
    this.logInService.LogInUser(this.logInUser).subscribe(

     (data : any)=>{
      this.router.navigate(['account']);
     },

     (error : any) =>{
       this.statusCode = error.status ;
       this.message = error.error.message;
       this.status = error.error.status;
       if(this.statusCode == "403"){
       alert("---User is Disabled---"+"\nSuccess : "+ this.status+"\nHTTP Status Code : "+ this.statusCode);
     }

       else{
       alert("---Something went wrong... Check your information---"+"\nSuccess : "+ this.status+"\nHTTP Status Code : "+this.statusCode+"\nMessages : "+this.message);
     }

     })
  }

}
