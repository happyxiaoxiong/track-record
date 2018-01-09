import {AfterViewInit, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-menu-aside',
  templateUrl: './menu-aside.component.html',
  styleUrls: ['./menu-aside.component.css']
})
export class MenuAsideComponent implements OnInit, AfterViewInit, OnDestroy {
  private lastLi: any;
  private currentUrl: string;
  private evnSubscribe;
  @Input() menuItems: Array<any> = [];
  @Input() menuTitle = '导航';
  constructor(private router: Router) {
    this.evnSubscribe = this.router.events.subscribe((evt: any) => {
      if (evt instanceof NavigationEnd) {
        this.currentUrl = evt.url;
        this.menuChange();
      }
    });
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    $('.sidebar-menu').tree();
    this.menuChange();
  }

  ngOnDestroy(): void {
    this.evnSubscribe.unsubscribe();
  }

  menuChange() {
    if (this.lastLi) {
      this.lastLi.removeClass('active');
    }
    this.lastLi = $(`[href='${this.currentUrl}']`).parents('li').addClass('active');
  }
}
