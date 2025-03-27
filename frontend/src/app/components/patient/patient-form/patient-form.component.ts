import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { PatientService } from '../../../services/patient.service';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import moment, { Moment } from 'moment';
import { provideMomentDateAdapter } from '@angular/material-moment-adapter';
import { MAT_DATE_FORMAT } from '../../../models/MAT_DATE_FORMAT';
import { MatInputModule } from '@angular/material/input';
import { NgClass, NgFor } from '@angular/common';
import { genders, Genders } from '../../../models/Genders';
import { Patient } from '../../../models/patient';

@Component({
  selector: 'app-patient-form',
  providers: [provideMomentDateAdapter(MAT_DATE_FORMAT)],
  imports: [
    MatProgressBarModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    NgClass,
    NgFor,
    RouterLink,
  ],
  templateUrl: './patient-form.component.html',
  styleUrl: './patient-form.component.css',
})
export class PatientFormComponent implements OnInit, OnDestroy {
  private patientService: PatientService = inject(PatientService);
  private router: Router = inject(Router);

  public isLoading = signal<boolean>(false);
  public isFormSubmitted = signal<boolean>(false);
  public hasDateOfBirthChanged = signal<boolean>(false);

  public patientForm!: FormGroup;
  private patient: Patient = new Patient();
  public genders: Genders[] = genders;

  public minDate: Moment = moment().subtract(110, 'year');
  public maxDate: Moment = moment();

  private onlyNumbersRegex = /^\d+$/;
  private phoneRegex = /^\d{10}$/;
  public phoneLength = 10;
  private nameRegex = /[a-zA-Z\xC0-\uFFFF]/; // Latin characters
  private birthdateRegex = /^\d{2}\/\d{2}\/\d{4}$/; // MM/DD/YYYY

  ngOnInit(): void {
    /* Init Form */
    this.patientForm = new FormGroup({
      firstname: new FormControl('', [Validators.required]),
      lastname: new FormControl('', [Validators.required]),
      birthDate: new FormControl(null, [Validators.required]),
      gender: new FormControl('', [Validators.required]),
      address: new FormControl('', [Validators.required]),
      phone: new FormControl('', [
        Validators.required,
        Validators.pattern(this.phoneRegex),
        Validators.maxLength(this.phoneLength),
      ]),
    });
  }

  ngOnDestroy(): void {}

  /* Getters */
  public get lastname() {
    return this.patientForm.get('lastname');
  }

  public get firstname() {
    return this.patientForm.get('firstname');
  }

  public get birthDate() {
    return this.patientForm.get('birthDate');
  }

  public get gender() {
    return this.patientForm.get('gender');
  }

  public get address() {
    return this.patientForm.get('address');
  }

  public get phone() {
    return this.patientForm.get('phone');
  }

  /* Invalid */
  public isInvalidLastname() {
    return (
      (this.lastname?.invalid &&
        (this.lastname?.dirty || this.lastname?.touched)) ||
      (this.isFormSubmitted() && this.lastname?.invalid)
    );
  }

  public isInvalidFirstname() {
    return (
      (this.firstname?.invalid &&
        (this.firstname?.dirty || this.firstname?.touched)) ||
      (this.isFormSubmitted() && this.firstname?.invalid)
    );
  }

  public isInvalidBirthDate() {
    return (
      (this.birthDate?.invalid &&
        (this.birthDate?.dirty || this.birthDate?.touched)) ||
      (this.isFormSubmitted() && this.birthDate?.invalid)
    );
  }

  public isInvalidGender() {
    return (
      (this.gender?.invalid && (this.gender?.dirty || this.gender?.touched)) ||
      (this.isFormSubmitted() && this.gender?.invalid)
    );
  }

  public isInvalidAddress() {
    return (
      (this.address?.invalid &&
        (this.address?.dirty || this.address?.touched)) ||
      (this.isFormSubmitted() && this.address?.invalid)
    );
  }

  public isInvalidPhone() {
    return (
      (this.phone?.invalid && (this.phone?.dirty || this.phone?.touched)) ||
      (this.isFormSubmitted() && this.phone?.invalid)
    );
  }

  /* Methods */

  public submitPatient() {
    this.isLoading.set(true);
    this.isFormSubmitted.set(true);
    console.log('Patient Form:', this.patientForm.value);

    this.validateForm();

    if (!this.patientForm.valid) {
      this.isLoading.set(false);
      return;
    }

    this.updatePatientValues();

    this.patientService.createPatient(this.patient).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/patients'], {
          state: { createSuccess: true },
        });
      },
      error: (error) => {
        this.isLoading.set(false);
        console.error(error);
      },
    });
  }

  private validateForm() {
    /* Validate Lastname & Firstname */
    if (!this.nameRegex.test(this.patientForm.get('lastname')?.value)) {
      this.patientForm.get('lastname')?.setErrors({ invalid: true });
    }

    if (!this.nameRegex.test(this.patientForm.get('firstname')?.value)) {
      this.patientForm.get('firstname')?.setErrors({ invalid: true });
    }

    /* Validate Phone */
    if (
      !this.phoneRegex.test(this.patientForm.get('phone')?.value) ||
      this.patientForm.get('phone')?.value.length !== this.phoneLength
    ) {
      this.patientForm.get('phone')?.setErrors({ invalid: true });
    }
  }

  private updatePatientValues() {
    this.patient.firstname = this.patientForm.get('firstname')?.value;
    this.patient.lastname = this.patientForm.get('lastname')?.value;
    // this.patient.birthDate = this.patientForm.get('birthDate')?.value;
    this.patient.gender = this.patientForm.get('gender')?.value;
    this.patient.address = this.patientForm.get('address')?.value;
    this.patient.phoneNumber = this.patientForm.get('phone')?.value;
  }

  public updateDate(event: any): void {
    console.log('Date of Birth:', event.targetElement.value);

    if (!this.validateBirthdate(event.targetElement.value)) {
      this.patientForm.get('birthDate')?.setErrors({ invalid: true });
    } else {
      this.patient.birthDate = (event.value as Moment).format('YYYY-MM-DD');
    }
  }

  private validateBirthdate(inputDate: string): boolean {
    return (
      this.birthdateRegex.test(inputDate) &&
      moment(inputDate, 'DD/MM/YYYY').isValid()
    );
  }

  public validatePhoneInputValue(event: KeyboardEvent) {
    const key = event.key;
    if (key !== 'Backspace' && !this.onlyNumbersRegex.test(key)) {
      event.preventDefault();
    }
  }
}
