import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICceleb } from '../cceleb.model';

@Component({
  selector: 'jhi-cceleb-detail',
  templateUrl: './cceleb-detail.component.html',
})
export class CcelebDetailComponent implements OnInit {
  cceleb: ICceleb | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cceleb }) => {
      this.cceleb = cceleb;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
