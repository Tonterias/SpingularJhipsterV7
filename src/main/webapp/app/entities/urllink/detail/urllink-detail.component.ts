import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUrllink } from '../urllink.model';

@Component({
  selector: 'jhi-urllink-detail',
  templateUrl: './urllink-detail.component.html',
})
export class UrllinkDetailComponent implements OnInit {
  urllink: IUrllink | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ urllink }) => {
      this.urllink = urllink;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
