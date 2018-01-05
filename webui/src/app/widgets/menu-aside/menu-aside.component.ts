import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-menu-aside',
  templateUrl: './menu-aside.component.html',
  styleUrls: ['./menu-aside.component.css']
})
export class MenuAsideComponent implements OnInit {

  private lastLi: any;
  currentUrl: string;
  @Input() menuItems: Array<any> = [];
  @Input() menuTitle = '导航';
  constructor(private router: Router) {
    this.router.events.subscribe((evt: any) => {
      if (evt instanceof NavigationEnd) {
        this.currentUrl = evt.url;
        if (this.lastLi) {
          this.lastLi.removeClass('active');
        }
        this.lastLi = $(`[href='${this.currentUrl}']`).parents('li').addClass('active');
      }
    });
  }

  ngOnInit() {
  }
}
