import { TestBed } from '@angular/core/testing';

import { CustomConfigRatingService } from './custom-config-rating.service';

describe('CustomConfigRatingService', () => {
  let service: CustomConfigRatingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomConfigRatingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
