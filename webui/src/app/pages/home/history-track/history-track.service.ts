import { Injectable } from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {Observable} from 'rxjs/Observable';

export enum TrackMapType {
  ADD = 1,
  REMOVE = 2,
  CLEAR = 3
}

@Injectable()
export class HistoryTrackService {

  private updateSubject = new Subject<any>();
  private animatedSubject = new Subject<any>();
  private trackCount = 0;
  private diff = 0;

  constructor() { }

  increase(id: string) {
    this.updateTrackCount(1);
    this.updateSubject.next({
      id: id,
      type: TrackMapType.ADD,
      trackCount: this.trackCount,
      diff: this.diff
    });
  }

  decrease(id: string) {
    if (this.trackCount > 0) {
      this.updateTrackCount(-1);
      this.updateSubject.next({
        id: id,
        type: TrackMapType.REMOVE,
        trackCount: this.trackCount,
        diff: this.diff
      });
    }
  }

  clear() {
    if (this.trackCount > 0) {
      this.updateTrackCount(-this.trackCount)
      this.updateSubject.next({
        type: TrackMapType.CLEAR,
        trackCount: this.trackCount,
        diff: this.diff
      });
    }
  }

  getTrackCount() {
    return this.trackCount;
  }

  getUpdateSubject(): Observable<any> {
    return this.updateSubject.asObservable();
  }

  animated() {
    this.animatedSubject.next(this.trackCount);
  }

  getAnimatedSubject(): Observable<any> {
    return this.animatedSubject.asObservable();
  }

  private updateTrackCount(val) {
    this.diff = val;
    this.trackCount += val;
  }
}
