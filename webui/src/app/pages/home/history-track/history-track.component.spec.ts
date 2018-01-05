import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryTrackComponent } from './history-track.component';

describe('HistoryTrackComponent', () => {
  let component: HistoryTrackComponent;
  let fixture: ComponentFixture<HistoryTrackComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistoryTrackComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoryTrackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
