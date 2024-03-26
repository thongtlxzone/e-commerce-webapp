import { Component, OnInit } from '@angular/core';
import { NgbPopoverConfig } from '@ng-bootstrap/ng-bootstrap';
import { UserResponse } from 'src/app/responses/user/user.response';
import { TokenService } from 'src/app/components/header/services/token.service';
import { UserService } from 'src/app/components/header/services/user.service';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{
  userResponse?: UserResponse | null;
  isPopoverOpen: boolean = false;

  togglePopover(event: Event): void{
    event.preventDefault();
    this.isPopoverOpen = !this.isPopoverOpen;
  }

  handleItemClick(index: number):void{
    // alert(`Click on ${index}`);
    if(index === 2){
      this.userService.removeUserFromLocalStorage();
      this.tokenService.removeToken;
      this.userResponse = this.userService.getUserFromLocalStorage();
    }
    this.isPopoverOpen = false;
  }

  constructor(
    private userService:UserService,
    private popoverConfig: NgbPopoverConfig,
    private tokenService: TokenService,
  ) {
    
  }
  ngOnInit() {
    this.userResponse = this.userService.getUserFromLocalStorage();
  }
}
