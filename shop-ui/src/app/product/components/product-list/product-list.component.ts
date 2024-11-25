import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Product } from '../../models/product/product';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../../user/models/user/user';
import { ProductService } from '../../services/product/product.service';
import { TokenService } from '../../../token/services/token/token.service';
import { PaginationAndSortParams } from '../../../pagination/models/pagination-and-sort-params/pagination-and-sort-params';
import { EMPTY, Observable, switchMap } from 'rxjs';
import { PaginatedProductResponse } from '../../models/paginated-product-response/paginated-product-response';
import { Page } from '../../../shared/models/page/page';
import { NotificationService } from '../../../notification/services/notification/notification.service';
import { FavouriteService } from '../../../favourite/services/favourite/favourite.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss',
})
export class ProductListComponent implements OnInit {
  @ViewChild('productsSection') productsSection!: ElementRef;

  isLoading = true;
  products: Product[] = [];
  user: User = new User();

  currentCategoryId = 1;
  currentCategoryName = '';
  searchMode = false;
  isFavouriteRoute = false;

  previousCategoryId = 1;
  previousKeyword = '';
  selectedSortOption = 'default';

  page: Page = new Page();

  constructor(
    private productService: ProductService,
    private favouriteService: FavouriteService,
    private tokenService: TokenService,
    private notificationService: NotificationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => this.updatePageSize('15'));
  }

  listProducts(): void {
    this.isLoading = true;
    this.searchMode = this.route.snapshot.paramMap.has('keyword');

    this.getProducts().subscribe({
      next: (data) => this.updateProductList(data),
      error: () => this.handleError(),
    });
  }

  isNewProduct(dateCreated: any): boolean {
    return this.productService.isProductNew(dateCreated);
  }

  updatePageSize(pageSize: string) {
    this.page.size = +pageSize;
    this.page.number = 1;
    this.listProducts();
  }

  onSortOptionChange() {
    this.page.number = 1;
    this.listProducts();
  }

  scrollToProducts() {
    if (this.productsSection) {
      this.listProducts();
      this.productsSection.nativeElement.scrollIntoView({
        behavior: 'smooth',
      });
    }
  }

  private getProducts(): Observable<PaginatedProductResponse> {
    const paginationParams = this.getPaginationParams();

    this.checkFavouriteRoute();
    if (this.isFavouriteRoute) {
      return this.getFavouriteProducts(paginationParams);
    }

    if (this.searchMode) {
      return this.getSearchResults(paginationParams);
    }

    return this.getProductsByCategoryOrAll(paginationParams);
  }

  private getPaginationParams(): PaginationAndSortParams {
    return {
      page: this.page.number - 1,
      size: this.page.size,
      sortBy: this.mapSortOption(this.selectedSortOption),
    };
  }

  private mapSortOption(option: string): string | undefined {
    const sortOptions: Record<string, string | undefined> = {
      'lowest-price': 'PRICE_ASC',
      'highest-price': 'PRICE_DESC',
      rating: 'RATE_DESC',
      sale: 'SALES_DESC',
      default: undefined,
    };
    return sortOptions[option];
  }

  private checkFavouriteRoute(): void {
    this.route.url.subscribe((urlSegments) => {
      this.isFavouriteRoute = urlSegments.some(
        (segment) => segment.path === 'favourites'
      );
    });
  }

  private getFavouriteProducts(
    paginationParams: PaginationAndSortParams
  ): Observable<PaginatedProductResponse> {
    const userObservable = this.tokenService.getUserByJwtToken();
    if (!userObservable) {
      return EMPTY;
    }

    return userObservable.pipe(
      switchMap((user) => {
        this.user = user;
        return this.favouriteService.getFavourites(
          this.user.id,
          paginationParams
        );
      })
    );
  }

  private getSearchResults(
    paginationParams: PaginationAndSortParams
  ): Observable<PaginatedProductResponse> {
    const keyword = this.route.snapshot.paramMap.get('keyword')!;
    this.resetPageIfKeywordChanged(keyword);

    return this.productService.getProductsByKeyword(keyword, paginationParams);
  }

  private resetPageIfKeywordChanged(keyword: string): void {
    if (this.previousKeyword !== keyword) {
      this.page.number = 1;
    }
    this.previousKeyword = keyword;
  }

  private getProductsByCategoryOrAll(
    paginationParams: PaginationAndSortParams
  ): Observable<PaginatedProductResponse> {
    const hasCategoryId = this.route.snapshot.paramMap.has('id');

    if (hasCategoryId) {
      this.currentCategoryId = +this.route.snapshot.paramMap.get('id')!;
      this.resetPageIfCategoryChanged();
      return this.productService.getProductsByCategoryId(
        this.currentCategoryId,
        paginationParams
      );
    }

    return this.productService.getAllProducts(paginationParams);
  }

  private resetPageIfCategoryChanged(): void {
    if (this.previousCategoryId !== this.currentCategoryId) {
      this.page.number = 1;
    }
    this.previousCategoryId = this.currentCategoryId;
  }

  private updateProductList(data: PaginatedProductResponse): void {
    this.products = data.content;
    this.page.number = data.page.number + 1;
    this.page.size = data.page.size;
    this.page.totalElements = data.page.totalElements;
    this.isLoading = false;
  }

  private handleError(): void {
    this.isLoading = false;
    this.notificationService.showMessage(
      'Something went wrong while fetching products',
      false
    );
  }
}
