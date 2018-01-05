import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RealTimePositionComponent } from './real-time-position.component';

describe('RealTimePositionComponent', () => {
  let component: RealTimePositionComponent;
  let fixture: ComponentFixture<RealTimePositionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RealTimePositionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RealTimePositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
