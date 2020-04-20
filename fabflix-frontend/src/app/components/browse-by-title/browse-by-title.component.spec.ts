import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseByTitleComponent } from './browse-by-title.component';

describe('BrowseByTitleComponent', () => {
  let component: BrowseByTitleComponent;
  let fixture: ComponentFixture<BrowseByTitleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BrowseByTitleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowseByTitleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
