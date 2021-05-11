import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommunity } from '../community.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-community-detail',
  templateUrl: './community-detail.component.html',
})
export class CommunityDetailComponent implements OnInit {
  community: ICommunity | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ community }) => {
      this.community = community;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
