import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUrllink, Urllink } from '../urllink.model';
import { UrllinkService } from '../service/urllink.service';

@Component({
  selector: 'jhi-urllink-update',
  templateUrl: './urllink-update.component.html',
})
export class UrllinkUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    linkText: [null, [Validators.required]],
    linkURL: [null, [Validators.required]],
  });

  constructor(protected urllinkService: UrllinkService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ urllink }) => {
      this.updateForm(urllink);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const urllink = this.createFromForm();
    if (urllink.id !== undefined) {
      this.subscribeToSaveResponse(this.urllinkService.update(urllink));
    } else {
      this.subscribeToSaveResponse(this.urllinkService.create(urllink));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUrllink>>): void {
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

  protected updateForm(urllink: IUrllink): void {
    this.editForm.patchValue({
      id: urllink.id,
      linkText: urllink.linkText,
      linkURL: urllink.linkURL,
    });
  }

  protected createFromForm(): IUrllink {
    return {
      ...new Urllink(),
      id: this.editForm.get(['id'])!.value,
      linkText: this.editForm.get(['linkText'])!.value,
      linkURL: this.editForm.get(['linkURL'])!.value,
    };
  }
}
