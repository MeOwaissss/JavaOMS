import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Category, Product } from '../../models/oms.models';

@Component({
  selector: 'app-admin-products',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-products.component.html',
  styles: []
})
export class AdminProductsComponent implements OnInit {
  private fb = inject(FormBuilder);
  private apiService = inject(ApiService);

  products: Product[] = [];
  categories: Category[] = [];

  activeTab: 'products' | 'categories' = 'products';

  productForm: FormGroup = this.fb.group({
    id: [null],
    categoryId: ['', Validators.required],
    name: ['', Validators.required],
    description: [''],
    price: [0, [Validators.required, Validators.min(0)]],
    gstPercent: [18, [Validators.required, Validators.min(0)]],
    imageUrl: [''],
    sku: ['', Validators.required]
  });

  categoryForm: FormGroup = this.fb.group({
    id: [null],
    name: ['', Validators.required],
    description: ['']
  });

  editingProduct: boolean = false;
  editingCategory: boolean = false;

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.apiService.getCategories().subscribe(cats => this.categories = cats);
    this.apiService.getProducts().subscribe(prods => this.products = prods);
  }

  setTab(tab: 'products' | 'categories'): void {
    this.activeTab = tab;
  }

  onSaveProduct(): void {
    if (this.productForm.invalid) return;

    const val = this.productForm.value;
    if (val.id) {
      this.apiService.updateProduct(val.id, val).subscribe(() => {
        this.resetProductForm();
        this.loadData();
      });
    } else {
      this.apiService.createProduct(val).subscribe(() => {
        this.resetProductForm();
        this.loadData();
      });
    }
  }

  onEditProduct(p: Product): void {
    this.editingProduct = true;
    this.productForm.patchValue({
      id: p.id,
      categoryId: p.category.id,
      name: p.name,
      description: p.description,
      price: p.price,
      gstPercent: p.gstPercent,
      imageUrl: p.imageUrl,
      sku: p.sku
    });
  }

  onDeleteProduct(id: number): void {
    this.apiService.deleteProduct(id).subscribe(() => this.loadData());
  }

  resetProductForm(): void {
    this.editingProduct = false;
    this.productForm.reset({
      id: null,
      categoryId: '',
      name: '',
      description: '',
      price: 0,
      gstPercent: 18,
      imageUrl: '',
      sku: ''
    });
  }

  onSaveCategory(): void {
    if (this.categoryForm.invalid) return;

    const val = this.categoryForm.value;
    if (val.id) {
      this.apiService.updateCategory(val.id, val).subscribe(() => {
        this.resetCategoryForm();
        this.loadData();
      });
    } else {
      this.apiService.createCategory(val).subscribe(() => {
        this.resetCategoryForm();
        this.loadData();
      });
    }
  }

  onEditCategory(c: Category): void {
    this.editingCategory = true;
    this.categoryForm.patchValue({
      id: c.id,
      name: c.name,
      description: c.description
    });
  }

  onDeleteCategory(id: number): void {
    this.apiService.deleteCategory(id).subscribe(() => this.loadData());
  }

  resetCategoryForm(): void {
    this.editingCategory = false;
    this.categoryForm.reset({
      id: null,
      name: '',
      description: ''
    });
  }
}
