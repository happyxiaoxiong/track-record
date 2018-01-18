import {AfterViewInit, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {server, AppConfig} from '../../../app.config';
import {NGXLogger} from 'ngx-logger';
import {HistoryTrackService} from './history-track.service';

@Component({
  selector: 'app-history-track',
  templateUrl: './history-track.component.html',
  styleUrls: ['./history-track.component.css']
})
export class HistoryTrackComponent implements OnInit, AfterViewInit {

  private trackCount = 0;

  constructor(private htService: HistoryTrackService, private http: HttpClient, private appConfig: AppConfig, private log: NGXLogger) {
    htService.getAnimatedSubject().subscribe((trackCount) => {
      this.trackCount = trackCount;
    });
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    // $('.nav.nav-tabs').tab();
  }

  clearTrackMap(event: any) {
    this.htService.clear();
    event.stopPropagation();
    return false;
  }
}
