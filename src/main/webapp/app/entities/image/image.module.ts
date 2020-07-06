import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ImageprojectSharedModule } from 'app/shared/shared.module';
import { ImageComponent } from './image.component';
import { ImageUpdateComponent } from './image-update.component';
import { imageRoute } from './image.route';

@NgModule({
  imports: [ImageprojectSharedModule, RouterModule.forChild(imageRoute)],
  declarations: [ImageComponent, ImageUpdateComponent],
})
export class ImageprojectImageModule {}
