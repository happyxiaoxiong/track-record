import {AfterViewInit, Component, HostListener, Input, OnDestroy, OnInit} from '@angular/core';
import {Imap} from '../../../../maps/imap';
import {TiandituMap} from '../../../../maps/tianditu-map';
import {HistoryTrackService, TrackMapType} from '../history-track.service';

@Component({
  selector: 'app-track-map',
  template: `<div #map id="{{mapId}}" style="height: 400px;">Loading...</div>`,
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, AfterViewInit, OnDestroy {

  private mapTrack = [];
  private map: Imap;
  private mapId = 'mapContainer';

  @HostListener('window:resize')
  onResize() {
    this.mapResize();
  }

  constructor(private htService: HistoryTrackService) {
    htService.getUpdateSubject().subscribe((data: any) => {
      if (data.type === TrackMapType.CLEAR) {
        this.clear();
      } else if (data.type === TrackMapType.ADD) {
        this.add(data.id);
      } else if (data.type === TrackMapType.REMOVE) {
        this.hide(data.id);
      }
    });
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    const me = this;
    // fix map not show full
    $('a[href="#track-map"]').one('shown.bs.tab', function () {
      me.mapResize();
      me.map = new TiandituMap(me.mapId);
    });
  }

  ngOnDestroy(): void {
    this.mapTrack = [];
  }

  add(id: string) {
    if (!this.show(id)) {

    }
  }
  show(id: string) {
    const overlay = this.mapTrack[id];
    if (overlay) {
      this.map.show(overlay);
      return true;
    }
    return false;
  }

  hide(id: string) {
    const overlay = this.mapTrack[id];
    if (overlay) {
      this.map.hide(overlay);
      return true;
    }
    return false;
  }

  remove(id: string) {
    const overlay = this.mapTrack[id];
    if (overlay) {
      this.map.remove(overlay);
      delete this.mapTrack[id];
      return true;
    }
    return false;
  }

  clear() {
    this.mapTrack = [];
    this.map.clearOverLays();
  }

  mapResize(): void {
    let mapHeight = $(window).height() - $('header').height() - $('footer').height() - ($('section.content-header').height() || 50) - 164;
    if (mapHeight < 400) {
      mapHeight = 400;
    }
    $(`#${this.mapId}`).height(mapHeight);
  }
}
