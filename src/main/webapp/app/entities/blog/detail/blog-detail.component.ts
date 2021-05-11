import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBlog } from '../blog.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-blog-detail',
  templateUrl: './blog-detail.component.html',
})
export class BlogDetailComponent implements OnInit {
  blog: IBlog | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blog }) => {
      this.blog = blog;
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
