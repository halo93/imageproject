import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ImageprojectSharedModule } from 'app/shared/shared.module';
import { ImageComponent } from './image.component';
import { imageRoute } from './image.route';
import { ImageUploadDialogComponent } from 'app/entities/image/image-upload-dialog.component';

@NgModule({
  imports: [ImageprojectSharedModule, RouterModule.forChild(imageRoute)],
  declarations: [ImageComponent, ImageUploadDialogComponent],
})
export class ImageprojectImageModule {}
