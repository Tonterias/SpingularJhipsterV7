jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TopicService } from '../service/topic.service';
import { ITopic, Topic } from '../topic.model';
import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';

import { TopicUpdateComponent } from './topic-update.component';

describe('Component Tests', () => {
  describe('Topic Management Update Component', () => {
    let comp: TopicUpdateComponent;
    let fixture: ComponentFixture<TopicUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let topicService: TopicService;
    let postService: PostService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TopicUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TopicUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TopicUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      topicService = TestBed.inject(TopicService);
      postService = TestBed.inject(PostService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Post query and add missing value', () => {
        const topic: ITopic = { id: 456 };
        const posts: IPost[] = [{ id: 4802 }];
        topic.posts = posts;

        const postCollection: IPost[] = [{ id: 29462 }];
        spyOn(postService, 'query').and.returnValue(of(new HttpResponse({ body: postCollection })));
        const additionalPosts = [...posts];
        const expectedCollection: IPost[] = [...additionalPosts, ...postCollection];
        spyOn(postService, 'addPostToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ topic });
        comp.ngOnInit();

        expect(postService.query).toHaveBeenCalled();
        expect(postService.addPostToCollectionIfMissing).toHaveBeenCalledWith(postCollection, ...additionalPosts);
        expect(comp.postsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const topic: ITopic = { id: 456 };
        const posts: IPost = { id: 36928 };
        topic.posts = [posts];

        activatedRoute.data = of({ topic });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(topic));
        expect(comp.postsSharedCollection).toContain(posts);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const topic = { id: 123 };
        spyOn(topicService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ topic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: topic }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(topicService.update).toHaveBeenCalledWith(topic);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const topic = new Topic();
        spyOn(topicService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ topic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: topic }));
        saveSubject.complete();

        // THEN
        expect(topicService.create).toHaveBeenCalledWith(topic);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const topic = { id: 123 };
        spyOn(topicService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ topic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(topicService.update).toHaveBeenCalledWith(topic);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPostById', () => {
        it('Should return tracked Post primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPostById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPost', () => {
        it('Should return option if no Post is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPost(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Post for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPost(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Post is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPost(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
