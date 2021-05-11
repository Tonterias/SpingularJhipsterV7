import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICactivity } from '../cactivity.model';

@Component({
  selector: 'jhi-cactivity-detail',
  templateUrl: './cactivity-detail.component.html',
})
export class CactivityDetailComponent implements OnInit {
  cactivity: ICactivity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cactivity }) => {
      this.cactivity = cactivity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
