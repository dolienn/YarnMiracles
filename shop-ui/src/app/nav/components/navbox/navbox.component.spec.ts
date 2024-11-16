import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavboxComponent } from './navbox.component';

describe('NavboxComponent', () => {
  let component: NavboxComponent;
  let fixture: ComponentFixture<NavboxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NavboxComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NavboxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
