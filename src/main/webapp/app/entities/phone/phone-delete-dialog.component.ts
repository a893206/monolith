import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPhone } from 'app/shared/model/phone.model';
import { PhoneService } from './phone.service';

@Component({
  templateUrl: './phone-delete-dialog.component.html',
})
export class PhoneDeleteDialogComponent {
  phone?: IPhone;

  constructor(protected phoneService: PhoneService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.phoneService.delete(id).subscribe(() => {
      this.eventManager.broadcast('phoneListModification');
      this.activeModal.close();
    });
  }
}
