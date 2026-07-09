import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styles: []
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  errorMessage: string = '';
  dbStatus: 'CHECKING' | 'CONNECTED' | 'DISCONNECTED' = 'CHECKING';

  ngOnInit() {
    this.authService.checkHealth().subscribe({
      next: (res) => {
        if (res.database === 'CONNECTED') {
          this.dbStatus = 'CONNECTED';
        } else {
          this.dbStatus = 'DISCONNECTED';
        }
      },
      error: (err) => {
        this.dbStatus = 'DISCONNECTED';
        this.errorMessage = 'Health check failed: ' + (err.message || err.statusText);
      }
    });
  }

  onSubmit(): void {
    if (this.loginForm.invalid) return;

    this.authService.login(this.loginForm.value).subscribe({
      next: (user) => {
        if (user.role === 'ROLE_ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/catalog']);
        }
      },
      error: (err) => {
        this.errorMessage = 'Invalid username or password';
      }
    });
  }
}
