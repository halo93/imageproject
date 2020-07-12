import { Component, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ImageService } from './image.service';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  templateUrl: './image-upload-dialog.component.html',
})
export class ImageUploadDialogComponent {
  submitted: boolean;

  uploadForm = this.fb.group({
    description: [null, [Validators.maxLength(500), Validators.required]],
    uploadedImage: [null, [Validators.required]],
  });

  constructor(
    protected imageService: ImageService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager,
    protected elementRef: ElementRef,
    private fb: FormBuilder
  ) {
    this.submitted = false;
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmUpload(content: any): void {
    this.submitted = true;
    if (this.uploadForm.invalid) {
      return;
    }
    const formData: FormData = new FormData();
    formData.append('description', content.description);
    formData.append('uploadedImage', content.uploadedImage, content.uploadedImage.name);
    this.imageService.upload(formData).subscribe(() => {
      this.submitted = false;
      this.eventManager.broadcast('imageListModification');
      this.activeModal.close();
    });
  }

  setFileData(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      this.uploadForm.get('uploadedImage')?.setValue(target.files[0]);
    } else {
      this.uploadForm.get('uploadedImage')?.setErrors({ required: true });
    }
  }

  validateFileSize(): void {
    const uploadedImage = this.uploadForm.get('uploadedImage');
    if (uploadedImage && uploadedImage.value.size > 500000) {
      uploadedImage.setErrors({ maxbyte: true });
    }
  }

  clearInputImage(field: string, idInput: string): void {
    this.uploadForm.patchValue({
      [field]: null,
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }
}
