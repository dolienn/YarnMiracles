import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.scss',
})
export class PaginationComponent {
  @Input() totalElements!: number;
  @Input() pageSize!: number;
  @Input() pageNumber!: number;
  @Input() isLoading: boolean = false;
  @Output() pageNumberChange = new EventEmitter<number>();

  onPageChange(): void {
    this.pageNumberChange.emit(this.pageNumber);
  }
}
