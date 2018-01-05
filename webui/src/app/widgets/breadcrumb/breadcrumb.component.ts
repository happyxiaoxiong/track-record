import {Component, Input, OnInit} from '@angular/core';
import {MenuService} from '../../services/menu.service';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css']
})
export class BreadcrumbComponent implements OnInit {
  @Input() public display = true;
  public header = '';
  public description = '';
  public levels: Array<any> = [];
  private allBreadcrumb: Array<any> = [];

  constructor(private menuSer: MenuService, private router: Router) {
    menuSer.getCurrent().subscribe((menuItems: Array<any>) => {
      this.allBreadcrumb = this.getAllBreadcrumb(menuItems);
    });
    this.router.events.subscribe((evt: any) => {
      if (evt instanceof NavigationEnd) {
        this.levels = this.allBreadcrumb[evt.url] || [];
        if (this.levels.length > 0) {
          this.header = this.levels[0].title;
          this.description = this.levels[0].description;
        }
      }
    });
  }

  ngOnInit() {
  }

  getAllBreadcrumb(menuItems: Array<any>): Array<any> {
    const res = [];
    for (const menuItem of menuItems) {
      const breadcrumb = {
        link: menuItem.link,
        icon: menuItem.icon,
        title: menuItem.title,
        description: null
      };
      if (menuItem.subMenuItems) {
        const subRes = this.getAllBreadcrumb(menuItem.subMenuItems);
        for (const link in subRes) {
          res[link] = [breadcrumb].concat(subRes[link]);
        }
      } else {
        breadcrumb.description = menuItem.description || '';
        res[menuItem.link] = [breadcrumb];
      }
    }
    return res;
  }
}
