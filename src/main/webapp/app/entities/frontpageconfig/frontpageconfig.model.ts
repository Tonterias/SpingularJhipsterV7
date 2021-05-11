import * as dayjs from 'dayjs';

export interface IFrontpageconfig {
  id?: number;
  creationDate?: dayjs.Dayjs;
  topNews1?: number | null;
  topNews2?: number | null;
  topNews3?: number | null;
  topNews4?: number | null;
  topNews5?: number | null;
  latestNews1?: number | null;
  latestNews2?: number | null;
  latestNews3?: number | null;
  latestNews4?: number | null;
  latestNews5?: number | null;
  breakingNews1?: number | null;
  recentPosts1?: number | null;
  recentPosts2?: number | null;
  recentPosts3?: number | null;
  recentPosts4?: number | null;
  featuredArticles1?: number | null;
  featuredArticles2?: number | null;
  featuredArticles3?: number | null;
  featuredArticles4?: number | null;
  featuredArticles5?: number | null;
  featuredArticles6?: number | null;
  featuredArticles7?: number | null;
  featuredArticles8?: number | null;
  featuredArticles9?: number | null;
  featuredArticles10?: number | null;
  popularNews1?: number | null;
  popularNews2?: number | null;
  popularNews3?: number | null;
  popularNews4?: number | null;
  popularNews5?: number | null;
  popularNews6?: number | null;
  popularNews7?: number | null;
  popularNews8?: number | null;
  weeklyNews1?: number | null;
  weeklyNews2?: number | null;
  weeklyNews3?: number | null;
  weeklyNews4?: number | null;
  newsFeeds1?: number | null;
  newsFeeds2?: number | null;
  newsFeeds3?: number | null;
  newsFeeds4?: number | null;
  newsFeeds5?: number | null;
  newsFeeds6?: number | null;
  usefulLinks1?: number | null;
  usefulLinks2?: number | null;
  usefulLinks3?: number | null;
  usefulLinks4?: number | null;
  usefulLinks5?: number | null;
  usefulLinks6?: number | null;
  recentVideos1?: number | null;
  recentVideos2?: number | null;
  recentVideos3?: number | null;
  recentVideos4?: number | null;
  recentVideos5?: number | null;
  recentVideos6?: number | null;
}

export class Frontpageconfig implements IFrontpageconfig {
  constructor(
    public id?: number,
    public creationDate?: dayjs.Dayjs,
    public topNews1?: number | null,
    public topNews2?: number | null,
    public topNews3?: number | null,
    public topNews4?: number | null,
    public topNews5?: number | null,
    public latestNews1?: number | null,
    public latestNews2?: number | null,
    public latestNews3?: number | null,
    public latestNews4?: number | null,
    public latestNews5?: number | null,
    public breakingNews1?: number | null,
    public recentPosts1?: number | null,
    public recentPosts2?: number | null,
    public recentPosts3?: number | null,
    public recentPosts4?: number | null,
    public featuredArticles1?: number | null,
    public featuredArticles2?: number | null,
    public featuredArticles3?: number | null,
    public featuredArticles4?: number | null,
    public featuredArticles5?: number | null,
    public featuredArticles6?: number | null,
    public featuredArticles7?: number | null,
    public featuredArticles8?: number | null,
    public featuredArticles9?: number | null,
    public featuredArticles10?: number | null,
    public popularNews1?: number | null,
    public popularNews2?: number | null,
    public popularNews3?: number | null,
    public popularNews4?: number | null,
    public popularNews5?: number | null,
    public popularNews6?: number | null,
    public popularNews7?: number | null,
    public popularNews8?: number | null,
    public weeklyNews1?: number | null,
    public weeklyNews2?: number | null,
    public weeklyNews3?: number | null,
    public weeklyNews4?: number | null,
    public newsFeeds1?: number | null,
    public newsFeeds2?: number | null,
    public newsFeeds3?: number | null,
    public newsFeeds4?: number | null,
    public newsFeeds5?: number | null,
    public newsFeeds6?: number | null,
    public usefulLinks1?: number | null,
    public usefulLinks2?: number | null,
    public usefulLinks3?: number | null,
    public usefulLinks4?: number | null,
    public usefulLinks5?: number | null,
    public usefulLinks6?: number | null,
    public recentVideos1?: number | null,
    public recentVideos2?: number | null,
    public recentVideos3?: number | null,
    public recentVideos4?: number | null,
    public recentVideos5?: number | null,
    public recentVideos6?: number | null
  ) {}
}

export function getFrontpageconfigIdentifier(frontpageconfig: IFrontpageconfig): number | undefined {
  return frontpageconfig.id;
}
