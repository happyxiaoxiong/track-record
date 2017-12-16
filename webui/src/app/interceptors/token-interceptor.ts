import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Injectable, Injector, OnInit} from '@angular/core';
import 'rxjs/add/operator/catch';
import {Router} from '@angular/router';
import {UserService} from '../services/user.service';
import {AppConfig, server} from '../app.config';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  private router: Router;
  private  userService: UserService;
  private  appConfig: AppConfig;
  constructor(
    private injector: Injector
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.router == null) {
      this.router = this.injector.get(Router);
      this.userService = this.injector.get(UserService);
      this.appConfig = this.injector.get(AppConfig);
    }
    const headers = {};
    const token = this.appConfig.getToken();
    headers[`${token.header}`] = `${token.head} ${this.userService.getToken()}`;
    req = req.clone({
      setHeaders: headers
    });
    const me = this;
    return next.handle(req).catch(function(error: any){
      if (error.status === 440) {// 会话超时
        me.userService.logout();
        me.router.navigate(['/login']);
      } else {
        return Observable.throw(error || 'Server error');
      }
    });
  }
}
