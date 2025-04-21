import {
  Component,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { DialogService } from './services/dialog.service';
import { Subscription } from 'rxjs';
import { AplazoButtonComponent } from '@apz/shared-ui/button';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,AplazoButtonComponent],
  template: `<router-outlet></router-outlet>
    <dialog
      #dialogRefElement
      class="max-w-screen-xl border rounded-t-lg ease-out duration-300"
    >
      <div
        class="relative z-10 ease-out duration-300"
        aria-labelledby="modal-title"
        role="dialog"
        aria-modal="true"
      >
        <div
          class="fixed inset-0 bg-gray-500/75 transition-opacity"
          aria-hidden="true"
        ></div>

        <div class="fixed inset-0 z-10 w-screen overflow-y-auto">
          <div
            class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0"
          >
            <div
              class="relative transform overflow-hidden rounded-lg bg-white text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg"
            >
              <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                <div class="sm:flex sm:items-start">
                  <div
                    class="mx-auto flex size-12 shrink-0 items-center justify-center rounded-full bg-red-100 sm:mx-0 sm:size-10"
                  >
                    <svg
                      class="size-6 text-red-600"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke-width="1.5"
                      stroke="currentColor"
                      aria-hidden="true"
                      data-slot="icon"
                    >
                      <path
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126ZM12 15.75h.007v.008H12v-.008Z"
                      />
                    </svg>
                  </div>
                  <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left">
                    <h3
                      class="text-base font-semibold text-gray-900"
                      id="modal-title"
                    >
                      {{ dialog.title }}
                    </h3>
                    <div class="mt-2">
                      <p class="text-sm text-gray-500">
                        {{ dialog.description }}
                      </p>
                    </div>
                  </div>
                </div>
              </div>
              <div
                class="bg-gray-50 px-4 py-3 sm:flex sm:flex-row-reverse sm:px-6"
              >
                <button
                  (click)="close()"
                  aplzButton
                  aplzAppearance="stroked"
                  aplzColor="dark"
                  size="md"
                  type="submit"
                >
                  Cerrar
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </dialog> `,
})
export class AppComponent implements OnInit, OnDestroy {
  @ViewChild('dialogRefElement')
  dialogRefElement: ElementRef<HTMLDialogElement>;
  readonly #dialogService: DialogService = inject(DialogService);
  #dialog: Subscription;
  dialog: {
    show: boolean;
    title: string;
    description: string;
  };

  close() {
    this.dialogRefElement?.nativeElement?.close();
  }

  ngOnInit(): void {
    this.#dialog = this.#dialogService.getDialog().subscribe((dialog) => {
      this.dialog = dialog;
      if (dialog.show) {
        this.dialogRefElement.nativeElement.open = true;
      }
    });
  }

  ngOnDestroy(): void {
    this.#dialog?.unsubscribe();
  }
}
