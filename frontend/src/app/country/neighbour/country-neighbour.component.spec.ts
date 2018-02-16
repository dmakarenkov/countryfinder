import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CountryNeighbourComponent } from './country-neighbour.component';

describe('CountryNeighbourComponent', () => {
  let component: CountryNeighbourComponent;
  let fixture: ComponentFixture<CountryNeighbourComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CountryNeighbourComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CountryNeighbourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
