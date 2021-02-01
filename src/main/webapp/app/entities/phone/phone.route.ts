import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPhone, Phone } from 'app/shared/model/phone.model';
import { PhoneService } from './phone.service';
import { PhoneComponent } from './phone.component';
import { PhoneDetailComponent } from './phone-detail.component';
import { PhoneUpdateComponent } from './phone-update.component';

@Injectable({ providedIn: 'root' })
export class PhoneResolve implements Resolve<IPhone> {
  constructor(private service: PhoneService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPhone> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((phone: HttpResponse<Phone>) => {
          if (phone.body) {
            return of(phone.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Phone());
  }
}

export const phoneRoute: Routes = [
  {
    path: '',
    component: PhoneComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'monolithApp.phone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PhoneDetailComponent,
    resolve: {
      phone: PhoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'monolithApp.phone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PhoneUpdateComponent,
    resolve: {
      phone: PhoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'monolithApp.phone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PhoneUpdateComponent,
    resolve: {
      phone: PhoneResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'monolithApp.phone.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
