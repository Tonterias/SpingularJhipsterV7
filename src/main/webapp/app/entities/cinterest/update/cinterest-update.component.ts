import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICinterest, Cinterest } from '../cinterest.model';
import { CinterestService } from '../service/cinterest.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-cinterest-update',
  templateUrl: './cinterest-update.component.html',
})
export class CinterestUpdateComponent implements OnInit {
  isSaving = false;

  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    interestName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    communities: [],
  });

  constructor(
    protected cinterestService: CinterestService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cinterest }) => {
      this.updateForm(cinterest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cinterest = this.createFromForm();
    if (cinterest.id !== undefined) {
      this.subscribeToSaveResponse(this.cinterestService.update(cinterest));
    } else {
      this.subscribeToSaveResponse(this.cinterestService.create(cinterest));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICinterest>>): void {
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

  protected updateForm(cinterest: ICinterest): void {
    this.editForm.patchValue({
      id: cinterest.id,
      interestName: cinterest.interestName,
      communities: cinterest.communities,
    });

    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      ...(cinterest.communities ?? [])
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

  protected createFromForm(): ICinterest {
    return {
      ...new Cinterest(),
      id: this.editForm.get(['id'])!.value,
      interestName: this.editForm.get(['interestName'])!.value,
      communities: this.editForm.get(['communities'])!.value,
    };
  }
}
