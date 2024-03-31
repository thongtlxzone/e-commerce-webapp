import { Component, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, NgForm, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { RegisterDTO } from '../../dtos/user/register.dto';
import { TokenService } from 'src/app/services/token.service';
import { UserResponse } from 'src/app/responses/user/user.response';
import { ValidationError } from 'class-validator';
import { UpdateUserDTO } from 'src/app/dtos/user/update.user.dto';

@Component({
  selector: 'user-profile',
  templateUrl: './user.profile.component.html',
  styleUrls: ['./user.profile.component.scss']
})
export class UserProfileComponent implements OnInit{
  userProfileForm: FormGroup;
  userResponse?: UserResponse;
  token: string = this.tokenService.getToken() ?? '';
  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService
  ){
    this.userProfileForm = formBuilder.group({
      fullname: [''],
      address: ['', Validators.minLength(3)],
      password: ['', Validators.minLength(3)],
      retype_password: ['', Validators.minLength(3)],
      date_of_birth: [Date.now()]
    },
    {
      validators: this.passwordMatchValidators
    })
  }
  ngOnInit(): void {
      debugger
      this.userService.getUserDetail(token).subscribe({
        next: (response: any) => {
          debugger
          this.userResponse = {
            ...response,
            date_of_birth: new Date(response.date_of_birth)
          };
          this.userProfileForm.patchValue({
            fullname: this.userResponse?.fullname ?? '',
            address: this.userResponse?.address ?? '',
            date_of_birth: this.userResponse?.date_of_birth.toISOString().substring(0,10),
          })
          this.userService.saveUserResponseToLocalStorage(this.userResponse);
        },
        complete: () => {
          debugger
        },
        error: (error: any) => {
          debugger
          alert(error.error.message);
        }
      })
  }
  passwordMatchValidators(): ValidatorFn {
    return (formGroup: AbstractControl): ValidationErrors | null => {
      const password = formGroup.get('password')?.value;
      const retypePassword = formGroup.get('retype_password')?.value;
      if(password!==retypePassword){
        return { passwordMismatch: true }
      }
      return null;
    }
  }
  save():void{
    debugger
    if(this.userProfileForm.valid){
      const updateUserDTO: UpdateUserDTO = {
        fullname: this.userProfileForm.get('fullname')?.value,
        address: this.userProfileForm.get('address')?.value,
        password: this.userProfileForm.get('password')?.value,
        retype_password: this.userProfileForm.get('retype_password')?.value,
        date_of_birth: this.userProfileForm.get('date_of_birth')?.value
      };
      this.userService.updateUserDetail(this.token, updateUserDTO).subscribe({
        next: (response: any) => {
          this.userService.removeUserFromLocalStorage();
          this.tokenService.removeToken();
          this.router.navigate(['/login']);
        },
        error: (error: any) => {
          alert(error.error.message);
        }
      })
    }else{
      if(this.userProfileForm.hasError('passwordMismatch')){
        alert('Mật khẩu chưa trùng khớp')
      }
    }
  }
}