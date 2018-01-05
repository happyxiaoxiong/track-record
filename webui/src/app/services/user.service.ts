import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {AppConfig, server} from '../app.config';
import {HttpRes} from '../models/http-res';
import {isString} from 'util';
import {Router} from '@angular/router';

@Injectable()
export class UserService {
  private hasLogin = false;
  private readonly userKey = 'user';
  private storage: Storage = sessionStorage;

  constructor(private http: HttpClient, private appConfig: AppConfig, private router: Router) { }

  login(account: string, password: string, success: Function, fail: Function): void {
    this.http.post(server.apis.noAuth.login, JSON.stringify({
      account: account,
      password: password}))
      .subscribe((res: HttpRes) => {
        if (this.appConfig.getHttpResCode().success === res .code) {
          const user = res.data.user;
          user.token = res.data.token;
          this.setUser(user);
          success();
        } else {
          fail();
        }
    });
  }

  logout() {
    this.removeUser();
    this.router.navigate(['login']);
  }

  timeout() {
    this.logout();
  }

  isLogin(): boolean {
    return this.hasLogin || isString(this.getUser().token);
  }

  getTokenHeader(): any {
    const header = {};
    const token = this.appConfig.getTokenConfig();
    header[`${token.header}`] = `${token.head}${this.getToken()}`;
    return header;
  }

  getUsername(): string {
    return this.getUser().name || '';
  }
  private setUser(user: any) {
    this.storage.setItem(this.userKey, JSON.stringify(user));
    this.hasLogin = true;
  }

  private removeUser() {
    this.storage.removeItem(this.userKey);
    this.hasLogin = false;
  }

  private getUser(): any {
    const userStr = this.storage.getItem(this.userKey);
    if (userStr) {
      return JSON.parse(userStr);
    }
    return {};
  }

  private getToken(): string {
    return this.getUser().token || '';
  }
}
