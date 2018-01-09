import {AfterViewInit, Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-datetime-range',
  template: `<div class="input-group date-range">
    <label class="input-group-addon" for="{{ startTimeId }}">{{ startTimeTitle }}</label>
    <input id="{{ startTimeId }}" type="text" class="form-control {{ inputClass }}" readonly>
    <label class="input-group-addon" for="{{ endTimeId }}">{{ endTimeTitle }}</label>
    <input id="{{ endTimeId }}" type="text" class="form-control {{ inputClass }}" readonly>
  </div>`,
  styleUrls: ['./datetime-range.component.css']
})
export class DateRangeComponent implements OnInit, AfterViewInit {

  @Input() startTimeId = 'startTime';
  @Input() startTimeTitle = '开始时间';
  @Input() endTimeId = 'endTime';
  @Input() endTimeTitle = '结束时间';
  @Input() inputClass = 'input-sm';
  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    $('.date-range').datepicker({
      format: 'yyyy-mm-dd',
      todayBtn: 'linked',
      clearBtn: true,
      language: 'zh-CN',
      todayHighlight: true,
      toggleActive: true,
      inputs: $('.date-range :input')
    });
  }
}
