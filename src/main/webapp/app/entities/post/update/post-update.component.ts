import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPost, Post } from '../post.model';
import { PostService } from '../service/post.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IBlog } from 'app/entities/blog/blog.model';
import { BlogService } from 'app/entities/blog/service/blog.service';

@Component({
  selector: 'jhi-post-update',
  templateUrl: './post-update.component.html',
})
export class PostUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];
  blogsSharedCollection: IBlog[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    publicationDate: [],
    headline: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    leadtext: [null, [Validators.minLength(2), Validators.maxLength(1000)]],
    bodytext: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(65000)]],
    quote: [null, [Validators.minLength(2), Validators.maxLength(1000)]],
    conclusion: [null, [Validators.minLength(2), Validators.maxLength(2000)]],
    linkText: [null, [Validators.minLength(2), Validators.maxLength(1000)]],
    linkURL: [null, [Validators.minLength(2), Validators.maxLength(1000)]],
    image: [],
    imageContentType: [],
    appuser: [null, Validators.required],
    blog: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected postService: PostService,
    protected appuserService: AppuserService,
    protected blogService: BlogService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ post }) => {
      if (post.id === undefined) {
        const today = dayjs().startOf('day');
        post.creationDate = today;
        post.publicationDate = today;
      }

      this.updateForm(post);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('spingularApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const post = this.createFromForm();
    if (post.id !== undefined) {
      this.subscribeToSaveResponse(this.postService.update(post));
    } else {
      this.subscribeToSaveResponse(this.postService.create(post));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  trackBlogById(index: number, item: IBlog): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPost>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(post: IPost): void {
    this.editForm.patchValue({
      id: post.id,
      creationDate: post.creationDate ? post.creationDate.format(DATE_TIME_FORMAT) : null,
      publicationDate: post.publicationDate ? post.publicationDate.format(DATE_TIME_FORMAT) : null,
      headline: post.headline,
      leadtext: post.leadtext,
      bodytext: post.bodytext,
      quote: post.quote,
      conclusion: post.conclusion,
      linkText: post.linkText,
      linkURL: post.linkURL,
      image: post.image,
      imageContentType: post.imageContentType,
      appuser: post.appuser,
      blog: post.blog,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersSharedCollection, post.appuser);
    this.blogsSharedCollection = this.blogService.addBlogToCollectionIfMissing(this.blogsSharedCollection, post.blog);
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) => this.appuserService.addAppuserToCollectionIfMissing(appusers, this.editForm.get('appuser')!.value))
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));

    this.blogService
      .query()
      .pipe(map((res: HttpResponse<IBlog[]>) => res.body ?? []))
      .pipe(map((blogs: IBlog[]) => this.blogService.addBlogToCollectionIfMissing(blogs, this.editForm.get('blog')!.value)))
      .subscribe((blogs: IBlog[]) => (this.blogsSharedCollection = blogs));
  }

  protected createFromForm(): IPost {
    return {
      ...new Post(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      publicationDate: this.editForm.get(['publicationDate'])!.value
        ? dayjs(this.editForm.get(['publicationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      headline: this.editForm.get(['headline'])!.value,
      leadtext: this.editForm.get(['leadtext'])!.value,
      bodytext: this.editForm.get(['bodytext'])!.value,
      quote: this.editForm.get(['quote'])!.value,
      conclusion: this.editForm.get(['conclusion'])!.value,
      linkText: this.editForm.get(['linkText'])!.value,
      linkURL: this.editForm.get(['linkURL'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
      blog: this.editForm.get(['blog'])!.value,
    };
  }
}
