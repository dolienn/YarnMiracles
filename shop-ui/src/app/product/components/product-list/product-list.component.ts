import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Product } from '../../models/product/product';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../../user/services/user/user.service';
import { User } from '../../../user/models/user/user';
import { ProductService } from '../../services/product/product.service';
import { TokenService } from '../../../token/services/token/token.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss',
})
export class ProductListComponent implements OnInit {
  @ViewChild('productsSection') productsSection!: ElementRef;

  isLoading: boolean = true;

  products: Product[] = [];
  currentCategoryId: number = 1;
  previousCategoryId: number = 1;
  currentCategoryName: string = '';
  searchMode: boolean = false;
  isFavouriteRoute: boolean = false;
  user: User = new User();

  pageNumber: number = 1;
  pageSize: number = 15;
  totalElements: number = 0;

  previousKeyword: string = '';

  selectedSortOption: string = 'default';

  constructor(
    private productService: ProductService,
    private userService: UserService,
    private tokenService: TokenService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.listProducts();
    });
  }

  listProducts() {
    this.isLoading = true;
    this.searchMode = this.route.snapshot.paramMap.has('keyword');

    if (this.searchMode) {
      this.handleSearchProducts();
    } else {
      this.route.url.subscribe((urlSegments) => {
        this.isFavouriteRoute = urlSegments.some(
          (segment) => segment.path === 'favourites'
        );
      });
      if (this.isFavouriteRoute) {
        this.handleFavouritesProducts();
      } else {
        this.handleListProducts();
      }
    }
  }

  handleSearchProducts() {
    let keyword: string = this.route.snapshot.paramMap.get('keyword')!;

    if (this.previousKeyword != keyword) {
      this.pageNumber = 1;
    }

    this.previousKeyword = keyword;

    if (this.selectedSortOption === 'default') {
      this.productService
        .searchProductsPaginate(this.pageNumber - 1, this.pageSize, keyword)
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'lowest-price') {
      this.productService
        .searchProductsPaginateOrderByUnitPriceAsc(
          this.pageNumber - 1,
          this.pageSize,
          keyword
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'highest-price') {
      this.productService
        .searchProductsPaginateOrderByUnitPriceDesc(
          this.pageNumber - 1,
          this.pageSize,
          keyword
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'rating') {
      this.productService
        .searchProductsPaginateOrderByRateDesc(
          this.pageNumber - 1,
          this.pageSize,
          keyword
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'popularity') {
      this.productService
        .searchProductsPaginateOrderBySalesDesc(
          this.pageNumber - 1,
          this.pageSize,
          keyword
        )
        .subscribe(this.processResult());
    }
  }

  handleListProducts() {
    const hasCategoryId: boolean = this.route.snapshot.paramMap.has('id');

    if (hasCategoryId) {
      this.currentCategoryId = +this.route.snapshot.paramMap.get('id')!;
      this.currentCategoryName = this.route.snapshot.paramMap.get('name')!;

      if (this.previousCategoryId != this.currentCategoryId) {
        this.pageNumber = 1;
      }

      this.previousCategoryId = this.currentCategoryId;

      this.handleListProductsByCategory();
    } else {
      this.handleListProductsWithoutCategory();
    }
  }

  handleListProductsByCategory() {
    if (this.selectedSortOption === 'default') {
      this.productService
        .getProductListPaginateWithCategory(
          this.pageNumber - 1,
          this.pageSize,
          this.currentCategoryId
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'lowest-price') {
      this.productService
        .getProductListPaginateOrderByUnitPriceAscWithCategory(
          this.pageNumber - 1,
          this.pageSize,
          this.currentCategoryId
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'highest-price') {
      this.productService
        .getProductListPaginateOrderByUnitPriceDescWithCategory(
          this.pageNumber - 1,
          this.pageSize,
          this.currentCategoryId
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'rating') {
      this.productService
        .getProductListPaginateOrderByRateDescWithCategory(
          this.pageNumber - 1,
          this.pageSize,
          this.currentCategoryId
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'popularity') {
      this.productService
        .getProductListPaginateOrderBySalesDescWithCategory(
          this.pageNumber - 1,
          this.pageSize,
          this.currentCategoryId
        )
        .subscribe(this.processResult());
    }
  }

  handleListProductsWithoutCategory() {
    if (this.selectedSortOption === 'default') {
      this.productService
        .getProductListPaginate(this.pageNumber - 1, this.pageSize)
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'lowest-price') {
      this.productService
        .getProductListPaginateOrderByUnitPriceAsc(
          this.pageNumber - 1,
          this.pageSize
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'highest-price') {
      this.productService
        .getProductListPaginateOrderByUnitPriceDesc(
          this.pageNumber - 1,
          this.pageSize
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'rating') {
      this.productService
        .getProductListPaginateOrderByRateDesc(
          this.pageNumber - 1,
          this.pageSize
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'popularity') {
      this.productService
        .getProductListPaginateOrderBySalesDesc(
          this.pageNumber - 1,
          this.pageSize
        )
        .subscribe(this.processResult());
    }
  }

  handleFavouritesProducts() {
    this.tokenService.getUserInfo()?.subscribe((data) => {
      this.user.id = data.id;
      this.user.firstname = data.firstname;
      this.user.lastname = data.lastname;
      this.user.email = data.email;
      if (this.selectedSortOption === 'default') {
        this.userService
          .getFavouriteProductsPaginate(
            this.pageNumber - 1,
            this.pageSize,
            this.user.id
          )
          .subscribe(this.processResult());
      } else if (this.selectedSortOption === 'lowest-price') {
        this.userService
          .getFavouriteProductsPaginateOrderByUnitPriceAsc(
            this.pageNumber - 1,
            this.pageSize,
            this.user.id
          )
          .subscribe(this.processResult());
      } else if (this.selectedSortOption === 'highest-price') {
        this.userService
          .getFavouriteProductsPaginateOrderByUnitPriceDesc(
            this.pageNumber - 1,
            this.pageSize,
            this.user.id
          )
          .subscribe(this.processResult());
      } else if (this.selectedSortOption === 'rating') {
        this.userService
          .getFavouriteProductsPaginateOrderByRateDesc(
            this.pageNumber - 1,
            this.pageSize,
            this.user.id
          )
          .subscribe(this.processResult());
      }
    });
  }

  isNewProduct(dateCreated: any): boolean {
    return this.productService.isNewProduct(dateCreated);
  }

  updatePageSize(pageSize: string) {
    this.pageSize = +pageSize;
    this.pageNumber = 1;
    this.listProducts();
  }

  processResult() {
    return (data: any) => {
      this.products = data._embedded.products;
      this.pageNumber = data.page.number + 1;
      this.pageSize = data.page.size;
      this.totalElements = data.page.totalElements;
      this.isLoading = false;
    };
  }

  onSortOptionChange() {
    this.pageNumber = 1;
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
}
