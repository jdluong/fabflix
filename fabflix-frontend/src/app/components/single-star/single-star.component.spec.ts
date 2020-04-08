import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleStarComponent } from './single-star.component';

describe('SingleStarComponent', () => {
  let component: SingleStarComponent;
  let fixture: ComponentFixture<SingleStarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SingleStarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SingleStarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
