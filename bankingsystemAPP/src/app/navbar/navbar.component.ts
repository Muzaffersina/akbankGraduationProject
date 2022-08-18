
import { Router } from '@angular/router';
import { LogInService } from './../../services/logIn-service';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  detailsPage : boolean = false;
  eftHavalePage : boolean = false;
  createAccountPage : boolean = false;
  adminCreateAccountPage: boolean = false;
  adminChangeUserActivitionPage : boolean = false;


  constructor(private logInService : LogInService ,private router:Router) { }

  ngOnInit(): void {
  }


  logOutUser(){
    this.logInService.LogOutUser();
  }

  checkIsAdmin(){
    if(this.logInService.user.authorities.length ===1){
      return false;
    }

    return true;
  }

  @Output() AccountPageEvent = new EventEmitter();
  @Output() DetailsPageEvent = new EventEmitter();
  @Output() EftHavalePageEvent = new EventEmitter();

  @Output() AdminCreateBank = new EventEmitter();
  @Output() AdminChangeUserActivition = new EventEmitter();

  setCreateAccountPage(){
    this.createAccountPage = true;
    this.AccountPageEvent.emit();
  }
  setDetailsPage(){
    this.detailsPage = true;
    this.DetailsPageEvent.emit();
  }
  setEftHavalePage(){
    this.eftHavalePage = true;
    this.EftHavalePageEvent.emit();
  }
  setAdminCreateBankPage(){
    this.adminCreateAccountPage = true;
    this.AdminCreateBank.emit();
  }
  setAdminChangeUserActivitonPage(){
    this.adminChangeUserActivitionPage = true;
    this.AdminChangeUserActivition.emit();
  }
}
