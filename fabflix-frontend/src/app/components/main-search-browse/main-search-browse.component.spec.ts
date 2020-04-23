import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MainSearchBrowseComponent } from './main-search-browse.component';

describe('MainSearchBrowseComponent', () => {
  let component: MainSearchBrowseComponent;
  let fixture: ComponentFixture<MainSearchBrowseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MainSearchBrowseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MainSearchBrowseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
