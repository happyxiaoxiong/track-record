import {AfterViewInit, Component, OnDestroy} from '@angular/core';
import {AppConfig, server} from '../../../app.config';
import {UserService} from '../../../services/user.service';
import {HttpClient} from '@angular/common/http';
import {NGXLogger} from 'ngx-logger';
import {HttpRes} from '../../../models/http-res';
import {TimerObservable} from 'rxjs/observable/TimerObservable';
import 'rxjs/add/operator/takeWhile';
import {Utils} from '../../../utils';

@Component({
  selector: 'app-file-upload',
  template: `<div class="row">
    <div class="col-sm-12">
      <div id="{{uploadId}}">
        <div class="callout callout-warning">
          <h4>Sorry!</h4>
          <p>Your browser doesn't have Flash, Silverlight or HTML5 support.</p>
        </div>
      </div>
    </div>
  </div>
  <p></p>
  <div class="row">
    <div class="col-sm-12">
      <div id="track-file-state-box" class="box">
        <div class="box-header with-border">
          <h3 class="box-title">最近一周轨迹上传记录</h3>
          <div class="box-tools pull-right">
            <button type="button" class="btn btn-box-tool"><i class="fa fa-minus"></i>
            </button>
          </div>
        </div>
        <div id="dtWrapper" class="box-body">
          <table id="track-file-table" class="table table-bordered table-hover table-condensed table-striped table-responsive dataTable">
          </table>
        </div>
      </div>
    </div>
  </div>`,
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements AfterViewInit, OnDestroy {
  private uploadId = 'uploader';
  private rows = [];
  private $trackFileTable: any;
  private alive = false;
  private interval = 5000;
  private trackFileTableTimer;

  constructor(private http: HttpClient, private appConfig: AppConfig, private userSer: UserService, private log: NGXLogger) {}

  ngAfterViewInit() {
    this.initPlupLoad();
    this.initDataTables();
  }

  ngOnDestroy(): void {
    this.stopSyncDt();
  }

  initPlupLoad() {
    const me = this;
    const $plup = $(`#${me.uploadId}`);
    $plup.plupload({
      runtimes : 'html5,flash,silverlight,html4',
      url : server.apis.track.upload,
      chunk_size : '5mb',
      rename : false,
      dragdrop: true,
      headers: this.userSer.getTokenHeader(),
      // sortable: true,
      prevent_duplicates: true,
      filters : {
        // Maximum file size
        max_file_size : '1024mb',
        mime_types: [
          {title : 'KMZ files', extensions : 'kmz'}
        ]
      },
      views: {
        list: true,
        thumbs: true, // Show thumbs
        active: 'list'
      },
      init: {
        FileUploaded: function(up, file, info) {
          // Called when file has finished uploading
          if (me.appConfig.getHttpResCode().success === JSON.parse(info.response).code) {
            me.startSyncDt();
          } else {
            $plup.plupload('notify', 'error', `'${file.name}'文件上传失败`);
          }
        },
        ChunkUploaded: function(up, file, info) {
          // Called when file chunk has finished uploading
        },
        UploadComplete: function(up, files) {
          // Called when all files are either uploaded or failed
        },
        Error: function(up, err) {
          // Called when error occurs
          if (err.status === 440) {// 超时
            me.userSer.timeout();
          }
          me.log.debug('[Error] ', err);
        }
      }
    });
  }

  initDataTables() {
    $('#track-file-state-box').boxWidget({
      collapseTrigger: '.btn.btn-box-tool'
    });
    const me = this;
    me.$trackFileTable = $('#track-file-table').DataTable( {
      language: {
        url: '/assets/datatables/i18n/Chinese.json'
      },
      processing: true,
      pagingType: 'full_numbers',
      deferRender: true,
      lengthMenu: [ [5, 10, 25, -1], [5, 10, 25, '全部'] ],
      columns: [
        { data: 'filename', title: '文件名称'},
        { data: 'fileSize', title: '文件大小', 'render': function (data, type, row, meta) {
            return Utils.formatBytes(data);
          }
        },
        { data: 'md5', title: '文件md5' },
        { data: 'state', title: '上传状态', 'render': function (data, type, row, meta) {
            let bg = 'red';
            if (data === '完成') {
              bg = 'green';
            } else if (data.endsWith('...')) {
              bg = 'yellow';
            }
            return `<span class="badge bg-${bg}">${data}</span>${bg === 'yellow' ? '<i class="fa fa-spinner fa-spin "></i>' : ''}`;
          }
        },
        { data: 'comment', title: '说明'},
        { data: 'uploadTime', title: '上传时间', }
      ],
      order: [[5, 'desc']],
      columnDefs: [
        { targets: '_all', 'defaultContent': ''}
      ]
    } );
    me.startSyncDt();
  }

  startSyncDt() {
    if (!this.alive) {
      this.alive = true;
      this.trackFileTableTimer = TimerObservable.create(0, this.interval).takeWhile(() => this.alive)
        .subscribe(() => {
          this.http.get(server.apis.track.uploadState).subscribe(((res: HttpRes) => {
            if (this.appConfig.getHttpResCode().success === res.code) {
              res.data = res.data || [];
              // 验证数据有无更新
              if (this.rows.length === res.data.length || this.rows.length === 0) {
                let needSync = false, hasUpdate = false;
                for (let i = 0; i < this.rows.length; i++) {
                  if (res.data[i].state.endsWith('...')) {
                    needSync = true; // track need wait update
                  }
                  if (this.rows.length !== 0 && res.data[i].state !== this.rows[i].state) {
                    // TODO add tips, track file state update
                    hasUpdate = true;
                  }
                }
                if (!needSync) {// 不需要在同步了
                  this.stopSyncDt();
                }
                if (!hasUpdate && this.rows.length === res.data.length) {// 无更新
                  return;
                }
              }
              this.rows = res.data;
              const curPage = this.$trackFileTable.page();
              this.$trackFileTable.clear();
              this.$trackFileTable.rows.add(this.rows).draw(false).page(curPage).draw(false);
            }
          }));
        });
    }
  }

  stopSyncDt() {
    this.alive = false;
    if (this.trackFileTableTimer) {
      this.trackFileTableTimer.unsubscribe();
    }
  }
}
