import {Component, Input, OnInit} from '@angular/core';
import {AlertService} from '../../services/alert.service';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {
  @Input() dismissible = false;
  isOpen = true;
  message: any;

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.alertService.getMessage().subscribe(message => {
      this.isOpen = true;
      this.message = message;
    });
  }

  close() {
    if (this.isOpen) {
      this.isOpen = false;
    }
  }
}
