import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ImageService } from './image.service';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  templateUrl: './image-upload-dialog.component.html',
})
export class ImageUploadDialogComponent {
  uploadForm = this.fb.group({
    description: [null, [Validators.maxLength(500), Validators.required]],
  });

  constructor(
    protected imageService: ImageService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    private fb: FormBuilder
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmUpload(content: any): void {
    const formData: FormData = new FormData();
    formData.append('description', content.description);
    this.imageService.upload(formData).subscribe(() => {
      this.eventManager.broadcast('imageListModification');
      this.activeModal.close();
    });
  }
}
