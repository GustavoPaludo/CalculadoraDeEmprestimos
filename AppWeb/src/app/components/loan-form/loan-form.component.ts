import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormGroup, ValidationErrors, AbstractControl } from '@angular/forms';
import { LoanRequest } from '../../models/loan/loan-request.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loan-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './loan-form.component.html',
  styleUrls: ['./loan-form.component.scss']
})
export class LoanFormComponent implements OnInit {
  @Output() calculate = new EventEmitter<LoanRequest>();

  form!: FormGroup;

  constructor(private fb: FormBuilder) { }

  ngOnInit() {
    this.form = this.fb.group({
      initialDate: ['', Validators.required],
      endDate: ['', Validators.required],
      firstPaymentDate: ['', Validators.required],
      loanValue: [null, [Validators.required, Validators.min(0.01)]],
      interestValue: [null, [Validators.required, Validators.min(0.01)]]
    }, { validators: this.dateRangeValidator });
  }

  dateRangeValidator(control: AbstractControl): ValidationErrors | null {
    const initialDate = control.get('initialDate')?.value;
    const endDate = control.get('endDate')?.value;
    const firstPayment = control.get('firstPaymentDate')?.value;

    if (!initialDate || !endDate || !firstPayment) {
      return null;
    }

    const dtInitial = new Date(initialDate);
    const dtFinal = new Date(endDate);
    const dtFirstPayment = new Date(firstPayment);

    if (isNaN(dtInitial.getTime()) || isNaN(dtFinal.getTime()) || isNaN(dtFirstPayment.getTime())) {
      return { dateInvalid: true };
    }

    if (dtFinal <= dtInitial) {
      return { endDateBeforeInitialDate: true };
    }

    if (dtFirstPayment <= dtInitial || dtFirstPayment >= dtFinal) {
      return { firstPaymentOutsideRange: true };
    }

    return null;
  }

  onSubmit() {
    if (this.form.valid) {
      this.calculate.emit(this.form.value as LoanRequest);
    }
  }
}
