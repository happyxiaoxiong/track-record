import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {routing} from './app.routes';
import { IcheckDirective } from './directives/icheck.directive';
import { LoadingComponent } from './widgets/loading/loading.component';
import {UserService} from './services/user.service';
import { AlertComponent } from './widgets/alert/alert.component';
import {AlertService} from './services/alert.service';
import {TokenInterceptor} from './interceptors/token-interceptor';
import {AppConfig} from './app.config';
import {AuthGuard} from './guards/auth.guard';
import {MenuService} from './services/menu.service';
import {ShareModule} from './share.module';
import {LoggerModule, NGXLogger, NgxLoggerLevel} from 'ngx-logger';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    IcheckDirective,
    LoadingComponent,
    AlertComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ShareModule,
    LoggerModule.forRoot({level: NgxLoggerLevel.DEBUG, serverLogLevel: NgxLoggerLevel.ERROR}),
    routing
  ],
  providers: [
    UserService,
    AlertService,
    AppConfig,
    AuthGuard,
    NGXLogger,
    MenuService, {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
