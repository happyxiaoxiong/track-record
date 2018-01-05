import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {ModuleWithProviders} from '@angular/core';
import {HomeModule} from './pages/home/home.module';
import {AuthGuard} from './guards/auth.guard';
import {PageNotFoundComponent} from './pages/page-not-found/page-not-found.component';

const routes: Routes = [
  {
    path: 'home',
    canActivate: [AuthGuard],
    loadChildren: () => HomeModule,
    data: [
      {
        skin: 'skin-purple'
      }
    ]
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '**',  component: PageNotFoundComponent }
];
// AIzaSyCgHbqIlcIDZVEfFiGVW_wWnQstDxOUm18

export const routing: ModuleWithProviders = RouterModule.forRoot(routes);
