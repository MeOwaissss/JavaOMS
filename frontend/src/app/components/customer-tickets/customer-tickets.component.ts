import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { CustomerIssue, IssueReply } from '../../models/oms.models';

@Component({
  selector: 'app-customer-tickets',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-tickets.component.html',
  styles: []
})
export class CustomerTicketsComponent implements OnInit {
  private fb = inject(FormBuilder);
  private apiService = inject(ApiService);

  issues: CustomerIssue[] = [];
  selectedIssue: CustomerIssue | null = null;
  replies: IssueReply[] = [];

  issueForm: FormGroup = this.fb.group({
    subject: ['', Validators.required],
    description: ['', Validators.required]
  });

  replyForm: FormGroup = this.fb.group({
    message: ['', Validators.required]
  });

  creatingTicket: boolean = false;

  ngOnInit(): void {
    this.loadIssues();
  }

  loadIssues(): void {
    this.apiService.getIssues().subscribe(data => this.issues = data);
  }

  onSelectIssue(issue: CustomerIssue): void {
    this.selectedIssue = issue;
    this.creatingTicket = false;
    this.replyForm.reset();
    this.loadReplies(issue.id!);
  }

  loadReplies(issueId: number): void {
    this.apiService.getIssueReplies(issueId).subscribe(data => this.replies = data);
  }

  onSubmitTicket(): void {
    if (this.issueForm.invalid) return;

    this.apiService.createIssue(this.issueForm.value).subscribe({
      next: (issue) => {
        this.issueForm.reset();
        this.creatingTicket = false;
        this.loadIssues();
        this.onSelectIssue(issue);
      }
    });
  }

  onSubmitReply(): void {
    if (this.replyForm.invalid || !this.selectedIssue) return;

    this.apiService.replyToIssue(this.selectedIssue.id!, this.replyForm.value.message).subscribe({
      next: () => {
        this.replyForm.reset();
        this.loadReplies(this.selectedIssue!.id!);
      }
    });
  }
}
