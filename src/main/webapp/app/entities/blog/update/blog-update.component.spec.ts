jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BlogService } from '../service/blog.service';
import { IBlog, Blog } from '../blog.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { BlogUpdateComponent } from './blog-update.component';

describe('Component Tests', () => {
  describe('Blog Management Update Component', () => {
    let comp: BlogUpdateComponent;
    let fixture: ComponentFixture<BlogUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let blogService: BlogService;
    let appuserService: AppuserService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BlogUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BlogUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BlogUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      blogService = TestBed.inject(BlogService);
      appuserService = TestBed.inject(AppuserService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const blog: IBlog = { id: 456 };
        const appuser: IAppuser = { id: 19408 };
        blog.appuser = appuser;

        const appuserCollection: IAppuser[] = [{ id: 27496 }];
        spyOn(appuserService, 'query').and.returnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [appuser];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        spyOn(appuserService, 'addAppuserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ blog });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Community query and add missing value', () => {
        const blog: IBlog = { id: 456 };
        const community: ICommunity = { id: 88073 };
        blog.community = community;

        const communityCollection: ICommunity[] = [{ id: 65234 }];
        spyOn(communityService, 'query').and.returnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [community];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        spyOn(communityService, 'addCommunityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ blog });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const blog: IBlog = { id: 456 };
        const appuser: IAppuser = { id: 13064 };
        blog.appuser = appuser;
        const community: ICommunity = { id: 84765 };
        blog.community = community;

        activatedRoute.data = of({ blog });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(blog));
        expect(comp.appusersSharedCollection).toContain(appuser);
        expect(comp.communitiesSharedCollection).toContain(community);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blog = { id: 123 };
        spyOn(blogService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: blog }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(blogService.update).toHaveBeenCalledWith(blog);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blog = new Blog();
        spyOn(blogService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: blog }));
        saveSubject.complete();

        // THEN
        expect(blogService.create).toHaveBeenCalledWith(blog);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blog = { id: 123 };
        spyOn(blogService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(blogService.update).toHaveBeenCalledWith(blog);
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

      describe('trackCommunityById', () => {
        it('Should return tracked Community primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommunityById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
