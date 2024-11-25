import { TestBed } from '@angular/core/testing';

import { CartUtilsService } from './cart-utils.service';

describe('CartUtilsService', () => {
  let service: CartUtilsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CartUtilsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
