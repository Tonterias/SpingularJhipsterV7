import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComment } from '../comment.model';

@Component({
  selector: 'jhi-comment-detail',
  templateUrl: './comment-detail.component.html',
})
export class CommentDetailComponent implements OnInit {
  comment: IComment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.comment = comment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
