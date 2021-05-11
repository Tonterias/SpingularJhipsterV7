import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { IPost } from 'app/entities/post/post.model';

export interface IComment {
  id?: number;
  creationDate?: dayjs.Dayjs;
  commentText?: string;
  isOffensive?: boolean | null;
  appuser?: IAppuser;
  post?: IPost;
}

export class Comment implements IComment {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public commentText?: string,
    public isOffensive?: boolean | null,
    public appuser?: IAppuser,
    public post?: IPost
  ) {
    this.isOffensive = this.isOffensive ?? false;
  }
}

export function getCommentIdentifier(comment: IComment): number | undefined {
  return comment.id;
}
