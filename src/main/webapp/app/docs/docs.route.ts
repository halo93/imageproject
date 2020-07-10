import { Route } from '@angular/router';

import { DocsComponent } from './docs.component';

export const docsRoute: Route = {
  path: 'docs',
  component: DocsComponent,
  data: {
    pageTitle: 'API',
  },
};
