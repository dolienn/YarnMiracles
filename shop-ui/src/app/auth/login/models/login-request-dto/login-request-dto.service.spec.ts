import { TestBed } from '@angular/core/testing';

import { LoginRequestDTOService } from './login-request-dto.service';

describe('LoginRequestDTOService', () => {
  let service: LoginRequestDTOService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginRequestDTOService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
