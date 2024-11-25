import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  ViewChild,
} from '@angular/core';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrl: './loader.component.scss',
})
export class LoaderComponent implements AfterViewInit {
  @ViewChild('loader') loader!: ElementRef;

  @Input() isLoading: boolean = true;
  @Input() width: string = '4rem';
  @Input() height: string = '4rem';
  @Input() maxHeight: boolean = true;

  ngAfterViewInit(): void {
    this.applyLoaderStyles();
  }

  private applyLoaderStyles(): void {
    if (!this.loader?.nativeElement) return;

    const loaderElement = this.loader.nativeElement;
    this.setStyle(
      loaderElement,
      '--loader-max-height',
      this.maxHeight ? '100vh' : 'auto'
    );
    this.setStyle(loaderElement, '--loader-width', this.width);
    this.setStyle(loaderElement, '--loader-height', this.height);
  }

  private setStyle(
    element: HTMLElement,
    property: string,
    value: string
  ): void {
    element.style.setProperty(property, value);
  }
}
