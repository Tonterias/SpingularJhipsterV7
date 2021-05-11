import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBlockuser } from '../blockuser.model';

@Component({
  selector: 'jhi-blockuser-detail',
  templateUrl: './blockuser-detail.component.html',
})
export class BlockuserDetailComponent implements OnInit {
  blockuser: IBlockuser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blockuser }) => {
      this.blockuser = blockuser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
