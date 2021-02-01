import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPhone, Phone } from 'app/shared/model/phone.model';
import { PhoneService } from './phone.service';

@Component({
  selector: 'jhi-phone-update',
  templateUrl: './phone-update.component.html',
})
export class PhoneUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(0), Validators.maxLength(20)]],
    price: [null, [Validators.required, Validators.min(0)]],
    os: [],
    promoting: [],
  });

  constructor(protected phoneService: PhoneService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ phone }) => {
      this.updateForm(phone);
    });
  }

  updateForm(phone: IPhone): void {
    this.editForm.patchValue({
      id: phone.id,
      name: phone.name,
      price: phone.price,
      os: phone.os,
      promoting: phone.promoting,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const phone = this.createFromForm();
    if (phone.id !== undefined) {
      this.subscribeToSaveResponse(this.phoneService.update(phone));
    } else {
      this.subscribeToSaveResponse(this.phoneService.create(phone));
    }
  }

  private createFromForm(): IPhone {
    return {
      ...new Phone(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      price: this.editForm.get(['price'])!.value,
      os: this.editForm.get(['os'])!.value,
      promoting: this.editForm.get(['promoting'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhone>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
