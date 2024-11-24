import { Component, OnInit } from '@angular/core';
import { ProductCategory } from '../../../product/models/product-category/product-category';
import { NotificationService } from '../../../notification/services/notification/notification.service';
import { Router } from '@angular/router';
import { ProductRequest } from '../../../product/models/product-request/product-request';
import { ProductService } from '../../../product/services/product/product.service';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.scss',
})
export class AddProductComponent implements OnInit {
  productCategories: ProductCategory[] = [];
  productRequest: ProductRequest = this.initializeProductRequest();
  errorMsg: Array<string> = [];
  selectedCategoryId: number = 0;
  selectedFile: File | null = null;
  selectedFileName: string | null = null;
  isAddingProduct: boolean = false;

  constructor(
    private productService: ProductService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProductCategories();
  }

  onCategoryChange() {
    this.productRequest.category = this.getSelectedCategory();
  }

  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (file && this.isValidFileType(file.type)) {
      this.selectedFile = file;
      this.selectedFileName = this.shortenFileName(file.name);
    } else {
      this.resetFileSelection(
        'Invalid file type. Please upload a .jpg or .png file.'
      );
    }
  }

  changeFile() {
    this.selectedFileName = null;
    const fileInput = document.getElementById('file') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  addProduct(): void {
    if (!this.isFormValid()) return;

    this.isAddingProduct = true;
    this.errorMsg = [];
    const formData = this.createFormData();

    this.productService.addProduct(formData).subscribe({
      next: () => this.handleAddProductSuccess(),
      error: (err) => this.handleAddProductError(err),
    });
  }

  private initializeProductRequest(): ProductRequest {
    return {
      category: this.productCategories[0],
      name: '',
      description: '',
      unitPrice: 0,
      unitsInStock: 0,
    };
  }

  private loadProductCategories(): void {
    this.productService.getProductCategories().subscribe({
      next: (categories) => (this.productCategories = categories),
      error: () =>
        this.notificationService.showMessage(
          'Failed to load categories',
          false
        ),
    });
  }

  private getSelectedCategory(): ProductCategory {
    return (
      this.productCategories.find(
        (category) => category.id === this.selectedCategoryId
      ) || this.productCategories[0]
    );
  }

  private isValidFileType(fileType: string): boolean {
    const validTypes = ['image/jpeg', 'image/png'];
    return validTypes.includes(fileType);
  }

  private shortenFileName(fileName: string): string {
    return fileName.length > 10 ? fileName.substring(0, 10) + '...' : fileName;
  }

  private resetFileSelection(errorMessage: string = ''): void {
    this.selectedFile = null;
    this.selectedFileName = null;
    if (errorMessage) this.errorMsg.push(errorMessage);

    const fileInput = document.getElementById('file') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  private isFormValid(): boolean {
    if (!this.productRequest.name || !this.productRequest.category) {
      this.errorMsg.push('Name and category are required.');
      return false;
    }
    return true;
  }

  private createFormData(): FormData {
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

    return formData;
  }

  private handleAddProductSuccess(): void {
    this.resetForm();
    this.notificationService.showMessage('Product added successfully', true);
    this.router.navigateByUrl('/admin-panel');
  }

  private resetForm(): void {
    this.productRequest = this.initializeProductRequest();
    this.resetFileSelection();
    this.isAddingProduct = false;
  }

  private handleAddProductError(err: any): void {
    this.isAddingProduct = false;
    if (err.error.validationErrors) {
      this.errorMsg = err.error.validationErrors;
    } else if (err.error.error) {
      this.errorMsg.push(err.error.error);
    }
    this.notificationService.showMessage('Error adding product', false);
  }
}
