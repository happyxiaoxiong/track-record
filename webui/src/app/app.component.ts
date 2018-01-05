import { Component } from '@angular/core';
import {MenuService} from './services/menu.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private menuItems: any = [
    {
      title: '主页',
      icon: 'dashboard',
      link: '/home'
    },
    {
      title: '轨迹管理',
      subMenuItems: [
        {
          title: '轨迹上传',
          link: '/home/file-upload',
          icon: 'upload'
        },
        {
          title: '历史轨迹',
          link: '/home/history-track',
          icon: 'history'
        }
      ]
    },
    {
      title: '实时位置',
      icon: 'location-arrow',
      link: '/home/real-time-position'
    }
  ];

  constructor(private menuSer: MenuService) {
    this.menuSer.setCurrent(this.menuItems);
  }
}
