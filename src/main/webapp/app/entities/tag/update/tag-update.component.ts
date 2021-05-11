import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITag, Tag } from '../tag.model';
import { TagService } from '../service/tag.service';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html',
})
export class TagUpdateComponent implements OnInit {
  isSaving = false;

  postsSharedCollection: IPost[] = [];

  editForm = this.fb.group({
    id: [],
    tagName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    posts: [],
  });

  constructor(
    protected tagService: TagService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.updateForm(tag);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tag = this.createFromForm();
    if (tag.id !== undefined) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  trackPostById(index: number, item: IPost): number {
    return item.id!;
  }

  getSelectedPost(option: IPost, selectedVals?: IPost[]): IPost {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>): void {
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

  protected updateForm(tag: ITag): void {
    this.editForm.patchValue({
      id: tag.id,
      tagName: tag.tagName,
      posts: tag.posts,
    });

    this.postsSharedCollection = this.postService.addPostToCollectionIfMissing(this.postsSharedCollection, ...(tag.posts ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.postService
      .query()
      .pipe(map((res: HttpResponse<IPost[]>) => res.body ?? []))
      .pipe(map((posts: IPost[]) => this.postService.addPostToCollectionIfMissing(posts, ...(this.editForm.get('posts')!.value ?? []))))
      .subscribe((posts: IPost[]) => (this.postsSharedCollection = posts));
  }

  protected createFromForm(): ITag {
    return {
      ...new Tag(),
      id: this.editForm.get(['id'])!.value,
      tagName: this.editForm.get(['tagName'])!.value,
      posts: this.editForm.get(['posts'])!.value,
    };
  }
}
