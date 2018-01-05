import { Injectable } from '@angular/core';
import {ReplaySubject} from 'rxjs/ReplaySubject';

@Injectable()
export class MenuService {
  private current: ReplaySubject<any> = new ReplaySubject<any>(1);

  public setCurrent( menu: any ) {
    this.current.next( menu );
  }

  public getCurrent() {
    return this.current;
  }

}
