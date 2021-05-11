import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommunity, Community } from '../community.model';
import { CommunityService } from '../service/community.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';

@Component({
  selector: 'jhi-community-update',
  templateUrl: './community-update.component.html',
})
export class CommunityUpdateComponent implements OnInit {
  isSaving = false;

  appusersSharedCollection: IAppuser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    communityName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    communityDescription: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(7500)]],
    image: [],
    imageContentType: [],
    isActive: [],
    appuser: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected communityService: CommunityService,
    protected appuserService: AppuserService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ community }) => {
      if (community.id === undefined) {
        const today = dayjs().startOf('day');
        community.creationDate = today;
      }

      this.updateForm(community);

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
    const community = this.createFromForm();
    if (community.id !== undefined) {
      this.subscribeToSaveResponse(this.communityService.update(community));
    } else {
      this.subscribeToSaveResponse(this.communityService.create(community));
    }
  }

  trackAppuserById(index: number, item: IAppuser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommunity>>): void {
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

  protected updateForm(community: ICommunity): void {
    this.editForm.patchValue({
      id: community.id,
      creationDate: community.creationDate ? community.creationDate.format(DATE_TIME_FORMAT) : null,
      communityName: community.communityName,
      communityDescription: community.communityDescription,
      image: community.image,
      imageContentType: community.imageContentType,
      isActive: community.isActive,
      appuser: community.appuser,
    });

    this.appusersSharedCollection = this.appuserService.addAppuserToCollectionIfMissing(this.appusersSharedCollection, community.appuser);
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

  protected createFromForm(): ICommunity {
    return {
      ...new Community(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      communityName: this.editForm.get(['communityName'])!.value,
      communityDescription: this.editForm.get(['communityDescription'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      appuser: this.editForm.get(['appuser'])!.value,
    };
  }
}
