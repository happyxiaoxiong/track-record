import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-menu-aside',
  templateUrl: './menu-aside.component.html',
  styleUrls: ['./menu-aside.component.css']
})
export class MenuAsideComponent implements OnInit {
  public currentUrl: string;
  @Input() items: Array<any> = [];
  @Input() menu_title = '导航';
  constructor() {
    // this.router.events.subscribe((evt: any) => this.currentUrl = evt.url);
  }

  ngOnInit() {
  }

}
