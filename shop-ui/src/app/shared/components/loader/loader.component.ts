import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  OnDestroy,
  ViewChild,
} from '@angular/core';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrl: './loader.component.scss',
})
export class LoaderComponent implements AfterViewInit {
  @ViewChild('loader') loader!: ElementRef;

  @Input()
  isLoading: boolean = true;

  @Input()
  width: string = '4rem';

  @Input()
  height: string = '4rem';

  @Input()
  maxHeight: boolean = true;

  ngAfterViewInit(): void {
    this.updateLoaderStyles();
  }

  private updateLoaderStyles(): void {
    if (this.loader && this.loader.nativeElement) {
      const loaderElement = this.loader.nativeElement;
      loaderElement.style.setProperty(
        '--loader-max-height',
        this.maxHeight ? '100vh' : 'auto'
      );
      loaderElement.style.setProperty('--loader-width', this.width);
      loaderElement.style.setProperty('--loader-height', this.height);
    }
  }
}
