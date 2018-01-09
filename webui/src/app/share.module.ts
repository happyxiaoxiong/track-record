import { NgModule } from '@angular/core';
import {PageNotFoundComponent} from './pages/page-not-found/page-not-found.component';
import {DateRangeComponent} from './pages/home/widgets/date-range/datetime-range.component';

@NgModule({
  imports: [
  ],
  declarations: [
    PageNotFoundComponent,
    DateRangeComponent,
  ],
  exports: [
    PageNotFoundComponent,
    DateRangeComponent,
  ]
})
export class ShareModule { }
