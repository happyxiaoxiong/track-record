import {Injectable, OnInit} from '@angular/core';
import {Subject} from 'rxjs/Subject';
import {Router} from '@angular/router';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AlertService implements OnInit {
  private subject = new Subject<any>();

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      this.subject.next();
    });
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
