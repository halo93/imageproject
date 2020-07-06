import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './layouts/error/error.route';
import { navbarRoute } from './layouts/navbar/navbar.route';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
  imports: [RouterModule.forRoot([...LAYOUT_ROUTES], { enableTracing: true })],
  exports: [RouterModule],
})
export class ImageprojectAppRoutingModule {}
