import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICeleb } from '../celeb.model';

@Component({
  selector: 'jhi-celeb-detail',
  templateUrl: './celeb-detail.component.html',
})
export class CelebDetailComponent implements OnInit {
  celeb: ICeleb | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ celeb }) => {
      this.celeb = celeb;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
