import * as dayjs from 'dayjs';
import { IPost } from 'app/entities/post/post.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { ICommunity } from 'app/entities/community/community.model';

export interface IBlog {
  id?: number;
  creationDate?: dayjs.Dayjs;
  title?: string;
  imageContentType?: string | null;
  image?: string | null;
  posts?: IPost[] | null;
  appuser?: IAppuser;
  community?: ICommunity | null;
}

export class Blog implements IBlog {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public title?: string,
    public imageContentType?: string | null,
    public image?: string | null,
    public posts?: IPost[] | null,
    public appuser?: IAppuser,
    public community?: ICommunity | null
  ) {}
}

export function getBlogIdentifier(blog: IBlog): number | undefined {
  return blog.id;
}
