import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {HistoryTrackComponent} from './history-track.component';
import {RouterModule} from '@angular/router';
import {ShareModule} from '../../../share.module';
import { ListComponent } from './list/list.component';
import { MapComponent } from './map/map.component';
import {HistoryTrackService} from './history-track.service';

const routes = [
  {
    path: '',
    component: HistoryTrackComponent,
    children: [
      {
        path: '',
        component: ListComponent
      },
      {
        path: 'list',
        component: ListComponent
      },
      {
        path: 'map',
        component: MapComponent
      }
    ]
  }
];

@NgModule({
  imports: [
    CommonModule,
    ShareModule,
    RouterModule.forChild(routes)
  ],
  declarations: [
    HistoryTrackComponent,
    ListComponent,
    MapComponent
  ],
  providers: [
    HistoryTrackService
  ]
})
export class HistoryTrackModule { }
