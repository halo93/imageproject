import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IImage } from 'app/shared/model/image.model';

type EntityResponseType = HttpResponse<IImage>;
type EntityArrayResponseType = HttpResponse<IImage[]>;

@Injectable({ providedIn: 'root' })
export class ImageService {
  public resourceUrl = SERVER_API_URL + 'api/images';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/images';

  constructor(protected http: HttpClient) {}

  upload(formData: FormData): Observable<EntityResponseType> {
    return this.http.post<IImage>(this.resourceUrl, formData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImage[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
