import { ICommunity } from 'app/entities/community/community.model';

export interface ICinterest {
  id?: number;
  interestName?: string;
  communities?: ICommunity[] | null;
}

export class Cinterest implements ICinterest {
  constructor(public id?: number, public interestName?: string, public communities?: ICommunity[] | null) {}
}

export function getCinterestIdentifier(cinterest: ICinterest): number | undefined {
  return cinterest.id;
}
