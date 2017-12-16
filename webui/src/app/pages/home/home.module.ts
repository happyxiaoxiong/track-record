import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import {homeRoutes} from './home.route';
import {RouterModule} from '@angular/router';
import {MenuAsideComponent} from '../../widgets/menu-aside/menu-aside.component';
import {BreadcrumbComponent} from '../../widgets/breadcrumb/breadcrumb.component';
import {LogoComponent} from '../../widgets/logo/logo.component';
import {HeaderComponent} from '../../widgets/header/header.component';
import {FooterComponent} from '../../widgets/footer/footer.component';

@NgModule({
  declarations: [
    HomeComponent,
    FooterComponent,
    HeaderComponent,
    LogoComponent,
    BreadcrumbComponent,
    MenuAsideComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(homeRoutes)
  ],
  exports: [RouterModule],
  bootstrap: [HomeComponent]
})
export class HomeModule { }
