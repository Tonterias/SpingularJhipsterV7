import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'appuser',
        data: { pageTitle: 'spingularApp.appuser.home.title' },
        loadChildren: () => import('./appuser/appuser.module').then(m => m.AppuserModule),
      },
      {
        path: 'blog',
        data: { pageTitle: 'spingularApp.blog.home.title' },
        loadChildren: () => import('./blog/blog.module').then(m => m.BlogModule),
      },
      {
        path: 'post',
        data: { pageTitle: 'spingularApp.post.home.title' },
        loadChildren: () => import('./post/post.module').then(m => m.PostModule),
      },
      {
        path: 'topic',
        data: { pageTitle: 'spingularApp.topic.home.title' },
        loadChildren: () => import('./topic/topic.module').then(m => m.TopicModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'spingularApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'comment',
        data: { pageTitle: 'spingularApp.comment.home.title' },
        loadChildren: () => import('./comment/comment.module').then(m => m.CommentModule),
      },
      {
        path: 'notification',
        data: { pageTitle: 'spingularApp.notification.home.title' },
        loadChildren: () => import('./notification/notification.module').then(m => m.NotificationModule),
      },
      {
        path: 'appphoto',
        data: { pageTitle: 'spingularApp.appphoto.home.title' },
        loadChildren: () => import('./appphoto/appphoto.module').then(m => m.AppphotoModule),
      },
      {
        path: 'community',
        data: { pageTitle: 'spingularApp.community.home.title' },
        loadChildren: () => import('./community/community.module').then(m => m.CommunityModule),
      },
      {
        path: 'follow',
        data: { pageTitle: 'spingularApp.follow.home.title' },
        loadChildren: () => import('./follow/follow.module').then(m => m.FollowModule),
      },
      {
        path: 'blockuser',
        data: { pageTitle: 'spingularApp.blockuser.home.title' },
        loadChildren: () => import('./blockuser/blockuser.module').then(m => m.BlockuserModule),
      },
      {
        path: 'interest',
        data: { pageTitle: 'spingularApp.interest.home.title' },
        loadChildren: () => import('./interest/interest.module').then(m => m.InterestModule),
      },
      {
        path: 'activity',
        data: { pageTitle: 'spingularApp.activity.home.title' },
        loadChildren: () => import('./activity/activity.module').then(m => m.ActivityModule),
      },
      {
        path: 'celeb',
        data: { pageTitle: 'spingularApp.celeb.home.title' },
        loadChildren: () => import('./celeb/celeb.module').then(m => m.CelebModule),
      },
      {
        path: 'cinterest',
        data: { pageTitle: 'spingularApp.cinterest.home.title' },
        loadChildren: () => import('./cinterest/cinterest.module').then(m => m.CinterestModule),
      },
      {
        path: 'cactivity',
        data: { pageTitle: 'spingularApp.cactivity.home.title' },
        loadChildren: () => import('./cactivity/cactivity.module').then(m => m.CactivityModule),
      },
      {
        path: 'cceleb',
        data: { pageTitle: 'spingularApp.cceleb.home.title' },
        loadChildren: () => import('./cceleb/cceleb.module').then(m => m.CcelebModule),
      },
      {
        path: 'urllink',
        data: { pageTitle: 'spingularApp.urllink.home.title' },
        loadChildren: () => import('./urllink/urllink.module').then(m => m.UrllinkModule),
      },
      {
        path: 'frontpageconfig',
        data: { pageTitle: 'spingularApp.frontpageconfig.home.title' },
        loadChildren: () => import('./frontpageconfig/frontpageconfig.module').then(m => m.FrontpageconfigModule),
      },
      {
        path: 'feedback',
        data: { pageTitle: 'spingularApp.feedback.home.title' },
        loadChildren: () => import('./feedback/feedback.module').then(m => m.FeedbackModule),
      },
      {
        path: 'config-variables',
        data: { pageTitle: 'spingularApp.configVariables.home.title' },
        loadChildren: () => import('./config-variables/config-variables.module').then(m => m.ConfigVariablesModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
