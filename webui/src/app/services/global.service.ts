import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from "rxjs/Observable";
import {HttpRes} from "../models/http-res";
import "rxjs/add/observable/of";


const SERVER_ROOT_PATH = 'http://127.0.0.1:8080/api/v1/';

function  urlJoin(...params: string[]) {
  return SERVER_ROOT_PATH + params.join('/');
}

export const server = {
  rootPath: SERVER_ROOT_PATH,
  apis: {
    user: {
      login: urlJoin('user', 'login'),
    },
    httpResCode: urlJoin('config', 'http_res_code')
  }
};

@Injectable()
export class GlobalService {
  private httpResCode: any = null;
  constructor(private http: HttpClient) { }

  public getHttpResCode(): Promise<any> {
    if (this.httpResCode == null) {
      this.httpResCode = this.http.get<HttpRes>(server.apis.httpResCode).map((res) => res.data).toPromise();
    }
    return this.httpResCode;
  }
}
