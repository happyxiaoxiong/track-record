import {HomeComponent} from './home.component';
import {IndexComponent} from './index/index.component';
import {AuthGuard} from '../../guards/auth.guard';
import {RouterModule} from '@angular/router';
import {HistoryTrackModule} from './history-track/history-track.module';
import {FileUploadModule} from './file-upload/file-upload.module';
import {RealTimePositionModule} from './real-time-position/real-time-position.module';
import {PageNotFoundComponent} from '../page-not-found/page-not-found.component';

const routes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      { path: '', component: IndexComponent, canActivate: [AuthGuard] },
      { path: 'file-upload',  loadChildren: () => FileUploadModule, canActivate: [AuthGuard]},
      { path: 'history-track',  loadChildren: () => HistoryTrackModule, canActivate: [AuthGuard]},
      { path: 'real-time-position',  loadChildren: () => RealTimePositionModule, canActivate: [AuthGuard] },
      { path: '**',  component: PageNotFoundComponent },
    ]}
];

export const homeRoutes = RouterModule.forChild(routes);
