import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITopic, Topic } from '../topic.model';
import { TopicService } from '../service/topic.service';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';

@Component({
  selector: 'jhi-topic-update',
  templateUrl: './topic-update.component.html',
})
export class TopicUpdateComponent implements OnInit {
  isSaving = false;

  postsSharedCollection: IPost[] = [];

  editForm = this.fb.group({
    id: [],
    topicName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    posts: [],
  });

  constructor(
    protected topicService: TopicService,
    protected postService: PostService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ topic }) => {
      this.updateForm(topic);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const topic = this.createFromForm();
    if (topic.id !== undefined) {
      this.subscribeToSaveResponse(this.topicService.update(topic));
    } else {
      this.subscribeToSaveResponse(this.topicService.create(topic));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITopic>>): void {
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

  protected updateForm(topic: ITopic): void {
    this.editForm.patchValue({
      id: topic.id,
      topicName: topic.topicName,
      posts: topic.posts,
    });

    this.postsSharedCollection = this.postService.addPostToCollectionIfMissing(this.postsSharedCollection, ...(topic.posts ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.postService
      .query()
      .pipe(map((res: HttpResponse<IPost[]>) => res.body ?? []))
      .pipe(map((posts: IPost[]) => this.postService.addPostToCollectionIfMissing(posts, ...(this.editForm.get('posts')!.value ?? []))))
      .subscribe((posts: IPost[]) => (this.postsSharedCollection = posts));
  }

  protected createFromForm(): ITopic {
    return {
      ...new Topic(),
      id: this.editForm.get(['id'])!.value,
      topicName: this.editForm.get(['topicName'])!.value,
      posts: this.editForm.get(['posts'])!.value,
    };
  }
}
