import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Country } from '../../common/country/country';
import { FormService } from '../../services/form/form.service';
import { FormValidators } from '../../validators/form-validators';
import { CartService } from '../../services/cart/cart.service';
import { CheckoutService } from '../../services/checkout/checkout.service';
import { Router } from '@angular/router';
import { Order } from '../../common/order/order';
import { OrderItem } from '../../common/order-item/order-item';
import { Purchase } from '../../common/purchase/purchase';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss',
})
export class CheckoutComponent implements OnInit {
  checkoutFormGroup!: FormGroup;

  totalPrice: number = 0;
  totalQuantity: number = 0;

  countries: Country[] = [];

  isChecked: boolean = true;
  isLoading: boolean = true;
  isCheckboxDisabled: boolean = false;

  shipping: number = 19.99;

  constructor(
    private formBuilder: FormBuilder,
    private formService: FormService,
    private cartService: CartService,
    private checkoutService: CheckoutService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.reviewCartDetails();

    this.checkoutFormGroup = this.formBuilder.group({
      customer: this.formBuilder.group({
        firstName: new FormControl('', [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(13),
          FormValidators.notOnlyWhitespace,
        ]),
        lastName: new FormControl('', [
          Validators.required,
          Validators.minLength(2),
          Validators.maxLength(20),
          FormValidators.notOnlyWhitespace,
        ]),
        email: new FormControl('', [
          Validators.required,
          Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$'),
        ]),
      }),
      shippingAddress: this.formBuilder.group({
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
        country: new FormControl('Polska (PL)', [Validators.required]),
        zipCode: new FormControl('', [
          Validators.required,
          Validators.minLength(2),
          FormValidators.notOnlyWhitespace,
        ]),
      }),
      billingAddress: this.formBuilder.group({
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
        country: new FormControl('Polska (PL)', [Validators.required]),
        zipCode: new FormControl('', [
          Validators.required,
          Validators.minLength(2),
          FormValidators.notOnlyWhitespace,
        ]),
      }),
      creditCard: this.formBuilder.group({
        cardType: [''],
        nameOnCard: [''],
        cardNumber: [''],
        securityCode: [''],
        expirationMonth: [''],
        expirationYear: [''],
      }),
    });

    this.checkoutFormGroup
      .get('billingAddress')!
      .valueChanges.subscribe((value) => {
        if (this.isChecked) {
          console.log(this.isChecked);
          this.checkoutFormGroup.get('shippingAddress')!.patchValue(value);
        }
      });

    this.formService.getCountries().subscribe((data) => {
      this.countries = data;
      this.isLoading = false;

      this.copyBillingAddressToShippingAddress({
        target: { checked: this.isChecked },
      });
    });
  }

  get firstName() {
    return this.checkoutFormGroup.get('customer.firstName');
  }

  get lastName() {
    return this.checkoutFormGroup.get('customer.lastName');
  }

  get email() {
    return this.checkoutFormGroup.get('customer.email');
  }

  get shippingAddressStreet() {
    return this.checkoutFormGroup.get('shippingAddress.street');
  }

  get shippingAddressCity() {
    return this.checkoutFormGroup.get('shippingAddress.city');
  }

  get shippingAddressCountry() {
    return this.checkoutFormGroup.get('shippingAddress.country');
  }

  get shippingAddressZipCode() {
    return this.checkoutFormGroup.get('shippingAddress.zipCode');
  }

  get billingAddressStreet() {
    return this.checkoutFormGroup.get('billingAddress.street');
  }

  get billingAddressCity() {
    return this.checkoutFormGroup.get('billingAddress.city');
  }

  get billingAddressCountry() {
    return this.checkoutFormGroup.get('billingAddress.country');
  }

  get billingAddressZipCode() {
    return this.checkoutFormGroup.get('billingAddress.zipCode');
  }

  copyBillingAddressToShippingAddress(event: any) {
    const shippingAddressContainer = document.querySelector(
      '.shippingAddressContainer'
    );

    if (event.target.checked) {
      this.isChecked = true;
      this.checkoutFormGroup.controls['shippingAddress'].setValue(
        this.checkoutFormGroup.controls['billingAddress'].value
      );
      if (!shippingAddressContainer?.classList.contains('disabled')) {
        shippingAddressContainer?.classList.add('disabled');
      }
    } else {
      this.isChecked = false;
      this.checkoutFormGroup.controls['shippingAddress'].reset();

      if (shippingAddressContainer?.classList.contains('disabled')) {
        shippingAddressContainer?.classList.remove('disabled');
      }
    }

    this.checkoutFormGroup.controls['shippingAddress']
      .get('country')
      ?.setValue('Polska (PL)');
  }

  differentCountry(country: string) {
    const shippingAddressContainer = document.querySelector(
      '.shippingAddressContainer'
    );

    if (country !== 'Polska (PL)') {
      this.isChecked = false;
      if (shippingAddressContainer?.classList.contains('disabled')) {
        shippingAddressContainer?.classList.remove('disabled');
      }
      this.isCheckboxDisabled = true;
    } else {
      this.isChecked = true;
      if (!shippingAddressContainer?.classList.contains('disabled')) {
        shippingAddressContainer?.classList.add('disabled');
      }
      this.isCheckboxDisabled = false;
    }
  }

  reviewCartDetails() {
    this.cartService.totalQuantity.subscribe(
      (data) => (this.totalQuantity = data)
    );

    this.cartService.totalPrice.subscribe((data) => (this.totalPrice = data));
  }

  onSubmit() {
    console.log(this.checkoutFormGroup.controls['billingAddress'].value);
    console.log(this.checkoutFormGroup.controls['shippingAddress'].value);

    if (this.checkoutFormGroup.invalid) {
      this.checkoutFormGroup.markAllAsTouched();
      return;
    }

    const shippingAddressContainer = document.querySelector(
      '.shippingAddressContainer'
    );

    if (shippingAddressContainer?.classList.contains('disabled')) {
      this.checkoutFormGroup.controls['shippingAddress'].setValue(
        this.checkoutFormGroup.controls['billingAddress'].value
      );

      this.shippingAddressCountry?.setValue(
        `${this.countries[0].name} (${this.countries[0].code})`
      );
    }

    let order = new Order();
    order.totalPrice = this.totalPrice;
    order.totalQuantity = this.totalQuantity;

    const cartItems = this.cartService.cartItems;

    let orderItems: OrderItem[] = cartItems.map(
      (cartItem) => new OrderItem(cartItem)
    );

    let purchase = new Purchase();

    purchase.customer = this.checkoutFormGroup.controls['customer'].value;

    purchase.shippingAddress =
      this.checkoutFormGroup.controls['shippingAddress'].value;
    const shippingCountry: Country = JSON.parse(
      JSON.stringify(purchase.shippingAddress?.country)
    );
    purchase.shippingAddress!.country = shippingCountry.name;

    purchase.billingAddress =
      this.checkoutFormGroup.controls['billingAddress'].value;
    const billingCountry: Country = JSON.parse(
      JSON.stringify(purchase.billingAddress?.country)
    );
    purchase.billingAddress!.country = billingCountry.name;

    purchase.order = order;
    purchase.orderItems = orderItems;

    console.log(purchase);

    this.checkoutService.placeOrder(purchase).subscribe({
      next: (response) => {
        alert(
          `Your order has ben received. \n Order tracking number: ${response.orderTrackingNumber}`
        );

        this.resetCart();
      },
      error: (err) => {
        alert(`There was an error: ${err.message}`);
      },
    });
  }

  resetCart() {
    this.cartService.cartItems = [];
    this.cartService.totalPrice.next(0);
    this.cartService.totalQuantity.next(0);

    this.checkoutFormGroup.reset();

    this.router.navigateByUrl('/');
  }
}
