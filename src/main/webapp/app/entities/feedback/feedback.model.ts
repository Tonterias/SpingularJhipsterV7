import * as dayjs from 'dayjs';

export interface IFeedback {
  id?: number;
  creationDate?: dayjs.Dayjs;
  name?: string;
  email?: string;
  feedback?: string;
}

export class Feedback implements IFeedback {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public name?: string,
    public email?: string,
    public feedback?: string
  ) {}
}

export function getFeedbackIdentifier(feedback: IFeedback): number | undefined {
  return feedback.id;
}
