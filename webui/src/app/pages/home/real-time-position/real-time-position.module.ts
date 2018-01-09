import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule} from '@angular/router';
import {RealTimePositionComponent} from './real-time-position.component';

const routes = [
  {
    path: '',
    component: RealTimePositionComponent
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  declarations: [
    RealTimePositionComponent
  ]
})
export class RealTimePositionModule { }
