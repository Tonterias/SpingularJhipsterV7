import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IActivity, Activity } from '../activity.model';
import { ActivityService } from '../service/activity.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-activity-update',
  templateUrl: './activity-update.component.html',
})
export class ActivityUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    activityName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    appusers: [],
  });

  constructor(
    protected activityService: ActivityService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      this.updateForm(activity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activity = this.createFromForm();
    if (activity.id !== undefined) {
      this.subscribeToSaveResponse(this.activityService.update(activity));
    } else {
      this.subscribeToSaveResponse(this.activityService.create(activity));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivity>>): void {
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

  protected updateForm(activity: IActivity): void {
    this.editForm.patchValue({
      id: activity.id,
      activityName: activity.activityName,
      appusers: activity.appusers,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(
      this.appusersSharedCollection,
      ...(activity.appusers ?? [])
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

  protected createFromForm(): IActivity {
    return {
      ...new Activity(),
      id: this.editForm.get(['id'])!.value,
      activityName: this.editForm.get(['activityName'])!.value,
      appusers: this.editForm.get(['appusers'])!.value,
    };
  }
}
