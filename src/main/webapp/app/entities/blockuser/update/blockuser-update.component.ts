import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBlockuser, Blockuser } from '../blockuser.model';
import { BlockuserService } from '../service/blockuser.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-blockuser-update',
  templateUrl: './blockuser-update.component.html',
})
export class BlockuserUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];
  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [],
    blockeduser: [],
    blockinguser: [],
    cblockeduser: [],
    cblockinguser: [],
  });

  constructor(
    protected blockuserService: BlockuserService,
    protected appuserService: AppuserService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blockuser }) => {
      if (blockuser.id === undefined) {
        const today = dayjs().startOf('day');
        blockuser.creationDate = today;
      }

      this.updateForm(blockuser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const blockuser = this.createFromForm();
    if (blockuser.id !== undefined) {
      this.subscribeToSaveResponse(this.blockuserService.update(blockuser));
    } else {
      this.subscribeToSaveResponse(this.blockuserService.create(blockuser));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  trackCommunityById(index: number, item: ICommunity): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlockuser>>): void {
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

  protected updateForm(blockuser: IBlockuser): void {
    this.editForm.patchValue({
      id: blockuser.id,
      creationDate: blockuser.creationDate ? blockuser.creationDate.format(DATE_TIME_FORMAT) : null,
      blockeduser: blockuser.blockeduser,
      blockinguser: blockuser.blockinguser,
      cblockeduser: blockuser.cblockeduser,
      cblockinguser: blockuser.cblockinguser,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(
      this.appusersSharedCollection,
      blockuser.blockeduser,
      blockuser.blockinguser
    );
    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      blockuser.cblockeduser,
      blockuser.cblockinguser
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
            this.editForm.get('blockeduser')!.value,
            this.editForm.get('blockinguser')!.value
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
            this.editForm.get('cblockeduser')!.value,
            this.editForm.get('cblockinguser')!.value
          )
        )
      )
      .subscribe((communities: ICommunity[]) => (this.communitiesSharedCollection = communities));
  }

  protected createFromForm(): IBlockuser {
    return {
      ...new Blockuser(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      blockeduser: this.editForm.get(['blockeduser'])!.value,
      blockinguser: this.editForm.get(['blockinguser'])!.value,
      cblockeduser: this.editForm.get(['cblockeduser'])!.value,
      cblockinguser: this.editForm.get(['cblockinguser'])!.value,
    };
  }
}
