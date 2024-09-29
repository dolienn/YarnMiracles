import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product/product.service';
import { ProductCategory } from '../../../common/product-category/product-category';
import { ProductRequest } from '../../../common/product-request/product-request';
import { AdminService } from '../../../services/admin/admin.service';
import { NotificationService } from '../../../services/notification/notification.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.scss',
})
export class AddProductComponent implements OnInit {
  productCategories: ProductCategory[] = [];
  productRequest: ProductRequest = {
    category: this.productCategories[0],
    name: '',
    description: '',
    unitPrice: 0,
    unitsInStock: 0,
  };

  errorMsg: Array<string> = [];

  selectedCategoryId: number = 0;
  selectedFile: File | null = null;
  selectedFileName: string | null = null;

  addProductLoading: boolean = false;

  constructor(
    private adminService: AdminService,
    private productService: ProductService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.listProductCategories();
  }

  listProductCategories() {
    this.productService.getProductCategories().subscribe((data) => {
      this.productCategories = data;
    });
  }

  onCategoryChange() {
    this.productRequest.category =
      this.productCategories.find(
        (category) => category.id == this.selectedCategoryId
      ) || this.productCategories[0];
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    const validTypes = ['image/jpeg', 'image/png'];

    if (file && validTypes.includes(file.type)) {
      this.selectedFile = file;
      this.selectedFileName = this.shortenFileName(file.name);
    } else {
      this.errorMsg.push(
        'Invalid file type. Please upload a .jpg or .png file.'
      );
      this.selectedFile = null;
      this.selectedFileName = null;
    }
  }

  shortenFileName(fileName: string): string {
    return fileName.length > 10 ? fileName.substring(0, 10) + '...' : fileName;
  }

  changeFile() {
    this.selectedFileName = null;
    const fileInput = document.getElementById('file') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  addProduct() {
    this.addProductLoading = true;
    this.errorMsg = [];

    const formData = new FormData();

    const productJson = JSON.stringify({
      category: this.productRequest.category,
      name: this.productRequest.name,
      description: this.productRequest.description,
      unitPrice: this.productRequest.unitPrice,
      unitsInStock: this.productRequest.unitsInStock,
    });

    formData.append(
      'product',
      new Blob([productJson], { type: 'application/json' })
    );

    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    this.adminService.addProduct(formData).subscribe({
      next: () => {
        this.errorMsg = [];
        this.productRequest = {
          category: this.productCategories[0],
          name: '',
          description: '',
          unitPrice: 0,
          unitsInStock: 0,
        };

        this.addProductLoading = false;
        this.notificationService.showMessage('Product added successfully!');
        this.router.navigateByUrl('/admin-panel');
      },
      error: (err) => {
        this.addProductLoading = false;
        if (err.error.validationErrors) {
          this.errorMsg = err.error.validationErrors;
        } else {
          this.errorMsg.push(err.error.error);
        }
      },
    });
  }
}
