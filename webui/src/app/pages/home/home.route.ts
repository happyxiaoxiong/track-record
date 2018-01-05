import {HomeComponent} from './home.component';
import {PageNotFoundComponent} from '../page-not-found/page-not-found.component';
import {IndexComponent} from './index/index.component';
import {RealTimePositionComponent} from './real-time-position/real-time-position.component';
import {FileUploadComponent} from './file-upload/file-upload.component';
import {HistoryTrackComponent} from './history-track/history-track.component';

export const homeRoutes = [
  {
    path: '',
    component: HomeComponent,
    children: [
      { path: '', component: IndexComponent },
      { path: 'file-upload',  component: FileUploadComponent },
      { path: 'history-track',  component: HistoryTrackComponent },
      { path: 'real-time-position',  component: RealTimePositionComponent },
      { path: '**',  component: PageNotFoundComponent }
    ]}
];
