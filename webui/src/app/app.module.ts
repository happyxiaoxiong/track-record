import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { FooterComponent } from './widgets/footer/footer.component';
import { HeaderComponent } from './widgets/header/header.component';
import { LogoComponent } from './widgets/logo/logo.component';
import { BreadcrumbComponent } from './widgets/breadcrumb/breadcrumb.component';
import { MenuAsideComponent } from './widgets/menu-aside/menu-aside.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
import {HttpClientModule, HttpClientXsrfModule} from '@angular/common/http';
import {routing} from './app.routes';
import { HomeComponent } from './pages/home/home.component';
import { IcheckDirective } from './directive/icheck.directive';
import { LoadingComponent } from './widgets/loading/loading.component';
import {UserService} from './services/user.service';
import { AlertComponent } from './widgets/alert/alert.component';
import {AlertService} from './services/alert.service';
import {GlobalService} from './services/global.service';

@NgModule({
  declarations: [
    AppComponent,
    FooterComponent,
    HeaderComponent,
    LogoComponent,
    BreadcrumbComponent,
    MenuAsideComponent,
    LoginComponent,
    RegisterComponent,
    PageNotFoundComponent,
    HomeComponent,
    IcheckDirective,
    LoadingComponent,
    AlertComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'x-xsrf-token'
    }),
    routing
  ],
  providers: [
    GlobalService,
    UserService,
    AlertService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
