import {Observable} from 'rxjs/Observable';

export interface Imap {

  show(overlay: any): void;

  hide(overlay: any): void;

  remove(overlay: any): void;

  add(overlay: any): void;

  clearOverLays(): void;

  events(): Observable<MapEvent>;
}

export declare class ClickEvent {
  /** @docsNotRequired */
  lng: number;
  /** @docsNotRequired */
  lat: string;

  constructor(
    /** @docsNotRequired */
    lng: number,
    /** @docsNotRequired */
    lat: string);
}

export declare type MapEvent = ClickEvent;
