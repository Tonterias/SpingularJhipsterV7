import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFollow, Follow } from '../follow.model';
import { FollowService } from '../service/follow.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-follow-update',
  templateUrl: './follow-update.component.html',
})
export class FollowUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];
  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    followed: [],
    following: [],
    cfollowed: [],
    cfollowing: [],
  });

  constructor(
    protected followService: FollowService,
    protected appuserService: AppuserService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ follow }) => {
      if (follow.id === undefined) {
        const today = dayjs().startOf('day');
        follow.creationDate = today;
      }

      this.updateForm(follow);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const follow = this.createFromForm();
    if (follow.id !== undefined) {
      this.subscribeToSaveResponse(this.followService.update(follow));
    } else {
      this.subscribeToSaveResponse(this.followService.create(follow));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  trackCommunityById(index: number, item: ICommunity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFollow>>): void {
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

  protected updateForm(follow: IFollow): void {
    this.editForm.patchValue({
      id: follow.id,
      creationDate: follow.creationDate ? follow.creationDate.format(DATE_TIME_FORMAT) : null,
      followed: follow.followed,
      following: follow.following,
      cfollowed: follow.cfollowed,
      cfollowing: follow.cfollowing,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(
      this.appusersSharedCollection,
      follow.followed,
      follow.following
    );
    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      follow.cfollowed,
      follow.cfollowing
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) =>
          this.appuserService.addAppuserToCollectionIfMissing(
            appusers,
            this.editForm.get('followed')!.value,
            this.editForm.get('following')!.value
          )
        )
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));

    this.communityService
      .query()
      .pipe(map((res: HttpResponse<ICommunity[]>) => res.body ?? []))
      .pipe(
        map((communities: ICommunity[]) =>
          this.communityService.addCommunityToCollectionIfMissing(
            communities,
            this.editForm.get('cfollowed')!.value,
            this.editForm.get('cfollowing')!.value
          )
        )
      )
      .subscribe((communities: ICommunity[]) => (this.communitiesSharedCollection = communities));
  }

  protected createFromForm(): IFollow {
    return {
      ...new Follow(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      followed: this.editForm.get(['followed'])!.value,
      following: this.editForm.get(['following'])!.value,
      cfollowed: this.editForm.get(['cfollowed'])!.value,
      cfollowing: this.editForm.get(['cfollowing'])!.value,
    };
  }
}
