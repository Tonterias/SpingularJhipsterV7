jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PostService } from '../service/post.service';
import { IPost, Post } from '../post.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IBlog } from 'app/entities/blog/blog.model';
import { BlogService } from 'app/entities/blog/service/blog.service';

import { PostUpdateComponent } from './post-update.component';

describe('Component Tests', () => {
  describe('Post Management Update Component', () => {
    let comp: PostUpdateComponent;
    let fixture: ComponentFixture<PostUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let postService: PostService;
    let appuserService: AppuserService;
    let blogService: BlogService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PostUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PostUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PostUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      postService = TestBed.inject(PostService);
      appuserService = TestBed.inject(AppuserService);
      blogService = TestBed.inject(BlogService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const post: IPost = { id: 456 };
        const appuser: IAppuser = { id: 28360 };
        post.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 8446 }];
        spyOn(appuserService, 'query').and.returnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        spyOn(appuserService, 'addAppuserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ post });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Blog query and add missing value', () => {
        const post: IPost = { id: 456 };
        const blog: IBlog = { id: 48738 };
        post.blog = blog;

        const blogCollection: IBlog[] = [{ id: 41206 }];
        spyOn(blogService, 'query').and.returnValue(of(new HttpResponse({ body: blogCollection })));
        const additionalBlogs = [blog];
        const expectedCollection: IBlog[] = [...additionalBlogs, ...blogCollection];
        spyOn(blogService, 'addBlogToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ post });
        comp.ngOnInit();

        expect(blogService.query).toHaveBeenCalled();
        expect(blogService.addBlogToCollectionIfMissing).toHaveBeenCalledWith(blogCollection, ...additionalBlogs);
        expect(comp.blogsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const post: IPost = { id: 456 };
        const appuser: IAppuser = { id: 25755 };
        post.appuser = appuser;
        const blog: IBlog = { id: 61570 };
        post.blog = blog;

        activatedRoute.data = of({ post });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(post));
        expect(comp.appusersSharedCollection).toContain(appuser);
        expect(comp.blogsSharedCollection).toContain(blog);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const post = { id: 123 };
        spyOn(postService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ post });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: post }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(postService.update).toHaveBeenCalledWith(post);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const post = new Post();
        spyOn(postService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ post });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: post }));
        saveSubject.complete();

        // THEN
        expect(postService.create).toHaveBeenCalledWith(post);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const post = { id: 123 };
        spyOn(postService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ post });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(postService.update).toHaveBeenCalledWith(post);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAppuserById', () => {
        it('Should return tracked Appuser primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAppuserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBlogById', () => {
        it('Should return tracked Blog primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBlogById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
