import { Injectable } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { User } from '../../../user/models/user/user';
import { FormValidators } from '../../../validators/form-validators';

@Injectable({
  providedIn: 'root',
})
export class CheckoutFormService {
  constructor(private formBuilder: FormBuilder) {}

  createCheckoutForm(user: User): FormGroup {
    return this.formBuilder.group({
      customer: this.formBuilder.group({
        firstname: new FormControl(user.firstname, [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(13),
          FormValidators.notOnlyWhitespace,
        ]),
        lastname: new FormControl(user.lastname, [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(20),
          FormValidators.notOnlyWhitespace,
        ]),
        email: new FormControl(user.email, [
          Validators.required,
          Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$'),
        ]),
      }),
      shippingAddress: this.createAddressGroup(),
      billingAddress: this.createAddressGroup(),
    });
  }

  createAddressGroup() {
    return this.formBuilder.group({
      street: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        FormValidators.notOnlyWhitespace,
      ]),
      city: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        FormValidators.notOnlyWhitespace,
      ]),
      country: new FormControl('', [Validators.required]),
      zipCode: new FormControl('', [
        Validators.required,
        Validators.minLength(2),
        FormValidators.notOnlyWhitespace,
      ]),
    });
  }
}
