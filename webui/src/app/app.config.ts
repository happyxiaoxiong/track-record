import {Injectable} from '@angular/core';
import 'rxjs/add/observable/of';
import {HttpClient} from '@angular/common/http';

const SERVER_ROOT_PATH = '/api/v1/';

function  urlJoin(...params: string[]) {
  return SERVER_ROOT_PATH + params.join('/');
}

const noAuth = 'no_auth';
const track = 'track';

export const server = {
  rootPath: SERVER_ROOT_PATH,
  apis: {
    noAuth: {
      login: urlJoin(noAuth, 'login'),
      register: urlJoin(noAuth, 'register'),
      exist: urlJoin(noAuth, 'exist')
    },
    config: urlJoin('config'),
    track: {
      upload: urlJoin(track, 'file/upload'),
      uploadState: urlJoin(track, 'file/upload/state'),
      search: urlJoin(track, 'search')
    }
  }
};

@Injectable()
export class AppConfig {
  private config: any = null;
  constructor(private http: HttpClient) {
    const me = this;
    $.ajax({
      type : 'get',
      url : server.apis.config,
      async : false,
      success : function(data){
        me.config = data.data;
      }
    });
    // this.http.get<HttpRes>(server.apis.config).subscribe((res) => {
    //   this.config = res.data;
    // });
  }

  getHttpResCode(): any {
    return this.config.http_res_code;
  }

  getTokenConfig(): any {
    return this.config.token;
  }
}
