import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ImageprojectSharedModule } from 'app/shared/shared.module';
import { ImageprojectCoreModule } from 'app/core/core.module';
import { ImageprojectAppRoutingModule } from './app-routing.module';
import { ImageprojectHomeModule } from './home/home.module';
import { ImageprojectEntityModule } from './entities/entity.module';
import { DocsModule } from './docs/docs.module';

// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    ImageprojectSharedModule,
    ImageprojectCoreModule,
    ImageprojectHomeModule,
    DocsModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ImageprojectEntityModule,
    ImageprojectAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class ImageprojectAppModule {}
