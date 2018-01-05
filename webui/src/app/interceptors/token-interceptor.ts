import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {Injectable, Injector} from '@angular/core';
import 'rxjs/add/operator/catch';
import {UserService} from '../services/user.service';
import {NGXLogger} from 'ngx-logger';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  private  userSer: UserService;
  private log: NGXLogger;

  constructor(
    private injector: Injector,
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.userSer == null) {
      this.userSer = this.injector.get(UserService);
      this.log = this.injector.get(NGXLogger);
    }
    req = req.clone({
      setHeaders: this.userSer.getTokenHeader()
    });
    const me = this;
    return next.handle(req).catch(function(error: any){
      me.log.debug('error', error.status);
      if (error.status === 440) {// 会话超时
        me.userSer.timeout();
      } else {
        return Observable.throw(error || 'Server error');
      }
    });
  }
}
