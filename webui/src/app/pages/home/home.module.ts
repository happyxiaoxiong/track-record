import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {homeRoutes} from './home.route';
import {MenuAsideComponent} from '../../widgets/menu-aside/menu-aside.component';
import {BreadcrumbComponent} from '../../widgets/breadcrumb/breadcrumb.component';
import {LogoComponent} from '../../widgets/logo/logo.component';
import {HeaderComponent} from '../../widgets/header/header.component';
import {FooterComponent} from '../../widgets/footer/footer.component';
import { IndexComponent } from './index/index.component';
import { DateRangeComponent } from './widgets/date-range/datetime-range.component';
import {HomeComponent} from './home.component';
import {ShareModule} from "../../share.module";

@NgModule({
  declarations: [
    HomeComponent,
    FooterComponent,
    HeaderComponent,
    LogoComponent,
    BreadcrumbComponent,
    MenuAsideComponent,
    IndexComponent,
  ],
  imports: [
    CommonModule,
    ShareModule,
    homeRoutes
  ]
})
export class HomeModule { }
