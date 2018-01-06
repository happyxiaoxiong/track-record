import {AfterViewInit, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {server, AppConfig} from '../../../app.config';
import {NGXLogger} from 'ngx-logger';
import {HttpRes} from "../../../models/http-res";

@Component({
  selector: 'app-history-track',
  templateUrl: './history-track.component.html',
  styleUrls: ['./history-track.component.css']
})
export class HistoryTrackComponent implements OnInit, AfterViewInit {

  private $trackTable: any;
  private rows = [];

  constructor(private http: HttpClient, private appConfig: AppConfig, private log: NGXLogger) { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    this.initDataTables();
  }

  initDataTables() {
    this.$trackTable = $('#track-table').DataTable( {
      language: {
        url: '/assets/datatables/i18n/Chinese.json'
      },
      processing: true,
      lengthMenu: [ [5, 10, 25, -1], [5, 10, 25, '全部'] ],
      columns: [
        { data: 'name', title: '轨迹名称'},
        { data: 'userId', title: '上传人' },
        { data: 'startTime', title: '开始时间' },
        { data: 'endTime', title: '结束时间'},
        { data: 'length', title: '长度'},
        { data: 'maxAltitude', title: '最高海拔', },
        { data: 'keySitesList', title: '关键地点列表名称', },
        { data: 'fileSize', title: '文件md5', },
        { data: 'uploadTime', title: '上传时间', },
        { data: 'annotation', title: '注释说明', }
      ],
      columnDefs: [
        { targets: '_all', 'defaultContent': ''}
      ]
    } );
    this.search();
  }

  search() {
    this.http.get(server.apis.track.search).subscribe((res: HttpRes) => {
      this.rows = res.data;
    });
  }
}
