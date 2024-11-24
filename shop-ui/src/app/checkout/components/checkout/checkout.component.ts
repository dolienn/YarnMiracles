import {
  AfterViewInit,
  Component,
  ElementRef,
  OnInit,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { FormGroup } from '@angular/forms';

import { Router } from '@angular/router';
import { User } from '../../../user/models/user/user';
import { environment } from '../../../../environments/environment.development';
import { PaymentInfo } from '../../../payment/models/payment-info/payment-info';
import { CartService } from '../../../cart/services/cart/cart.service';
import { CheckoutService } from '../../services/checkout/checkout.service';
import { TokenService } from '../../../token/services/token/token.service';
import { Order } from '../../../order/models/order/order';
import { OrderItem } from '../../../order/models/order-item/order-item';
import { Purchase } from '../../models/purchase/purchase';
import { CartStorageService } from '../../../cart/services/cart-storage/cart-storage.service';
import { PaymentService } from '../../../payment/services/payment/payment.service';
import { Country } from '../../../country/models/country/country';
import { CountryService } from '../../../country/services/country/country.service';
import { CheckoutFormService } from '../../services/checkout-form/checkout-form.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss',
})
export class CheckoutComponent implements OnInit, AfterViewInit {
  @ViewChildren('alert') alerts!: QueryList<ElementRef>;

  checkoutFormGroup!: FormGroup;
  user: User = new User();
  totalPrice: number = 0;
  totalQuantity: number = 0;
  countries: Country[] = [];
  shippingCost: number = 19.99;

  isLoading: boolean = true;
  purchaseLoading: boolean = false;
  isChecked: boolean = true;
  isCheckboxDisabled: boolean = false;
  isDisabled: boolean = false;

  stripe = Stripe(environment.stripePublishableKey);
  paymentInfo: PaymentInfo = new PaymentInfo();
  cardElement: any;
  displayError: string = '';

  constructor(
    private countryService: CountryService,
    private cartService: CartService,
    private cartStorageService: CartStorageService,
    private checkoutService: CheckoutService,
    private tokenService: TokenService,
    private checkoutFormService: CheckoutFormService,
    private paymentService: PaymentService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    await Promise.all([
      this.setupStripePaymentForm(),
      this.loadCartDetails(),
      this.initFormAndUser(),
      this.setupBillingAddressSubscription(),
      this.loadCountries(),
    ]);
  }
  ngAfterViewInit() {
    this.alerts.changes.subscribe(() => {
      if (this.alerts.length) {
        this.scrollToFirstAlert();
      }
    });
  }

  onSubmit() {
    if (this.isFormInvalid()) {
      this.handleInvalidForm();
      return;
    }

    this.purchaseLoading = true;
    this.setShippingAddressIfDisabled();
    const purchase = this.createPurchaseDetails();
    this.setPaymentInfo(purchase);

    this.processPayment(purchase);
  }

  copyBillingToShipping(event: any): void {
    const isChecked = event.target.checked;
    this.isChecked = isChecked;

    this.copyBillingAddressToShipping(isChecked);
    this.toggleShippingAddressContainer(isChecked);
    this.setShippingAddressCountry();
  }

  onCountryChange(country: string): void {
    const isCountryValid = country !== '0: Object';
    this.updateCheckboxAndShippingAddress(isCountryValid);
    this.updateShippingAddressContainer(isCountryValid);
    this.setShippingAddressCountry();
  }

  getField(controlName: string) {
    return this.checkoutFormGroup.get(controlName);
  }

  isFieldInvalid(controlName: string): boolean | undefined {
    const control = this.checkoutFormGroup.get(controlName);
    return control?.invalid && (control?.dirty || control?.touched);
  }

  hasError(controlName: string, errorCode: string): boolean {
    const control = this.checkoutFormGroup.get(controlName);
    return control?.hasError(errorCode) ?? false;
  }

  private async setupStripePaymentForm(): Promise<void> {
    const elements = this.stripe.elements();
    this.cardElement = elements.create('card', { hidePostalCode: true });

    this.cardElement.mount('#card-element');

    this.cardElement.on('change', (event: any) => {
      if (event.complete) {
        this.displayError = '';
      } else if (event.error) {
        this.displayError = event.error.message;
      }
    });
  }

  private async loadCartDetails(): Promise<void> {
    this.cartService.totalQuantity.subscribe(
      (data) => (this.totalQuantity = data)
    );

    this.cartService.totalPrice.subscribe((data) => (this.totalPrice = data));
  }

  private async initFormAndUser(): Promise<void> {
    this.checkoutFormGroup = this.checkoutFormService.createCheckoutForm(
      this.user
    );

    this.tokenService.getUserByJwtToken()?.subscribe((data) => {
      this.user = data;
      this.checkoutFormGroup.patchValue({
        customer: {
          firstname: this.user.firstname,
          lastname: this.user.lastname,
          email: this.user.email,
        },
      });
    });
  }

  private async setupBillingAddressSubscription(): Promise<void> {
    this.checkoutFormGroup
      .get('billingAddress')!
      .valueChanges.subscribe((value) => {
        if (this.isChecked) {
          this.patchShippingAddress(value);
        }
      });
  }

  private patchShippingAddress(value: any): void {
    this.checkoutFormGroup.get('shippingAddress')!.patchValue(value);
  }

  private async loadCountries(): Promise<void> {
    this.isLoading = true;

    this.countryService.getCountries().subscribe({
      next: (data) => {
        this.countries = data;
        this.isLoading = false;

        if (this.countries.length > 0) {
          this.setDefaultCountries();
        }

        this.copyBillingToShipping({ target: { checked: this.isChecked } });
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  private setDefaultCountries(): void {
    const defaultCountry = this.countries[0];

    this.checkoutFormGroup
      .get('billingAddress.country')
      ?.setValue(defaultCountry);
    this.checkoutFormGroup
      .get('shippingAddress.country')
      ?.setValue(defaultCountry);
  }

  private scrollToFirstAlert() {
    const firstAlert = this.alerts.first;
    if (firstAlert) {
      firstAlert.nativeElement.scrollIntoView({
        behavior: 'smooth',
        block: 'center',
      });
    }
  }

  private isFormInvalid(): boolean {
    return this.checkoutFormGroup.invalid || this.displayError !== '';
  }

  private handleInvalidForm(): void {
    this.checkoutFormGroup.markAllAsTouched();
    this.scrollToFirstAlert();
    this.purchaseLoading = false;
  }

  private setShippingAddressIfDisabled(): void {
    const shippingAddressContainer = document.querySelector(
      '.shippingAddressContainer'
    );

    if (shippingAddressContainer?.classList.contains('disabled')) {
      this.checkoutFormGroup.controls['shippingAddress'].setValue(
        this.checkoutFormGroup.controls['billingAddress'].value
      );
    }
  }

  private createPurchaseDetails(): Purchase {
    const order = this.createOrder();
    const cartItems = this.cartService.cartItems;

    const orderItems: OrderItem[] = cartItems.map(
      (cartItem) => new OrderItem(cartItem)
    );

    const purchase = new Purchase();
    purchase.customer = this.checkoutFormGroup.controls['customer'].value;

    purchase.shippingAddress =
      this.getAddressWithCountryName('shippingAddress');
    purchase.billingAddress = this.getAddressWithCountryName('billingAddress');

    purchase.order = order;
    purchase.orderItems = orderItems;

    return purchase;
  }

  private createOrder(): Order {
    const order = new Order();
    order.totalPrice = this.totalPrice;
    order.totalQuantity = this.totalQuantity;
    return order;
  }

  private getAddressWithCountryName(
    addressType: 'shippingAddress' | 'billingAddress'
  ): any {
    const address = this.checkoutFormGroup.controls[addressType].value;
    const country: Country = address?.country;
    if (country) {
      address.country = country.name;
    }
    return { ...address };
  }

  private setPaymentInfo(purchase: Purchase): void {
    this.paymentInfo.amount = Math.round(this.totalPrice * 100);
    this.paymentInfo.currency = 'PLN';
    this.paymentInfo.receiptEmail = purchase.customer?.email;
  }

  private processPayment(purchase: any): void {
    this.isDisabled = true;

    this.paymentService.createPaymentIntent(this.paymentInfo).subscribe({
      next: (paymentIntentResponse) =>
        this.handlePaymentIntentResponse(paymentIntentResponse, purchase),
      error: () => this.handlePaymentError(),
    });
  }

  private handlePaymentIntentResponse(
    paymentIntentResponse: any,
    purchase: any
  ): void {
    this.stripe
      .confirmCardPayment(
        paymentIntentResponse.client_secret,
        {
          payment_method: this.createPaymentMethod(purchase),
        },
        { handleActions: false }
      )
      .then((result: any) => {
        if (result.error) {
          this.handlePaymentError();
        } else {
          this.placeOrder(purchase);
        }
      });
  }

  private createPaymentMethod(purchase: any): any {
    return {
      card: this.cardElement,
      billing_details: {
        email: purchase.customer?.email,
        name: `${purchase.customer?.firstname} ${purchase.customer?.lastname}`,
        address: {
          line1: purchase.billingAddress?.street,
          city: purchase.billingAddress?.city,
          postal_code: purchase.billingAddress?.zipCode,
          country: this.checkoutFormGroup.get('billingAddress.country')?.value
            .code,
        },
      },
    };
  }

  private handlePaymentError(): void {
    this.isDisabled = false;
    this.purchaseLoading = false;
  }

  private placeOrder(purchase: any): void {
    this.checkoutService.placeOrder(purchase).subscribe({
      next: (response: any) => {
        this.savePurchaseResponse(response, purchase);
        this.router.navigate(['/successful-purchase']);
        this.resetCart();
        this.finalizePurchase();
      },
      error: () => this.finalizePurchase(),
    });
  }

  private savePurchaseResponse(response: any, purchase: any): void {
    sessionStorage.setItem('purchaseResponse', JSON.stringify(response));
    sessionStorage.setItem('purchase', JSON.stringify(purchase));
  }

  private resetCart() {
    this.cartService.resetCart();
    this.cartStorageService.saveCartItems(this.cartService.cartItems);

    this.checkoutFormGroup.reset();
  }

  private finalizePurchase(): void {
    this.isDisabled = false;
    this.purchaseLoading = false;
  }

  private copyBillingAddressToShipping(isChecked: boolean): void {
    if (isChecked) {
      this.checkoutFormGroup.controls['shippingAddress'].setValue(
        this.checkoutFormGroup.controls['billingAddress'].value
      );
    } else {
      this.checkoutFormGroup.controls['shippingAddress'].reset();
    }
  }

  private toggleShippingAddressContainer(isChecked: boolean): void {
    const shippingAddressContainer = document.querySelector(
      '.shippingAddressContainer'
    );

    if (shippingAddressContainer) {
      if (isChecked) {
        shippingAddressContainer.classList.add('disabled');
      } else {
        shippingAddressContainer.classList.remove('disabled');
      }
    }
  }

  private setShippingAddressCountry(): void {
    this.checkoutFormGroup.controls['shippingAddress']
      .get('country')
      ?.setValue(this.countries[0]);
  }

  private updateCheckboxAndShippingAddress(isCountryValid: boolean): void {
    this.isChecked = !isCountryValid;
    this.isCheckboxDisabled = isCountryValid;

    if (isCountryValid) {
      this.checkoutFormGroup.controls['shippingAddress'].reset();
    }
  }

  private updateShippingAddressContainer(isCountryValid: boolean): void {
    const shippingAddressContainer = document.querySelector(
      '.shippingAddressContainer'
    );

    if (shippingAddressContainer) {
      shippingAddressContainer.classList.toggle('disabled', !isCountryValid);
    }
  }
}
