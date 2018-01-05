import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {PageNotFoundComponent} from './pages/page-not-found/page-not-found.component';
import {RouterModule} from '@angular/router';

@NgModule({
  imports: [
    RouterModule,
    CommonModule
  ],
  declarations: [
    PageNotFoundComponent
  ],
  exports: [
    PageNotFoundComponent
  ]
})
export class ShareModule { }
