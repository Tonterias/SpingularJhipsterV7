import { ICommunity } from 'app/entities/community/community.model';

export interface ICceleb {
  id?: number;
  celebName?: string;
  communities?: ICommunity[] | null;
}

export class Cceleb implements ICceleb {
  constructor(public id?: number, public celebName?: string, public communities?: ICommunity[] | null) {}
}

export function getCcelebIdentifier(cceleb: ICceleb): number | undefined {
  return cceleb.id;
}
