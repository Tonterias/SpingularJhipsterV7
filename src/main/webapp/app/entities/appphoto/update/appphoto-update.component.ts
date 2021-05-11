import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAppphoto, Appphoto } from '../appphoto.model';
import { AppphotoService } from '../service/appphoto.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-appphoto-update',
  templateUrl: './appphoto-update.component.html',
})
export class AppphotoUpdateComponent implements OnInit {
  isSaving = false;

  appusersCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    image: [],
    imageContentType: [],
    appuser: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected appphotoService: AppphotoService,
    protected appuserService: AppuserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appphoto }) => {
      if (appphoto.id === undefined) {
        const today = dayjs().startOf('day');
        appphoto.creationDate = today;
      }

      this.updateForm(appphoto);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('spingularApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appphoto = this.createFromForm();
    if (appphoto.id !== undefined) {
      this.subscribeToSaveResponse(this.appphotoService.update(appphoto));
    } else {
      this.subscribeToSaveResponse(this.appphotoService.create(appphoto));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppphoto>>): void {
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

  protected updateForm(appphoto: IAppphoto): void {
    this.editForm.patchValue({
      id: appphoto.id,
      creationDate: appphoto.creationDate ? appphoto.creationDate.format(DATE_TIME_FORMAT) : null,
      image: appphoto.image,
      imageContentType: appphoto.imageContentType,
      appuser: appphoto.appuser,
    });

    this.appusersCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersCollection, appphoto.appuser);
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query({ 'appphotoId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) => this.appuserService.addAppuserToCollectionIfMissing(appusers, this.editForm.get('appuser')!.value))
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersCollection = appusers));
  }

  protected createFromForm(): IAppphoto {
    return {
      ...new Appphoto(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
    };
  }
}
