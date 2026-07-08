import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-admin-customers',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-customers.component.html',
  styles: []
})
export class AdminCustomersComponent implements OnInit {
  private fb = inject(FormBuilder);
  private apiService = inject(ApiService);

  customers: any[] = [];
  isEditing = false;
  editingId: number | null = null;

  customerForm: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    phone: ['', Validators.required],
    address: [''],
    city: [''],
    state: [''],
    zipCode: ['']
  });

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.apiService.getAllCustomers().subscribe(data => this.customers = data);
  }

  onSaveCustomer(): void {
    if (this.customerForm.invalid) return;

    if (this.isEditing && this.editingId) {
      this.apiService.adminUpdateCustomer(this.editingId, this.customerForm.value).subscribe({
        next: () => {
          this.loadCustomers();
          this.resetForm();
        },
        error: (err) => alert('Error updating customer: ' + (err.error?.message || err.message))
      });
    } else {
      this.apiService.adminCreateCustomer(this.customerForm.value).subscribe({
        next: () => {
          this.loadCustomers();
          this.resetForm();
        },
        error: (err) => alert('Error creating customer: ' + (err.error?.message || err.message))
      });
    }
  }

  onEditCustomer(customer: any): void {
    this.isEditing = true;
    this.editingId = customer.id;
    this.customerForm.patchValue(customer);
    // Disable fields that shouldn't be edited
    this.customerForm.get('username')?.disable();
    this.customerForm.get('email')?.disable();
    this.customerForm.get('password')?.disable(); // Optional or set to empty
  }

  onDeleteCustomer(id: number): void {
    if (confirm('Are you sure you want to delete this customer? This action cannot be undone.')) {
      this.apiService.adminDeleteCustomer(id).subscribe({
        next: () => this.loadCustomers(),
        error: (err) => alert('Failed to delete customer: ' + (err.error?.message || err.message))
      });
    }
  }

  resetForm(): void {
    this.isEditing = false;
    this.editingId = null;
    this.customerForm.reset();
    this.customerForm.enable();
  }
}
