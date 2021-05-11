import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFeedback } from '../feedback.model';

@Component({
  selector: 'jhi-feedback-detail',
  templateUrl: './feedback-detail.component.html',
})
export class FeedbackDetailComponent implements OnInit {
  feedback: IFeedback | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedback }) => {
      this.feedback = feedback;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
