import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';

export interface IAppphoto {
  id?: number;
  creationDate?: dayjs.Dayjs;
  imageContentType?: string | null;
  image?: string | null;
  appuser?: IAppuser;
}

export class Appphoto implements IAppphoto {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public imageContentType?: string | null,
    public image?: string | null,
    public appuser?: IAppuser
  ) {}
}

export function getAppphotoIdentifier(appphoto: IAppphoto): number | undefined {
  return appphoto.id;
}
