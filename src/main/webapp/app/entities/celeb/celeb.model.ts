import { IAppuser } from 'app/entities/appuser/appuser.model';

export interface ICeleb {
  id?: number;
  celebName?: string;
  appusers?: IAppuser[] | null;
}

export class Celeb implements ICeleb {
  constructor(public id?: number, public celebName?: string, public appusers?: IAppuser[] | null) {}
}

export function getCelebIdentifier(celeb: ICeleb): number | undefined {
  return celeb.id;
}
