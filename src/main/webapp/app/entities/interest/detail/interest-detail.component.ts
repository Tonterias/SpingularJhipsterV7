import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInterest } from '../interest.model';

@Component({
  selector: 'jhi-interest-detail',
  templateUrl: './interest-detail.component.html',
})
export class InterestDetailComponent implements OnInit {
  interest: IInterest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ interest }) => {
      this.interest = interest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
