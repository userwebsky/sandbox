import { TestBed } from '@angular/core/testing';

import { HeaderClassInterceptor } from './header.class.interceptor';

describe('HeaderClassInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      HeaderClassInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: HeaderClassInterceptor = TestBed.inject(HeaderClassInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
