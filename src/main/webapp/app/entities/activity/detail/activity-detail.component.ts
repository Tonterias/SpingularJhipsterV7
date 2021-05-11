import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IActivity } from '../activity.model';

@Component({
  selector: 'jhi-activity-detail',
  templateUrl: './activity-detail.component.html',
})
export class ActivityDetailComponent implements OnInit {
  activity: IActivity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activity }) => {
      this.activity = activity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
