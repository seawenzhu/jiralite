import { TestBed, inject } from '@angular/core/testing';

import { FroalaEditorService } from './froala-editor.service';

describe('FroalaEditorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FroalaEditorService]
    });
  });

  it('should be created', inject([FroalaEditorService], (service: FroalaEditorService) => {
    expect(service).toBeTruthy();
  }));
});
