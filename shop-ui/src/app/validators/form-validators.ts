import { FormControl, ValidationErrors } from '@angular/forms';

export class FormValidators {
  static notOnlyWhitespace(control: FormControl): ValidationErrors | null {
    const value = control.value;
    if (value != null && value.trim().length === 0) {
      return { notOnlyWhitespace: true };
    }

    return null;
  }
}
