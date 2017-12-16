import { Component, OnInit } from '@angular/core';

declare let $: any;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    document.body.className = 'skin-blue sidebar-mini';
    $(window).resize();
  }

  protected detectIE(): boolean {
    const ua: string = window.navigator.userAgent;
    return ua.includes('MSIE ') || ua.includes('Trident/') || ua.includes('Edge/');
  }
}
