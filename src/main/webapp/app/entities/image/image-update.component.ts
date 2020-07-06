import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IImage, Image } from 'app/shared/model/image.model';
import { ImageService } from './image.service';

@Component({
  selector: 'jhi-image-update',
  templateUrl: './image-update.component.html',
})
export class ImageUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    path: [null, [Validators.maxLength(200)]],
    pictureDescription: [null, [Validators.maxLength(500)]],
    fileType: [null, [Validators.maxLength(100)]],
    size: [null, [Validators.required, Validators.min(1), Validators.max(500000)]],
    isActive: [null, [Validators.required]],
  });

  constructor(protected imageService: ImageService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ image }) => {
      this.updateForm(image);
    });
  }

  updateForm(image: IImage): void {
    this.editForm.patchValue({
      id: image.id,
      path: image.path,
      pictureDescription: image.pictureDescription,
      fileType: image.fileType,
      size: image.size,
      isActive: image.isActive,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const image = this.createFromForm();
    if (image.id !== undefined) {
      this.subscribeToSaveResponse(this.imageService.update(image));
    } else {
      this.subscribeToSaveResponse(this.imageService.create(image));
    }
  }

  private createFromForm(): IImage {
    return {
      ...new Image(),
      id: this.editForm.get(['id'])!.value,
      path: this.editForm.get(['path'])!.value,
      pictureDescription: this.editForm.get(['pictureDescription'])!.value,
      fileType: this.editForm.get(['fileType'])!.value,
      size: this.editForm.get(['size'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImage>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
