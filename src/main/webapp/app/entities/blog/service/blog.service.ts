import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBlog, getBlogIdentifier } from '../blog.model';

export type EntityResponseType = HttpResponse<IBlog>;
export type EntityArrayResponseType = HttpResponse<IBlog[]>;

@Injectable({ providedIn: 'root' })
export class BlogService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/blogs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(blog: IBlog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blog);
    return this.http
      .post<IBlog>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(blog: IBlog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blog);
    return this.http
      .put<IBlog>(`${this.resourceUrl}/${getBlogIdentifier(blog) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(blog: IBlog): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blog);
    return this.http
      .patch<IBlog>(`${this.resourceUrl}/${getBlogIdentifier(blog) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBlog>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBlog[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBlogToCollectionIfMissing(blogCollection: IBlog[], ...blogsToCheck: (IBlog | null | undefined)[]): IBlog[] {
    const blogs: IBlog[] = blogsToCheck.filter(isPresent);
    if (blogs.length > 0) {
      const blogCollectionIdentifiers = blogCollection.map(blogItem => getBlogIdentifier(blogItem)!);
      const blogsToAdd = blogs.filter(blogItem => {
        const blogIdentifier = getBlogIdentifier(blogItem);
        if (blogIdentifier == null || blogCollectionIdentifiers.includes(blogIdentifier)) {
          return false;
        }
        blogCollectionIdentifiers.push(blogIdentifier);
        return true;
      });
      return [...blogsToAdd, ...blogCollection];
    }
    return blogCollection;
  }

  protected convertDateFromClient(blog: IBlog): IBlog {
    return Object.assign({}, blog, {
      creationDate: blog.creationDate?.isValid() ? blog.creationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate ? dayjs(res.body.creationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((blog: IBlog) => {
        blog.creationDate = blog.creationDate ? dayjs(blog.creationDate) : undefined;
      });
    }
    return res;
  }
}
