import * as dayjs from 'dayjs';
import { IComment } from 'app/entities/comment/comment.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { IBlog } from 'app/entities/blog/blog.model';
import { ITag } from 'app/entities/tag/tag.model';
import { ITopic } from 'app/entities/topic/topic.model';

export interface IPost {
  id?: number;
  creationDate?: dayjs.Dayjs;
  publicationDate?: dayjs.Dayjs | null;
  headline?: string;
  leadtext?: string | null;
  bodytext?: string;
  quote?: string | null;
  conclusion?: string | null;
  linkText?: string | null;
  linkURL?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  comments?: IComment[] | null;
  appuser?: IAppuser;
  blog?: IBlog;
  tags?: ITag[] | null;
  topics?: ITopic[] | null;
}

export class Post implements IPost {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public publicationDate?: dayjs.Dayjs | null,
    public headline?: string,
    public leadtext?: string | null,
    public bodytext?: string,
    public quote?: string | null,
    public conclusion?: string | null,
    public linkText?: string | null,
    public linkURL?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public comments?: IComment[] | null,
    public appuser?: IAppuser,
    public blog?: IBlog,
    public tags?: ITag[] | null,
    public topics?: ITopic[] | null
  ) {}
}

export function getPostIdentifier(post: IPost): number | undefined {
  return post.id;
}
