import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IImage } from 'app/shared/model/image.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ImageService } from './image.service';
import { ImageUploadDialogComponent } from 'app/entities/image/image-upload-dialog.component';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-image',
  styles: [
    `
      .search-results {
        height: 100%;
        overflow: scroll;
      }
    `,
  ],
  templateUrl: './image.component.html',
})
export class ImageComponent implements OnInit, OnDestroy {
  images: IImage[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;
  processing: boolean;
  fileTypeOptions: any[] = [
    {
      value: 'image/jpeg',
      description: 'JPEG',
    },
    {
      value: 'image/png',
      description: 'PNG',
    },
  ];

  searchForm = this.fb.group({
    description: [],
    fileType: [''],
    size: [],
  });

  constructor(
    protected imageService: ImageService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.images = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.processing = false;
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    this.processing = true;
    if (this.currentSearch) {
      this.imageService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<IImage[]>) => {
          this.paginateImages(res.body, res.headers);
          this.processing = false;
        });
      return;
    }

    this.imageService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IImage[]>) => {
        this.paginateImages(res.body, res.headers);
        this.processing = false;
      });
  }

  reset(): void {
    this.page = 0;
    this.images = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(content: {}): void {
    this.images = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (content && Object.values(content).some(e => e)) {
      this.predicate = '_score';
      this.ascending = false;
    } else {
      this.predicate = 'id';
      this.ascending = true;
      this.searchForm.patchValue({
        description: null,
        size: null,
        fileType: '',
      });
    }
    this.currentSearch = Object.entries(content)
      .filter(e => e[1])
      .map(e => `${e[0]}:"${e[1]}"`)
      .join(' AND ');
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInImages();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IImage): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInImages(): void {
    this.eventSubscriber = this.eventManager.subscribe('imageListModification', () => this.reset());
  }

  openUploadModal(): void {
    this.modalService.open(ImageUploadDialogComponent, { size: 'lg', backdrop: 'static' });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateImages(data: IImage[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    if (headersLink) {
      this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    }
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.images.push(data[i]);
      }
    }
  }
}
