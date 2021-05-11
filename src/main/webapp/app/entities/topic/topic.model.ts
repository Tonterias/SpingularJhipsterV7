import { IPost } from 'app/entities/post/post.model';

export interface ITopic {
  id?: number;
  topicName?: string;
  posts?: IPost[] | null;
}

export class Topic implements ITopic {
  constructor(public id?: number, public topicName?: string, public posts?: IPost[] | null) {}
}

export function getTopicIdentifier(topic: ITopic): number | undefined {
  return topic.id;
}
