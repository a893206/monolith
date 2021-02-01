import { Os } from 'app/shared/model/enumerations/os.model';

export interface IPhone {
  id?: number;
  name?: string;
  price?: number;
  os?: Os;
  promoting?: boolean;
}

export class Phone implements IPhone {
  constructor(public id?: number, public name?: string, public price?: number, public os?: Os, public promoting?: boolean) {
    this.promoting = this.promoting || false;
  }
}
