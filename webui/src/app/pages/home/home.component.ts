import {AfterViewInit, Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {ActivatedRoute} from '@angular/router';
import {MenuService} from '../../services/menu.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  menuItems: Array<any> = [];
  skin = 'skin-blue';

  constructor(private userServ: UserService, private menuSer: MenuService, private route: ActivatedRoute) {
    const param = route.snapshot.data[0];
    this.skin = this.paramExistOrDefault(param, 'skin', 'skin-blue');
  }

  ngOnInit() {
    // default menu structure, please use the menuService to modify
    this.menuItems = [
      {
        'title': 'Home',
        'icon': 'dashboard',
        'link': '/home'
      }
    ];
    this.menuSer.getCurrent().subscribe((menuItems) => {
      this.menuItems = menuItems;
    });

    document.body.className = `${this.skin} sidebar-mini fixed`;
    $(window).resize();
  }

  private paramExistOrDefault(param: any, propName: string, defaultValue: any = true) {
    return param[propName] || defaultValue;
  }
}
