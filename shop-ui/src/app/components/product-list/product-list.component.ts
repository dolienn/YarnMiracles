import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product/product.service';
import { Product } from '../../common/product/product';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../services/user/user.service';
import { User } from '../../common/user/user';
import { TokenService } from '../../services/token.service';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss',
})
export class ProductListComponent implements OnInit {
  isLoading: boolean = true;

  products: Product[] = [];
  currentCategoryId: number = 1;
  previousCategoryId: number = 1;
  currentCategoryName: string = '';
  searchMode: boolean = false;
  isFavouriteRoute: boolean = false;
  user: User = new User();

  pageNumber: number = 1;
  pageSize: number = 6;
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
    this.searchMode = this.route.snapshot.paramMap.has('keyword');

    if (this.searchMode) {
      this.handleSearchProducts();
    } else {
      this.route.url.subscribe((urlSegments) => {
        this.isFavouriteRoute = urlSegments.some(
          (segment) => segment.path === 'favourites'
        );
      });
      console.log(this.isFavouriteRoute);
      if (this.isFavouriteRoute) {
        this.handleFavouritesProducts();
      } else {
        this.handleListProducts();
      }
    }
  }

  handleSearchProducts() {
    const keyword: string = this.route.snapshot.paramMap.get('keyword')!;

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
    }
  }

  handleListProducts() {
    const hasCategoryId: boolean = this.route.snapshot.paramMap.has('id');

    if (hasCategoryId) {
      this.currentCategoryId = +this.route.snapshot.paramMap.get('id')!;
      this.currentCategoryName = this.route.snapshot.paramMap.get('name')!;
    } else {
      this.currentCategoryId = 1;
    }

    if (this.previousCategoryId != this.currentCategoryId) {
      this.pageNumber = 1;
    }

    this.previousCategoryId = this.currentCategoryId;

    if (this.selectedSortOption === 'default') {
      this.productService
        .getProductListPaginate(
          this.pageNumber - 1,
          this.pageSize,
          this.currentCategoryId
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'lowest-price') {
      this.productService
        .getProductListPaginateOrderByUnitPriceAsc(
          this.pageNumber - 1,
          this.pageSize,
          this.currentCategoryId
        )
        .subscribe(this.processResult());
    } else if (this.selectedSortOption === 'highest-price') {
      this.productService
        .getProductListPaginateOrderByUnitPriceDesc(
          this.pageNumber - 1,
          this.pageSize,
          this.currentCategoryId
        )
        .subscribe(this.processResult());
    }
  }

  handleFavouritesProducts() {
    console.log('halo');
    if (this.selectedSortOption === 'default') {
      this.tokenService.getUserInfo()?.subscribe((data) => {
        this.user.id = data.id;
        this.user.firstname = data.firstname;
        this.user.lastname = data.lastname;
        this.user.email = data.email;
        this.userService
          .getFavouriteProductsPaginate(
            this.pageNumber - 1,
            this.pageSize,
            this.user.id
          )
          .subscribe((data: any) => {
            this.products = data.content;
            this.pageNumber = data.page.number + 1;
            this.pageSize = data.page.size;
            this.totalElements = data.page.totalElements;
            this.isLoading = false;
          });
      });
    }
    // } else if (this.selectedSortOption === 'lowest-price') {
    //   this.productService
    //     .getProductListPaginateOrderByUnitPriceAsc(
    //       this.pageNumber - 1,
    //       this.pageSize,
    //       this.currentCategoryId
    //     )
    //     .subscribe(this.processResult());
    // } else if (this.selectedSortOption === 'highest-price') {
    //   this.productService
    //     .getProductListPaginateOrderByUnitPriceDesc(
    //       this.pageNumber - 1,
    //       this.pageSize,
    //       this.currentCategoryId
    //     )
    //     .subscribe(this.processResult());
    // }
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

  isLessThanFiveProducts(): boolean {
    return this.products.length < 5;
  }

  isLessThanFourProducts(): boolean {
    return this.products.length < 4;
  }

  isLessThanThreeProducts(): boolean {
    return this.products.length < 3;
  }

  onSortOptionChange() {
    this.pageNumber = 1;
    this.listProducts();
  }
}
