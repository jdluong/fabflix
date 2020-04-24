import { TestBed } from '@angular/core/testing';

import { ServerCacheService } from './server-cache.service';

describe('ServerCacheService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ServerCacheService = TestBed.get(ServerCacheService);
    expect(service).toBeTruthy();
  });
});
