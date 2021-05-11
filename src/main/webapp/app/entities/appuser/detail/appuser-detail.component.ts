import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppuser } from '../appuser.model';

@Component({
  selector: 'jhi-appuser-detail',
  templateUrl: './appuser-detail.component.html',
})
export class AppuserDetailComponent implements OnInit {
  appuser: IAppuser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appuser }) => {
      this.appuser = appuser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
