import { Component, OnInit, ViewChild } from '@angular/core';
import { LoginDTO } from '../../dtos/user/login.dto';
import { UserService } from '../header/services/user.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import {LoginResponses} from '../../responses/user/login.responses'
import { TokenService } from 'src/app/components/header/services/token.service';
import { RoleService } from 'src/app/components/header/services/role.service';
import { Role } from 'src/app/models/role';
import { UserResponse } from 'src/app/responses/user/user.response';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  @ViewChild('loginForm') loginForm!: NgForm;

  phoneNumber: string;
  password: string;

  roles: Role[] = [];//Mang Roles
  rememberMe: boolean = true;
  selectedRole: Role | undefined;
  userResponse?: UserResponse;

  constructor(
    private router: Router, 
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService
    ){
    this.phoneNumber = '111111222';
    this.password = '123';
  }

  ngOnInit(){
    debugger;
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        debugger;
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      },
      error: (error: any) => {
        debugger;
        console.error('Error gettings roles: ', error);
      }
    });
  }

  onPhoneNumberChange(){
    console.log('Phone typed: ' + this.phoneNumber);
  }
  login(){
    // const message = `phone: ${this.phone} ` +
    //                 `password: ${this.password}` +
    //                 `retypePassword: ${this.retypePassword}` +
    //                 `fullname: ${this.fullname}` +
    //                 `address: ${this.address}` +
    //                 `isAccepted: ${this.isAccepted}` +
    //                 `dateOfBirth: ${this.dateOfBirth}`;
    // alert(message);
    const loginDTO:LoginDTO = {
      "phone_number": this.phoneNumber,
      "password": this.password,  
      "role_id": this.selectedRole?.id ?? 1
    };
    this.userService.login(loginDTO ).subscribe({
      next: (response: LoginResponses) => {
        debugger;
        const {token} = response;
        if(this.rememberMe){
          this.tokenService.setToken(token);
          debugger;
          this.userService.getUserDetail(token).subscribe({
            next: (uResponse: any) => {
              debugger;
              this.userResponse = {
                ...uResponse,
                date_of_birth: new Date(uResponse.date_of_birth)
              }
              this.userService.saveUserResponseToLocalStorage(this.userResponse);
              this.router.navigate(['/']);
            },
            complete: () => {
              debugger;
            },
            error: (error: any) => {
              debugger;
              alert(error.error.message);
            }
          })
        }
      },
      complete: () => {
        debugger
      },
      error: (error: any) => {
        debugger
        //handle error
        alert(error?.error?.message)
      }
    });
  }
}
