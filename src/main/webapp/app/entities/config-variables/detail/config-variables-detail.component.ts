import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConfigVariables } from '../config-variables.model';

@Component({
  selector: 'jhi-config-variables-detail',
  templateUrl: './config-variables-detail.component.html',
})
export class ConfigVariablesDetailComponent implements OnInit {
  configVariables: IConfigVariables | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configVariables }) => {
      this.configVariables = configVariables;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
