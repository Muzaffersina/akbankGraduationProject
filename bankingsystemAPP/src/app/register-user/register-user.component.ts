import { RegisterUser } from './../models/register-user-model';

import { RegisterService } from '../../services/register.service';
import { Component, OnInit } from '@angular/core';
import { NgModel } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent implements OnInit {

  user: RegisterUser = new RegisterUser();

  message : string ="";
  statusCode : string ="";
  status : boolean =false;

  constructor(private registerService : RegisterService , private router : Router) { }

  ngOnInit(): void {
  }

  userRegister(){


    this.registerService.registerUser(this.user).subscribe(
      data=>{

        alert("Created User"+"\nSurname : "+this.user.username+"\nEmail : "+this.user.email),  this.router.navigate(['login']);},

    error =>{
      this.statusCode = error.status ;
      this.message = error.error.message;
      this.status = error.error.success;
      alert("---Something went wrong... Check your information--- "+"\nSuccess : "+ this.status +"\nHTTP Status Code : "+this.statusCode+"\nMessages : "+this.message)}
    )
  }

}
