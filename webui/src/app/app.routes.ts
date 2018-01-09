import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {HomeModule} from './pages/home/home.module';
import {PageNotFoundComponent} from './pages/page-not-found/page-not-found.component';
import {RegisterModule} from './pages/register/register.module';

const routes: Routes = [
  {
    path: 'home',
    loadChildren: () => HomeModule,
    data: [
      {
        skin: 'skin-purple'
      }
    ]
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', loadChildren: () => RegisterModule },
  { path: '**',  component: PageNotFoundComponent }
];
// AIzaSyCgHbqIlcIDZVEfFiGVW_wWnQstDxOUm18

export const routing = RouterModule.forRoot(routes);
