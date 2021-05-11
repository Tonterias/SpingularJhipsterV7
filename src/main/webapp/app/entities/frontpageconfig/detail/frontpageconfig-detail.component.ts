import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFrontpageconfig } from '../frontpageconfig.model';

@Component({
  selector: 'jhi-frontpageconfig-detail',
  templateUrl: './frontpageconfig-detail.component.html',
})
export class FrontpageconfigDetailComponent implements OnInit {
  frontpageconfig: IFrontpageconfig | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frontpageconfig }) => {
      this.frontpageconfig = frontpageconfig;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
