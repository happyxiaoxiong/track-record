import {ClickEvent, Imap, MapEvent} from './imap';
import * as util from 'util';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';

declare let T: any;

export class TiandituMap implements Imap {

  private map: any;
  private defaultZoom = 11;
  private mapEventSubject = new Subject<MapEvent>();

  constructor(private mapId) {
    this.map = new T.Map(`${mapId}`);
    this.map.centerAndZoom(new T.LngLat(116.40769, 39.89945), this.defaultZoom);
    this.registerEvent();
    this.getLocation();
    this.addControls();
  }

  private getLocation(): any {
    // ip城市定位
    const me = this;
    const lc = new T.LocalCity();
    lc.location(function (e) {
      me.map.centerAndZoom(e.lnglat, me.defaultZoom);
    });
  }

  private addControls() {
    // 创建缩放平移控件对象
    this.map.addControl(new T.Control.Zoom());
    // 创建比例尺控件对象
    this.map.addControl(new T.Control.Scale());
  }

  private registerEvent() {
    const me = this;
    this.map.addEventListener('click', function (env) {
      me.mapEventSubject.next(new ClickEvent(env.lnglat.getLng(), env.lnglat.getLat()));
    });
  }

  show(overlay: any): void {
    if (util.isArray(overlay)) {
      overlay.forEach(function (ele) {
        ele.show();
      });
    } else {
      overlay.show();
    }
  }

  hide(overlay: any): void {
    if (util.isArray(overlay)) {
      overlay.forEach(function (ele) {
        ele.hide();
      });
    } else {
      overlay.hide();
    }
  }

  remove(overlay: any): void {
    if (util.isArray(overlay)) {
      overlay.forEach(function (ele) {
        this.map.removeOverlay(ele);
      });
    } else {
      this.map.removeOverlay(overlay);
    }
  }

  add(overlay: any): void {
    if (util.isArray(overlay)) {
      overlay.forEach(function (ele) {
        this.map.addOverlay(ele);
      });
    } else {
      this.map.addOverlay(overlay);
    }
  }

  clearOverLays() {
    this.map.clearOverLays();
  }

  events(): Observable<MapEvent> {
    return this.mapEventSubject.asObservable();
  }
}
