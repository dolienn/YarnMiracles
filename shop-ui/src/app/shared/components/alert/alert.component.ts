import { Component, ElementRef, Input, OnInit, Renderer2 } from '@angular/core';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.scss',
})
export class AlertComponent implements OnInit {
  @Input()
  msg: string = '';

  @Input()
  backgroundColor: string = '';

  @Input()
  color: string = '';

  @Input()
  borderColor: string = '';

  constructor(private renderer: Renderer2, private el: ElementRef) {}

  ngOnInit(): void {
    this.setBackgroundColor();
    this.setColor();
    this.setBorderColor();
  }

  setBackgroundColor() {
    const alertElement = this.el.nativeElement.querySelector('.alert');
    if (alertElement && this.backgroundColor !== '') {
      this.renderer.setStyle(
        alertElement,
        'background-color',
        this.backgroundColor
      );
    }
  }

  setColor() {
    const msgElement = this.el.nativeElement.querySelector('.msg');
    if (msgElement && this.color !== '') {
      this.renderer.setStyle(msgElement, 'color', this.color);
    }
  }

  setBorderColor() {
    const alertElement = this.el.nativeElement.querySelector('.alert');
    if (alertElement && this.borderColor !== '') {
      this.renderer.setStyle(
        alertElement,
        'border-left-color',
        this.borderColor
      );
    }
  }
}
