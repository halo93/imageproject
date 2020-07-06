import { NgModule } from '@angular/core';
import { ImageprojectSharedLibsModule } from './shared-libs.module';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';

@NgModule({
  imports: [ImageprojectSharedLibsModule],
  declarations: [AlertComponent, AlertErrorComponent],
  exports: [ImageprojectSharedLibsModule, AlertComponent, AlertErrorComponent],
})
export class ImageprojectSharedModule {}
