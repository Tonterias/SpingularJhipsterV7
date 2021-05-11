jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FollowService } from '../service/follow.service';
import { IFollow, Follow } from '../follow.model';
import { IAppuser } from 'app/entities/appuser/appuser.model';
import { AppuserService } from 'app/entities/appuser/service/appuser.service';
import { ICommunity } from 'app/entities/community/community.model';
import { CommunityService } from 'app/entities/community/service/community.service';

import { FollowUpdateComponent } from './follow-update.component';

describe('Component Tests', () => {
  describe('Follow Management Update Component', () => {
    let comp: FollowUpdateComponent;
    let fixture: ComponentFixture<FollowUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let followService: FollowService;
    let appuserService: AppuserService;
    let communityService: CommunityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FollowUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FollowUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FollowUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      followService = TestBed.inject(FollowService);
      appuserService = TestBed.inject(AppuserService);
      communityService = TestBed.inject(CommunityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Appuser query and add missing value', () => {
        const follow: IFollow = { id: 456 };
        const followed: IAppuser = { id: 40772 };
        follow.followed = followed;
        const following: IAppuser = { id: 70443 };
        follow.following = following;

        const appuserCollection: IAppuser[] = [{ id: 77950 }];
        spyOn(appuserService, 'query').and.returnValue(of(new HttpResponse({ body: appuserCollection })));
        const additionalAppusers = [followed, following];
        const expectedCollection: IAppuser[] = [...additionalAppusers, ...appuserCollection];
        spyOn(appuserService, 'addAppuserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ follow });
        comp.ngOnInit();

        expect(appuserService.query).toHaveBeenCalled();
        expect(appuserService.addAppuserToCollectionIfMissing).toHaveBeenCalledWith(appuserCollection, ...additionalAppusers);
        expect(comp.appusersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Community query and add missing value', () => {
        const follow: IFollow = { id: 456 };
        const cfollowed: ICommunity = { id: 69847 };
        follow.cfollowed = cfollowed;
        const cfollowing: ICommunity = { id: 69622 };
        follow.cfollowing = cfollowing;

        const communityCollection: ICommunity[] = [{ id: 62570 }];
        spyOn(communityService, 'query').and.returnValue(of(new HttpResponse({ body: communityCollection })));
        const additionalCommunities = [cfollowed, cfollowing];
        const expectedCollection: ICommunity[] = [...additionalCommunities, ...communityCollection];
        spyOn(communityService, 'addCommunityToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ follow });
        comp.ngOnInit();

        expect(communityService.query).toHaveBeenCalled();
        expect(communityService.addCommunityToCollectionIfMissing).toHaveBeenCalledWith(communityCollection, ...additionalCommunities);
        expect(comp.communitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const follow: IFollow = { id: 456 };
        const followed: IAppuser = { id: 44104 };
        follow.followed = followed;
        const following: IAppuser = { id: 70572 };
        follow.following = following;
        const cfollowed: ICommunity = { id: 52276 };
        follow.cfollowed = cfollowed;
        const cfollowing: ICommunity = { id: 80931 };
        follow.cfollowing = cfollowing;

        activatedRoute.data = of({ follow });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(follow));
        expect(comp.appusersSharedCollection).toContain(followed);
        expect(comp.appusersSharedCollection).toContain(following);
        expect(comp.communitiesSharedCollection).toContain(cfollowed);
        expect(comp.communitiesSharedCollection).toContain(cfollowing);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const follow = { id: 123 };
        spyOn(followService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ follow });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: follow }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(followService.update).toHaveBeenCalledWith(follow);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const follow = new Follow();
        spyOn(followService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ follow });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: follow }));
        saveSubject.complete();

        // THEN
        expect(followService.create).toHaveBeenCalledWith(follow);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const follow = { id: 123 };
        spyOn(followService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ follow });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(followService.update).toHaveBeenCalledWith(follow);
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
