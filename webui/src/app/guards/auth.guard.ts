import { Injectable } from '@angular/core';
import {
  CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivateChild,
  CanLoad, Route, NavigationExtras
} from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {UserService} from '../services/user.service';

@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild, CanLoad {

  constructor(private userSer: UserService, private router: Router) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    return this.checkLogin(state.url);
  }

  canLoad(route: Route): boolean | Observable<boolean> | Promise<boolean> {
    return this.checkLogin(`/${route.path}`);
  }

  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | Observable<boolean> | Promise<boolean> {
    return this.canActivate(childRoute, state);
  }

  checkLogin(url: string): boolean {
    if (this.userSer.isLogin()) {
      return true;
    }
    this.router.navigate(['/login'], { queryParams: { redirectUrl: url }});
    return false;
  }
}
