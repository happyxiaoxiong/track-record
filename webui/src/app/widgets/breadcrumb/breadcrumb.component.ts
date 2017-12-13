import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css']
})
export class BreadcrumbComponent implements OnInit {
  public display = false;
  public header = '';
  public description = '';
  public levels: Array<any> = [];

  constructor() { }

  ngOnInit() {
  }

}
