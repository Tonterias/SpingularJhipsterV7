import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAppuser, Appuser } from '../appuser.model';
import { AppuserService } from '../service/appuser.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-appuser-update',
  templateUrl: './appuser-update.component.html',
})
export class AppuserUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    bio: [null, [Validators.maxLength(7500)]],
    facebook: [null, [Validators.maxLength(50)]],
    twitter: [null, [Validators.maxLength(50)]],
    linkedin: [null, [Validators.maxLength(50)]],
    instagram: [null, [Validators.maxLength(50)]],
    birthdate: [],
    user: [null, Validators.required],
  });

  constructor(
    protected appuserService: AppuserService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appuser }) => {
      if (appuser.id === undefined) {
        const today = dayjs().startOf('day');
        appuser.creationDate = today;
        appuser.birthdate = today;
      }

      this.updateForm(appuser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appuser = this.createFromForm();
    if (appuser.id !== undefined) {
      this.subscribeToSaveResponse(this.appuserService.update(appuser));
    } else {
      this.subscribeToSaveResponse(this.appuserService.create(appuser));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppuser>>): void {
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

  protected updateForm(appuser: IAppuser): void {
    this.editForm.patchValue({
      id: appuser.id,
      creationDate: appuser.creationDate ? appuser.creationDate.format(DATE_TIME_FORMAT) : null,
      bio: appuser.bio,
      facebook: appuser.facebook,
      twitter: appuser.twitter,
      linkedin: appuser.linkedin,
      instagram: appuser.instagram,
      birthdate: appuser.birthdate ? appuser.birthdate.format(DATE_TIME_FORMAT) : null,
      user: appuser.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, appuser.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IAppuser {
    return {
      ...new Appuser(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      bio: this.editForm.get(['bio'])!.value,
      facebook: this.editForm.get(['facebook'])!.value,
      twitter: this.editForm.get(['twitter'])!.value,
      linkedin: this.editForm.get(['linkedin'])!.value,
      instagram: this.editForm.get(['instagram'])!.value,
      birthdate: this.editForm.get(['birthdate'])!.value ? dayjs(this.editForm.get(['birthdate'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
