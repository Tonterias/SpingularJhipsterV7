import { ICommunity } from 'app/entities/community/community.model';

export interface ICactivity {
  id?: number;
  activityName?: string;
  communities?: ICommunity[] | null;
}

export class Cactivity implements ICactivity {
  constructor(public id?: number, public activityName?: string, public communities?: ICommunity[] | null) {}
}

export function getCactivityIdentifier(cactivity: ICactivity): number | undefined {
  return cactivity.id;
}
