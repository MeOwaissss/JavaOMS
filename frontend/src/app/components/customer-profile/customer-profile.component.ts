import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-customer-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-profile.component.html',
  styles: []
})
export class CustomerProfileComponent implements OnInit {
  private fb = inject(FormBuilder);
  private apiService = inject(ApiService);

  profileForm: FormGroup = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    phone: ['', Validators.required],
    address: [''],
    city: [''],
    state: [''],
    zipCode: ['']
  });

  successMessage = '';
  errorMessage = '';

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.apiService.getCustomerProfile().subscribe({
      next: (profile: any) => {
        this.profileForm.patchValue(profile);
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Failed to load profile details. Error: ' + (err.error?.message || err.message);
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) return;

    this.successMessage = '';
    this.errorMessage = '';

    this.apiService.updateCustomerProfile(this.profileForm.value).subscribe({
      next: () => {
        this.successMessage = 'Profile updated successfully!';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err: any) => {
        console.error(err);
        this.errorMessage = 'Error: ' + (err.error?.message || err.message || JSON.stringify(err));
      }
    });
  }
}
