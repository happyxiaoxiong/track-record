import {AfterViewInit, Component, HostListener, OnInit} from '@angular/core';

declare let T: any;

@Component({
  selector: 'app-real-time-position',
  template: `<div class="box box-solid">
    <div class="box-body">
      <div #map id="{{mapId}}" style="height: 400px;">Loading...</div>
    </div>
  </div>`,
  styleUrls: ['./real-time-position.component.css']
})
export class RealTimePositionComponent implements OnInit, AfterViewInit {

  private mapId = 'mapContainer';
  private map: any;
  private defaultZoom = 11;
  @HostListener('window:resize') onResize() {
    this.mapResize();
  }

  constructor() { }

  ngOnInit() {
    $(window).resize();
  }

  ngAfterViewInit(): void {
    this.mapResize();
    // 初始化地图对象
    this.map = new T.Map('mapContainer');
    this.map.centerAndZoom(new T.LngLat(116.40769, 39.89945), this.defaultZoom);
    this.getLocation();
    this.addControls();
  }

  mapResize(): void {
    let mapHeight = $(window).height() - $('header').height() - $('footer').height() - ($('section.content-header').height() || 50) - 100;
    if (mapHeight < 400) {
      mapHeight = 400;
    }
    $(`#${this.mapId}`).height(mapHeight);
  }
  getLocation(): any {
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

}
