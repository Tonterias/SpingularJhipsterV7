import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICinterest } from '../cinterest.model';

@Component({
  selector: 'jhi-cinterest-detail',
  templateUrl: './cinterest-detail.component.html',
})
export class CinterestDetailComponent implements OnInit {
  cinterest: ICinterest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cinterest }) => {
      this.cinterest = cinterest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
