import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import 'rxjs/add/operator/map';
import {GlobalService, server} from './global.service';
import {HttpRes} from "../models/http-res";

@Injectable()
export class UserService {

  constructor(private http: HttpClient, private globalService: GlobalService) { }

  login(account: any, password: string) {
    const body = new HttpParams()
      .set('account', account)
      .set('password', password);
    return this.http.post(server.apis.user.login, body)
      .map((res: HttpRes) => { this.globalService.getHttpResCode()
        .then((httpResCode) => {
          if (res.code === httpResCode.success) {

          }
        });
    });
  }

  logout() {

  }
}
