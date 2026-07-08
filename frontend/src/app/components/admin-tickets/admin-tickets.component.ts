import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { CustomerIssue, IssueReply } from '../../models/oms.models';

@Component({
  selector: 'app-admin-tickets',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-tickets.component.html',
  styles: []
})
export class AdminTicketsComponent implements OnInit {
  private fb = inject(FormBuilder);
  private apiService = inject(ApiService);

  issues: CustomerIssue[] = [];
  selectedIssue: CustomerIssue | null = null;
  replies: IssueReply[] = [];

  replyForm: FormGroup = this.fb.group({
    message: ['', Validators.required]
  });

  ngOnInit(): void {
    this.loadIssues();
  }

  loadIssues(): void {
    this.apiService.getIssues().subscribe(data => this.issues = data);
  }

  onSelectIssue(issue: CustomerIssue): void {
    this.selectedIssue = issue;
    this.replyForm.reset();
    this.loadReplies(issue.id!);
  }

  loadReplies(issueId: number): void {
    this.apiService.getIssueReplies(issueId).subscribe(data => this.replies = data);
  }

  onSubmitReply(): void {
    if (this.replyForm.invalid || !this.selectedIssue) return;

    this.apiService.replyToIssue(this.selectedIssue.id!, this.replyForm.value.message).subscribe({
      next: () => {
        this.replyForm.reset();
        this.loadReplies(this.selectedIssue!.id!);
        this.loadIssues();
      }
    });
  }
}
