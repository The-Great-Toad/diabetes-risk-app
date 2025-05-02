import { Component, Input, input, OnInit, output, signal } from '@angular/core';
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
import { RouterLink } from '@angular/router';

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
export class PatientFormComponent implements OnInit {
  public isLoading = signal<boolean>(false);
  public isFormSubmitted = signal<boolean>(false);
  public hasDateOfBirthChanged = signal<boolean>(false);

  public isFormDisabled = input<boolean>(false);
  public patient = input<Patient>({} as Patient);
  public onPatientSubmit = output<Patient>();

  public patientForm!: FormGroup;
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
      firstname: new FormControl(null, [
        Validators.required,
        Validators.pattern(this.nameRegex),
      ]),
      lastname: new FormControl(null, [
        Validators.required,
        Validators.pattern(this.nameRegex),
      ]),
      birthDate: new FormControl(null, [Validators.required]),
      gender: new FormControl(null, [Validators.required]),
      address: new FormControl(null),
      phone: new FormControl(null, [
        Validators.pattern(this.phoneRegex),
        Validators.maxLength(this.phoneLength),
      ]),
    });

    /* Set Form Values */
    if (!this.patient().isNew) {
      console.log('PatientForm - Patient:', this.patient());

      this.patientForm.patchValue({
        firstname: this.patient().firstname,
        lastname: this.patient().lastname,
        birthDate: moment(this.patient().birthDate, 'YYYY-MM-DD'),
        gender: this.patient().gender,
        address: this.patient().address,
        phone: this.patient().phone?.replaceAll('-', ''),
      });
    }
  }

  /* *************************** GETTERS *************************** */
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

  /* *************************** VALIDATIONS *************************** */
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

  /* *************************** METHODS *************************** */

  /**
   * Handles the submission of the patient form.
   * If the form is valid, it updates the patient values and emits the patient to its parent component.
   */
  public submitPatient(): void {
    this.isLoading.set(true);
    this.isFormSubmitted.set(true);
    console.log('Patient Form:', this.patientForm.value);

    if (!this.patientForm.valid) {
      this.isLoading.set(false);
      return;
    }

    this.updatePatientValues();

    this.isLoading.set(false);
    this.isFormSubmitted.set(false);
    this.onPatientSubmit.emit(this.patient());
  }

  /**
   * Updates the patient's values based on the form inputs.
   */
  private updatePatientValues() {
    this.patient().firstname = this.patientForm.get('firstname')?.value;
    this.patient().lastname = this.patientForm.get('lastname')?.value;
    this.patient().gender = this.patientForm.get('gender')?.value;
    this.patient().address = this.patientForm.get('address')?.value;
    this.patient().phone = this.patientForm.get('phone')?.value;
  }

  /**
   * Updates the patient's birth date if the input is valid.
   */
  public updateDate(event: any): void {
    console.log('Date of Birth:', event.targetElement.value);

    if (!this.validateBirthdate(event.targetElement.value)) {
      this.patientForm.get('birthDate')?.setErrors({ invalid: true });
    } else {
      this.patient().birthDate = (event.value as Moment).format('YYYY-MM-DD');
    }
  }

  /**
   * Validates the provided birthdate string.
   *
   * @param inputDate - The birthdate string to validate, expected in the format 'DD/MM/YYYY'.
   * @returns `true` if the input date is in the correct format and represents a valid date; otherwise, `false`.
   */
  private validateBirthdate(inputDate: string): boolean {
    return (
      this.birthdateRegex.test(inputDate) &&
      moment(inputDate, 'DD/MM/YYYY').isValid()
    );
  }

  /**
   * Validates the input value for a phone number field by restricting the allowed keys.
   * Prevents the default action for any key press that is not a number or an allowed control key.
   *
   * @param event - The keyboard event triggered by the user's input.
   */
  public validatePhoneInputValue(event: KeyboardEvent): void {
    const key = event.key;
    const allowedKeys = ['Backspace', 'Delete', 'ArrowLeft', 'ArrowRight'];
    if (!allowedKeys.includes(key) && !this.onlyNumbersRegex.test(key)) {
      event.preventDefault();
    }
  }
}
