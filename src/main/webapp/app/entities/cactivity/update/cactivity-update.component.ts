import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICactivity, Cactivity } from '../cactivity.model';
import { CactivityService } from '../service/cactivity.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-cactivity-update',
  templateUrl: './cactivity-update.component.html',
})
export class CactivityUpdateComponent implements OnInit {
  isSaving = false;

  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    activityName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    communities: [],
  });

  constructor(
    protected cactivityService: CactivityService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cactivity }) => {
      this.updateForm(cactivity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cactivity = this.createFromForm();
    if (cactivity.id !== undefined) {
      this.subscribeToSaveResponse(this.cactivityService.update(cactivity));
    } else {
      this.subscribeToSaveResponse(this.cactivityService.create(cactivity));
    }
  }

  trackCommunityById(index: number, item: ICommunity): number {
    return item.id!;
  }

  getSelectedCommunity(option: ICommunity, selectedVals?: ICommunity[]): ICommunity {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICactivity>>): void {
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

  protected updateForm(cactivity: ICactivity): void {
    this.editForm.patchValue({
      id: cactivity.id,
      activityName: cactivity.activityName,
      communities: cactivity.communities,
    });

    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      ...(cactivity.communities ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.communityService
      .query()
      .pipe(map((res: HttpResponse<ICommunity[]>) => res.body ?? []))
      .pipe(
        map((communities: ICommunity[]) =>
          this.communityService.addCommunityToCollectionIfMissing(communities, ...(this.editForm.get('communities')!.value ?? []))
        )
      )
      .subscribe((communities: ICommunity[]) => (this.communitiesSharedCollection = communities));
  }

  protected createFromForm(): ICactivity {
    return {
      ...new Cactivity(),
      id: this.editForm.get(['id'])!.value,
      activityName: this.editForm.get(['activityName'])!.value,
      communities: this.editForm.get(['communities'])!.value,
    };
  }
}
