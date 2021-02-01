import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MonolithSharedModule } from 'app/shared/shared.module';
import { PhoneComponent } from './phone.component';
import { PhoneDetailComponent } from './phone-detail.component';
import { PhoneUpdateComponent } from './phone-update.component';
import { PhoneDeleteDialogComponent } from './phone-delete-dialog.component';
import { phoneRoute } from './phone.route';

@NgModule({
  imports: [MonolithSharedModule, RouterModule.forChild(phoneRoute)],
  declarations: [PhoneComponent, PhoneDetailComponent, PhoneUpdateComponent, PhoneDeleteDialogComponent],
  entryComponents: [PhoneDeleteDialogComponent],
})
export class MonolithPhoneModule {}
