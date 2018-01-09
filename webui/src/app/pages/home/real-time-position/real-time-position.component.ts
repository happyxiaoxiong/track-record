import {AfterViewInit, Component, HostListener, OnInit} from '@angular/core';
import {Imap} from '../../../maps/imap';
import {TiandituMap} from '../../../maps/tianditu-map';

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
  private map: Imap;

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
    this.map = new TiandituMap(this.mapId);
  }

  mapResize(): void {
    let mapHeight = $(window).height() - $('header').height() - $('footer').height() - ($('section.content-header').height() || 50) - 120;
    if (mapHeight < 400) {
      mapHeight = 400;
    }
    $(`#${this.mapId}`).height(mapHeight);
  }
}
