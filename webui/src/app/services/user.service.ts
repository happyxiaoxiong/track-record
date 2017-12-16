import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {AppConfig, server} from '../app.config';
import {HttpRes} from '../models/http-res';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {isBoolean, isNullOrUndefined, isString} from "util";
import {uniqueByName} from "@angular/language-service/src/utils";

@Injectable()
export class UserService {
  private readonly userKey = 'user';
  private storage: Storage = sessionStorage;

  constructor(private http: HttpClient, private appConfig: AppConfig) { }

  login(account: string, password: string, success: Function, fail: Function): void {
    this.http.post(server.apis.no_auth.login, JSON.stringify({
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
  }

  isLogin(): boolean {
    return isString(this.getUser().token);
  }

  getToken(): string {
    return this.getUser().token || '';
  }

  private setUser(user: any) {
    this.storage.setItem(this.userKey, JSON.stringify(user));
  }

  private removeUser() {
    this.storage.removeItem(this.userKey);
  }

  private getUser(): any {
    const userStr = this.storage.getItem(this.userKey);
    if (userStr) {
      return JSON.parse(userStr);
    }
    return {};
  }
}
