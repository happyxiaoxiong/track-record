import {Injectable, OnDestroy, OnInit} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {Router} from '@angular/router';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AlertService implements OnInit, OnDestroy {
  private subject = new Subject<any>();
  private evnSubscribe;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.evnSubscribe = this.router.events.subscribe(event => {
      this.subject.next();
    });
  }

  ngOnDestroy(): void {
    this.evnSubscribe.unsubscribe();
  }

  success(message: string) {
    this.subject.next({ type: 'success', text: message });
  }

  error(message: string) {
    this.subject.next({ type: 'error', text: message });
  }

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }
}
