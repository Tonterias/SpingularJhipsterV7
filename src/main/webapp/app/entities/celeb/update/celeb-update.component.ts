import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICeleb, Celeb } from '../celeb.model';
import { CelebService } from '../service/celeb.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-celeb-update',
  templateUrl: './celeb-update.component.html',
})
export class CelebUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    celebName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
    appusers: [],
  });

  constructor(
    protected celebService: CelebService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ celeb }) => {
      this.updateForm(celeb);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const celeb = this.createFromForm();
    if (celeb.id !== undefined) {
      this.subscribeToSaveResponse(this.celebService.update(celeb));
    } else {
      this.subscribeToSaveResponse(this.celebService.create(celeb));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICeleb>>): void {
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

  protected updateForm(celeb: ICeleb): void {
    this.editForm.patchValue({
      id: celeb.id,
      celebName: celeb.celebName,
      appusers: celeb.appusers,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(
      this.appusersSharedCollection,
      ...(celeb.appusers ?? [])
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

  protected createFromForm(): ICeleb {
    return {
      ...new Celeb(),
      id: this.editForm.get(['id'])!.value,
      celebName: this.editForm.get(['celebName'])!.value,
      appusers: this.editForm.get(['appusers'])!.value,
    };
  }
}
