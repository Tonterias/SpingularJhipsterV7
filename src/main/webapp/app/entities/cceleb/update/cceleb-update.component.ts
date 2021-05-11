import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICceleb, Cceleb } from '../cceleb.model';
import { CcelebService } from '../service/cceleb.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

@Component({
  selector: 'jhi-cceleb-update',
  templateUrl: './cceleb-update.component.html',
})
export class CcelebUpdateComponent implements OnInit {
  isSaving = false;

  communitiesSharedCollection: ICommunity[] = [];

  editForm = this.fb.group({
    id: [],
    celebName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    communities: [],
  });

  constructor(
    protected ccelebService: CcelebService,
    protected communityService: CommunityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cceleb }) => {
      this.updateForm(cceleb);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cceleb = this.createFromForm();
    if (cceleb.id !== undefined) {
      this.subscribeToSaveResponse(this.ccelebService.update(cceleb));
    } else {
      this.subscribeToSaveResponse(this.ccelebService.create(cceleb));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICceleb>>): void {
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

  protected updateForm(cceleb: ICceleb): void {
    this.editForm.patchValue({
      id: cceleb.id,
      celebName: cceleb.celebName,
      communities: cceleb.communities,
    });

    this.communitiesSharedCollection = this.communityService.addCommunityToCollectionIfMissing(
      this.communitiesSharedCollection,
      ...(cceleb.communities ?? [])
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

  protected createFromForm(): ICceleb {
    return {
      ...new Cceleb(),
      id: this.editForm.get(['id'])!.value,
      celebName: this.editForm.get(['celebName'])!.value,
      communities: this.editForm.get(['communities'])!.value,
    };
  }
}
