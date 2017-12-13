import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {PageNotFoundComponent} from './pages/page-not-found/page-not-found.component';
import {ModuleWithProviders} from '@angular/core';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', component: LoginComponent },
  { path: '**', component: PageNotFoundComponent }
];


export const routing: ModuleWithProviders = RouterModule.forRoot(routes);
