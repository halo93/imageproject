import { Injectable } from '@angular/core';
import { Routes } from '@angular/router';
import { ImageComponent } from './image.component';

@Injectable({ providedIn: 'root' })
export class ImageResolve {}

export const imageRoute: Routes = [
  {
    path: '',
    component: ImageComponent,
    data: {
      pageTitle: 'Images',
    },
  },
];
