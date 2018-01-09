import {AfterViewInit, Component, OnInit, Output, EventEmitter} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {server, AppConfig} from '../../../../app.config';
import {NGXLogger} from 'ngx-logger';
import {HttpRes} from '../../../../models/http-res';
import {Utils} from '../../../../utils';
import {HistoryTrackService, TrackMapType} from '../history-track.service';

@Component({
  selector: 'app-track-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, AfterViewInit {
  private trackTableId = 'track-table';
  private $trackTable: any;

  constructor(private htService: HistoryTrackService, private http: HttpClient, private appConfig: AppConfig, private log: NGXLogger) { }

  ngOnInit() {
    this.htService.getUpdateSubject().subscribe((data: any) => {
      const $trackCount = $('#trackCount'), $checkbox = $(`#${data.id}`);
      if (data.type === TrackMapType.CLEAR) {
        this.clear();
        this.trackCheckChangedAnnotation($trackCount, null, data.diff + this.htService.getTrackCount());
      } else if (data.type === TrackMapType.ADD) {
        this.trackCheckChangedAnnotation($checkbox, $trackCount, data.diff);
      } else if (data.type === TrackMapType.REMOVE) {
        this.trackCheckChangedAnnotation($trackCount, $checkbox, data.diff);
      }
    });
  }

  ngAfterViewInit(): void {
    this.initInputClear();
    this.initDataTables();
  }

  initInputClear() {
    $('input').addClear({
      symbolClass: 'glyphicon glyphicon-remove'
    });
  }

  initDataTables() {
    const me = this;
    const trackTableId = `#${me.trackTableId}`;
    me.$trackTable = $(trackTableId).on( 'init.dt', function () {
      $(trackTableId).css('width', '100%');
      $(`${trackTableId}_filter`).replaceWith($('.track-search').removeClass('hide'));
      $(`${trackTableId}_length`).prepend($('.track-functions').removeClass('hide'));
    }).DataTable( {
      language: {
        url: '/assets/datatables/i18n/Chinese.json'
      },
      ordering: false,
      pagingType: 'full_numbers',
      // searching: false,
      serverSide: true,
      processing: true,
      // dom: 'ft<"row"<"col-sm-3"i><"col-sm-3"l><"col-sm-6"p>>',
      ajax: function (data, callback, settings) {
        // me.log.debug(settings);
        me.http.get(server.apis.track.search, { params: me.searchParams(data) }).subscribe((res: HttpRes) => {
          callback({
            recordsTotal: res.data.total,
            recordsFiltered: res.data.total,
            data: res.data.list,
            draw: data.draw || 0
          }, settings);
        });
      },
      lengthMenu: [ [10, 25, 50], [10, 25, 50] ],
      columns: [
        {data: 'id', title: '',
          'render': function (data, type, row, meta) {
            return `<div class="checkbox" id="${data}" title="选择在地图上显示"><label><input type="checkbox"></label></div>`;
          }
        },
        { data: 'name', title: '轨迹名称'},
        { data: 'userId', title: '上传人' },
        { data: 'startTime', title: '开始时间' },
        { data: 'endTime', title: '结束时间'},
        { data: 'length', title: '长度', 'render': function (data, type, row, meta) {
          return Utils.formatMeters(data);
        }
        },
        { data: 'maxAltitude', title: '最高海拔', 'render': function (data, type, row, meta) {
          return Utils.formatMeters(data);
        }
        },
        { data: 'keySitesList', title: '关键地点列表名称', },
        { data: 'fileSize', title: '文件大小', 'render': function (data, type, row, meta) {
          return Utils.formatBytes(data);
        }
        },
        { data: 'uploadTime', title: '上传时间', },
        { data: 'annotation', title: '注释说明', }
      ],
      columnDefs: [
        { targets: '_all', 'defaultContent': ''}
      ],
      drawCallback: function () {
        $(`${trackTableId} .checkbox`).iCheck({
          checkboxClass: 'icheckbox_square-blue',
          radioClass: 'iradio_square-blue'
        }).on({ 'ifToggled': function () {
          const $checkbox = $(this).closest('.checkbox');
          const id = $checkbox.attr('id');
          this.checked ? me.htService.increase(id) : me.htService.decrease(id);
          $checkbox.attr('title', this.checked ? '取消在地图上显示' : '选择在地图上显示');
        }});
      }
    });
  }

  trackCheckChangedAnnotation($src, $dest, val) {
    const $animate = $(`<div class="animate label bg-blue">${val}</div>`);
    const srcOffset = $src.append($animate).offset();
    const destOffset = $dest ? $dest.offset() : { left: srcOffset.left, top: 0};
    const me = this;
    const scrollLeft = $(document).scrollLeft();
    const scrollTop = $(document).scrollTop();
    $animate.css({
      position: 'fixed',
      zIndex: 0x3fff,
      left: srcOffset.left - scrollLeft,
      top: srcOffset.top - scrollTop}).removeClass('hide')
      .animate({top: destOffset.top - scrollTop, left: destOffset.left - scrollLeft }, 'slow', function () {
        $animate.remove();
        me.htService.animated();
      });
  }

  search() {
    this.$trackTable.ajax.reload();
  }

  selectAll() {
    $(`#${this.trackTableId} .checkbox`).iCheck('check');
  }

  unselect() {
    $(`#${this.trackTableId} .checkbox`).iCheck('toggle');
  }

  clear() {
    $(`#${this.trackTableId} .checkbox`).iCheck('uncheck');
  }

  searchParams(data) {
    const params: HttpParams = new HttpParams()
      .append('endTime', this.addHHMMSS($('#endTime').val()))
      .append('startTime', this.addHHMMSS($('#startTime').val()))
      .append('minLatitude', '')
      .append('maxLatitude', '')
      .append('minLongitude', '')
      .append('maxLongitude', '')
      .append('name', $('#trackName').val())
      .append('pageNum', (data.start / data.length + 1).toString())
      .append('pageSize', data.length.toString())
      .append('userName', $('#userName').val());
    return params;
  }

  addHHMMSS(val: string) {
    return val && val.match(/^\d{4}(-\d\d){2}$/) ? val + ' 00:00:00' : '';
  }
}
