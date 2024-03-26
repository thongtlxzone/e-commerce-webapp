import { Injectable, inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from "@angular/router";
import { TokenService } from "src/app/components/header/services/token.service";

@Injectable({
    providedIn: 'root'
})
export class AuthGoard {
    constructor(
        private tokenService: TokenService,
        private router: Router
    ){}

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean{
        const isTokenExpired = this.tokenService.isTokenExpired();
        const isUserIdValid = this.tokenService.getUserId()>0;
        debugger
        if(!isTokenExpired && isUserIdValid){
            return true;
        }else{
            this.router.navigate(['/login']);
            return false;
        }
    }
}

export const AuthGoardFn: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot):boolean =>{
    debugger;
    return inject(AuthGoard).canActivate(next, state);
}