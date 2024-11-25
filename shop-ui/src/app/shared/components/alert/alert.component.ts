import { Component, ElementRef, Input, OnInit, Renderer2 } from '@angular/core';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.scss',
})
export class AlertComponent implements OnInit {
  @Input() msg: string = '';
  @Input() backgroundColor: string = '';
  @Input() color: string = '';
  @Input() borderColor: string = '';

  constructor(private renderer: Renderer2, private el: ElementRef) {}

  ngOnInit(): void {
    this.applyStyles();
  }

  private applyStyles() {
    const alertElement = this.el.nativeElement.querySelector('.alert');
    const msgElement = this.el.nativeElement.querySelector('.msg');

    if (alertElement) {
      this.setBackgroundColor(alertElement);
      this.setBorderColor(alertElement);
    }

    this.setColor(msgElement);
  }

  private setBackgroundColor(alertElement: any) {
    if (this.backgroundColor !== '') {
      this.renderer.setStyle(
        alertElement,
        'background-color',
        this.backgroundColor
      );
    }
  }

  private setBorderColor(alertElement: any) {
    if (this.borderColor !== '') {
      this.renderer.setStyle(
        alertElement,
        'border-left-color',
        this.borderColor
      );
    }
  }

  private setColor(msgElement: any) {
    if (this.color !== '') {
      this.renderer.setStyle(msgElement, 'color', this.color);
    }
  }
}
