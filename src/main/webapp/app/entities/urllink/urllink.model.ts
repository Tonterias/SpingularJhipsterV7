export interface IUrllink {
  id?: number;
  linkText?: string;
  linkURL?: string;
}

export class Urllink implements IUrllink {
  constructor(public id?: number, public linkText?: string, public linkURL?: string) {}
}

export function getUrllinkIdentifier(urllink: IUrllink): number | undefined {
  return urllink.id;
}
