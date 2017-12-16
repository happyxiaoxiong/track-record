import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import 'rxjs/add/observable/of';
import {HttpRes} from './models/http-res';

declare let $: any;
const SERVER_ROOT_PATH = '/api/v1/';

function  urlJoin(...params: string[]) {
  return SERVER_ROOT_PATH + params.join('/');
}

const no_auth = 'no_auth';

export const server = {
  rootPath: SERVER_ROOT_PATH,
  apis: {
    no_auth: {
      login: urlJoin(no_auth, 'login'),
      register: urlJoin(no_auth, 'register'),
      exist: urlJoin(no_auth, 'exist')
    },
    config: urlJoin('config')
  }
};

@Injectable()
export class AppConfig {
  private config: any = null;
  constructor() {
    const me = this;
    $.get(server.apis.config, function (res) {
      me.config = res.data;
    });
    // this.http.get<HttpRes>(server.apis.config).subscribe((res) => {
    //   this.config = res.data;
    // });
  }

  getHttpResCode(): any {
    return this.config.http_res_code;
  }

  getToken(): any {
    return this.config.token;
  }
}
