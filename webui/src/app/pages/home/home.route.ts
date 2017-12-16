import {HomeComponent} from './home.component';

export const homeRoutes = [
  { path: '',
    children: [
      { path: '',  component: HomeComponent },
      { path: 'test',  component: HomeComponent },
      // { path: 'dashboard2', component: Dashboard2Component }
    ]},
];
