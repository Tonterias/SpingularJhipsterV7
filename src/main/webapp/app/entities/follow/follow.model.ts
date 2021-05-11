import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { ICommunity } from 'app/entities/community/community.model';

export interface IFollow {
  id?: number;
  creationDate?: dayjs.Dayjs | null;
  followed?: IAppuser | null;
  following?: IAppuser | null;
  cfollowed?: ICommunity | null;
  cfollowing?: ICommunity | null;
}

export class Follow implements IFollow {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs | null,
    public followed?: IAppuser | null,
    public following?: IAppuser | null,
    public cfollowed?: ICommunity | null,
    public cfollowing?: ICommunity | null
  ) {}
}

export function getFollowIdentifier(follow: IFollow): number | undefined {
  return follow.id;
}
