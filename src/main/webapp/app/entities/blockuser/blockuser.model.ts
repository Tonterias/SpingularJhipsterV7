import * as dayjs from 'dayjs';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { ICommunity } from 'app/entities/community/community.model';

export interface IBlockuser {
  id?: number;
  creationDate?: dayjs.Dayjs | null;
  blockeduser?: IAppuser | null;
  blockinguser?: IAppuser | null;
  cblockeduser?: ICommunity | null;
  cblockinguser?: ICommunity | null;
}

export class Blockuser implements IBlockuser {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs | null,
    public blockeduser?: IAppuser | null,
    public blockinguser?: IAppuser | null,
    public cblockeduser?: ICommunity | null,
    public cblockinguser?: ICommunity | null
  ) {}
}

export function getBlockuserIdentifier(blockuser: IBlockuser): number | undefined {
  return blockuser.id;
}
