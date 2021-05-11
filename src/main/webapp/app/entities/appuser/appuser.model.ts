import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IBlog } from 'app/entities/blog/blog.model';
import { ICommunity } from 'app/entities/community/community.model';
import { INotification } from 'app/entities/notification/notification.model';
import { IComment } from 'app/entities/comment/comment.model';
import { IPost } from 'app/entities/post/post.model';
import { IFollow } from 'app/entities/follow/follow.model';
import { IBlockuser } from 'app/entities/blockuser/blockuser.model';
import { IAppphoto } from 'app/entities/appphoto/appphoto.model';
import { IInterest } from 'app/entities/interest/interest.model';
import { IActivity } from 'app/entities/activity/activity.model';
import { ICeleb } from 'app/entities/celeb/celeb.model';

export interface IAppuser {
  id?: number;
  creationDate?: dayjs.Dayjs;
  bio?: string | null;
  facebook?: string | null;
  twitter?: string | null;
  linkedin?: string | null;
  instagram?: string | null;
  birthdate?: dayjs.Dayjs | null;
  user?: IUser;
  blogs?: IBlog[] | null;
  communities?: ICommunity[] | null;
  notifications?: INotification[] | null;
  comments?: IComment[] | null;
  posts?: IPost[] | null;
  followeds?: IFollow[] | null;
  followings?: IFollow[] | null;
  blockedusers?: IBlockuser[] | null;
  blockingusers?: IBlockuser[] | null;
  appphoto?: IAppphoto | null;
  interests?: IInterest[] | null;
  activities?: IActivity[] | null;
  celebs?: ICeleb[] | null;
}

export class Appuser implements IAppuser {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public bio?: string | null,
    public facebook?: string | null,
    public twitter?: string | null,
    public linkedin?: string | null,
    public instagram?: string | null,
    public birthdate?: dayjs.Dayjs | null,
    public user?: IUser,
    public blogs?: IBlog[] | null,
    public communities?: ICommunity[] | null,
    public notifications?: INotification[] | null,
    public comments?: IComment[] | null,
    public posts?: IPost[] | null,
    public followeds?: IFollow[] | null,
    public followings?: IFollow[] | null,
    public blockedusers?: IBlockuser[] | null,
    public blockingusers?: IBlockuser[] | null,
    public appphoto?: IAppphoto | null,
    public interests?: IInterest[] | null,
    public activities?: IActivity[] | null,
    public celebs?: ICeleb[] | null
  ) {}
}

export function getAppuserIdentifier(appuser: IAppuser): number | undefined {
  return appuser.id;
}
