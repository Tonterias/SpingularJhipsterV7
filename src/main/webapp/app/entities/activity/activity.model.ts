import { IAppuser } from 'app/entities/appuser/appuser.model';

export interface IActivity {
  id?: number;
  activityName?: string;
  appusers?: IAppuser[] | null;
}

export class Activity implements IActivity {
  constructor(public id?: number, public activityName?: string, public appusers?: IAppuser[] | null) {}
}

export function getActivityIdentifier(activity: IActivity): number | undefined {
  return activity.id;
}
