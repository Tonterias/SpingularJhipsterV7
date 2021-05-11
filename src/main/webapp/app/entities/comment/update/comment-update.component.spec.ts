jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommentService } from '../service/comment.service';
import { IComment, Comment } from '../comment.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';

import { CommentUpdateComponent } from './comment-update.component';

describe('Component Tests', () => {
  describe('Comment Management Update Component', () => {
    let comp: CommentUpdateComponent;
    let fixture: ComponentFixture<CommentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let commentService: CommentService;
    let appuserService: AppuserService;
    let postService: PostService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      commentService = TestBed.inject(CommentService);
      appuserService = TestBed.inject(AppuserService);
      postService = TestBed.inject(PostService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const comment: IComment = { id: 456 };
        const appuser: IAppuser = { id: 95068 };
        comment.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 92557 }];
        spyOn(appuserService, 'query').and.returnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        spyOn(appuserService, 'addAppuserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Post query and add missing value', () => {
        const comment: IComment = { id: 456 };
        const post: IPost = { id: 92775 };
        comment.post = post;

        const postCollection: IPost[] = [{ id: 36085 }];
        spyOn(postService, 'query').and.returnValue(of(new HttpResponse({ body: postCollection })));
        const additionalPosts = [post];
        const expectedCollection: IPost[] = [...additionalPosts, ...postCollection];
        spyOn(postService, 'addPostToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(postService.query).toHaveBeenCalled();
        expect(postService.addPostToCollectionIfMissing).toHaveBeenCalledWith(postCollection, ...additionalPosts);
        expect(comp.postsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const comment: IComment = { id: 456 };
        const appuser: IAppuser = { id: 69823 };
        comment.appuser = appuser;
        const post: IPost = { id: 75900 };
        comment.post = post;

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(comment));
        expect(comp.appusersSharedCollection).toContain(appuser);
        expect(comp.postsSharedCollection).toContain(post);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = { id: 123 };
        spyOn(commentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(commentService.update).toHaveBeenCalledWith(comment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = new Comment();
        spyOn(commentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comment }));
        saveSubject.complete();

        // THEN
        expect(commentService.create).toHaveBeenCalledWith(comment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = { id: 123 };
        spyOn(commentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(commentService.update).toHaveBeenCalledWith(comment);
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

      describe('trackPostById', () => {
        it('Should return tracked Post primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPostById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
