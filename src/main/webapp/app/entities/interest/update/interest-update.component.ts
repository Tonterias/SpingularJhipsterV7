import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInterest, Interest } from '../interest.model';
import { InterestService } from '../service/interest.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-interest-update',
  templateUrl: './interest-update.component.html',
})
export class InterestUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    interestName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    appusers: [],
  });

  constructor(
    protected interestService: InterestService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interest }) => {
      this.updateForm(interest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const interest = this.createFromForm();
    if (interest.id !== undefined) {
      this.subscribeToSaveResponse(this.interestService.update(interest));
    } else {
      this.subscribeToSaveResponse(this.interestService.create(interest));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  getSelectedAppuser(option: IAppuser, selectedVals?: IAppuser[]): IAppuser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInterest>>): void {
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

  protected updateForm(interest: IInterest): void {
    this.editForm.patchValue({
      id: interest.id,
      interestName: interest.interestName,
      appusers: interest.appusers,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(
      this.appusersSharedCollection,
      ...(interest.appusers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) =>
          this.appuserService.addAppuserToCollectionIfMissing(appusers, ...(this.editForm.get('appusers')!.value ?? []))
        )
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));
  }

  protected createFromForm(): IInterest {
    return {
      ...new Interest(),
      id: this.editForm.get(['id'])!.value,
      interestName: this.editForm.get(['interestName'])!.value,
      appusers: this.editForm.get(['appusers'])!.value,
    };
  }
}
