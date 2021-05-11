import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INotification, Notification } from '../notification.model';
import { NotificationService } from '../service/notification.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-notification-update',
  templateUrl: './notification-update.component.html',
})
export class NotificationUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    notificationDate: [],
    notificationReason: [null, [Validators.required]],
    notificationText: [null, [Validators.minLength(2), Validators.maxLength(100)]],
    isDelivered: [],
    appuser: [null, Validators.required],
  });

  constructor(
    protected notificationService: NotificationService,
    protected appuserService: AppuserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notification }) => {
      if (notification.id === undefined) {
        const today = dayjs().startOf('day');
        notification.creationDate = today;
        notification.notificationDate = today;
      }

      this.updateForm(notification);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notification = this.createFromForm();
    if (notification.id !== undefined) {
      this.subscribeToSaveResponse(this.notificationService.update(notification));
    } else {
      this.subscribeToSaveResponse(this.notificationService.create(notification));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotification>>): void {
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

  protected updateForm(notification: INotification): void {
    this.editForm.patchValue({
      id: notification.id,
      creationDate: notification.creationDate ? notification.creationDate.format(DATE_TIME_FORMAT) : null,
      notificationDate: notification.notificationDate ? notification.notificationDate.format(DATE_TIME_FORMAT) : null,
      notificationReason: notification.notificationReason,
      notificationText: notification.notificationText,
      isDelivered: notification.isDelivered,
      appuser: notification.appuser,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(
      this.appusersSharedCollection,
      notification.appuser
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appuserService
      .query()
      .pipe(map((res: HttpResponse<IAppuser[]>) => res.body ?? []))
      .pipe(
        map((appusers: IAppuser[]) => this.appuserService.addAppuserToCollectionIfMissing(appusers, this.editForm.get('appuser')!.value))
      )
      .subscribe((appusers: IAppuser[]) => (this.appusersSharedCollection = appusers));
  }

  protected createFromForm(): INotification {
    return {
      ...new Notification(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      notificationDate: this.editForm.get(['notificationDate'])!.value
        ? dayjs(this.editForm.get(['notificationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      notificationReason: this.editForm.get(['notificationReason'])!.value,
      notificationText: this.editForm.get(['notificationText'])!.value,
      isDelivered: this.editForm.get(['isDelivered'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
    };
  }
}
