import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  #dialog: BehaviorSubject<{
    show: boolean;
    title: string;
    description: string;
  }> = new BehaviorSubject<{
    show: boolean;
    title: string;
    description: string;
  }>({ show: false, title: '', description: '' });

  getDialog(): Observable<{
    show: boolean;
    title: string;
    description: string;
  }> {
    return this.#dialog.asObservable();
  }

  setDialog(dialog: {
    show: boolean;
    title: string;
    description: string;
  }): void {
    this.#dialog.next(dialog);
  }
}
