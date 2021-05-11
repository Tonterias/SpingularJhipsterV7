import { IAppuser } from 'app/entities/appuser/appuser.model';

export interface IInterest {
  id?: number;
  interestName?: string;
  appusers?: IAppuser[] | null;
}

export class Interest implements IInterest {
  constructor(public id?: number, public interestName?: string, public appusers?: IAppuser[] | null) {}
}

export function getInterestIdentifier(interest: IInterest): number | undefined {
  return interest.id;
}
