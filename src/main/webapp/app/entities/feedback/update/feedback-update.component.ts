import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFeedback, Feedback } from '../feedback.model';
import { FeedbackService } from '../service/feedback.service';

@Component({
  selector: 'jhi-feedback-update',
  templateUrl: './feedback-update.component.html',
})
export class FeedbackUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    name: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    email: [null, [Validators.required]],
    feedback: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(5000)]],
  });

  constructor(protected feedbackService: FeedbackService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedback }) => {
      if (feedback.id === undefined) {
        const today = dayjs().startOf('day');
        feedback.creationDate = today;
      }

      this.updateForm(feedback);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedback = this.createFromForm();
    if (feedback.id !== undefined) {
      this.subscribeToSaveResponse(this.feedbackService.update(feedback));
    } else {
      this.subscribeToSaveResponse(this.feedbackService.create(feedback));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedback>>): void {
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

  protected updateForm(feedback: IFeedback): void {
    this.editForm.patchValue({
      id: feedback.id,
      creationDate: feedback.creationDate ? feedback.creationDate.format(DATE_TIME_FORMAT) : null,
      name: feedback.name,
      email: feedback.email,
      feedback: feedback.feedback,
    });
  }

  protected createFromForm(): IFeedback {
    return {
      ...new Feedback(),
      id: this.editForm.get(['id'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value
        ? dayjs(this.editForm.get(['creationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      name: this.editForm.get(['name'])!.value,
      email: this.editForm.get(['email'])!.value,
      feedback: this.editForm.get(['feedback'])!.value,
    };
  }
}
