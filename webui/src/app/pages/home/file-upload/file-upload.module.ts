import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule} from '@angular/router';
import {FileUploadComponent} from "./file-upload.component";

const routes = [
  {
    path: '',
    component: FileUploadComponent
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  declarations: [
    FileUploadComponent
  ]
})
export class FileUploadModule { }
