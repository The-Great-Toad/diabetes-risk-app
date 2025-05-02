import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Patient } from '../../../models/patient';
import { PatientService } from '../../../services/patient.service';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { PatientFormComponent } from '../patient-form/patient-form.component';

@Component({
  selector: 'app-patient-create',
  imports: [PatientFormComponent, MatProgressBarModule],
  templateUrl: './patient-create.component.html',
  styleUrl: './patient-create.component.css',
})
export class PatientCreateComponent {
  private patientService: PatientService = inject(PatientService);
  private router: Router = inject(Router);

  public isLoading = signal<boolean>(false);

  public patient: Patient = new Patient();

  constructor() {
    this.patient.isNew = true;
  }

  public handlePatientSubmission() {
    this.isLoading.set(true);
    console.log('Patient:', this.patient);

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
}
