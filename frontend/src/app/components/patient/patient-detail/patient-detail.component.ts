import { Component, inject, Input, OnDestroy, signal } from '@angular/core';
import { Patient } from '../../../models/patient';
import { PatientService } from '../../../services/patient.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { provideMomentDateAdapter } from '@angular/material-moment-adapter';
import { MAT_DATE_FORMAT } from '../../../models/MAT_DATE_FORMAT';
import { HttpErrorResponse } from '@angular/common/http';
import moment, { Moment } from 'moment';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { NotesListComponent } from '../../notes/notes-list/notes-list.component';
import { RiskAssessmentComponent } from '../../risk-assessment/risk-assessment.component';
import { PatientFormComponent } from '../patient-form/patient-form.component';

@Component({
  selector: 'app-patient-detail',
  providers: [provideMomentDateAdapter(MAT_DATE_FORMAT)],
  imports: [
    FormsModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressBarModule,
    NotesListComponent,
    PatientFormComponent,
    RiskAssessmentComponent,
  ],
  templateUrl: './patient-detail.component.html',
  styleUrl: './patient-detail.component.css',
})
export class PatientDetailComponent implements OnDestroy {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private patientService = inject(PatientService);

  public isLoading = signal<boolean>(false);
  public isUpdating = signal<boolean>(false);
  public action = signal<string>('');
  public patientId!: number;

  private patient$!: Subscription;
  private subscription: Subscription[] = [];

  public patient!: Patient;

  @Input()
  set id(id: number) {
    this.patientId = id;
    this.patient$ = this.patientService.getPatient(id).subscribe({
      next: (patient: Patient) => {
        this.patient = patient;
        this.isLoading.set(false);
      },
      error: (error: HttpErrorResponse) => {
        console.error(error);
        this.isLoading.set(false);
      },
    });
    this.subscription.push(this.patient$);
  }

  public minDate: Moment = moment().subtract(110, 'year');
  public maxDate: Moment = moment();

  constructor() {
    this.isLoading.set(true);
    this.isUpdating.set(
      this.route.snapshot.routeConfig?.path?.includes('edit') ?? false
    );
  }

  ngOnDestroy(): void {
    this.subscription.forEach((sub) => sub.unsubscribe());
  }

  /**
   * Handles the submission of patient data.
   * On successful update, navigates to the patients-list page.
   */
  public handlePatientSubmission(): void {
    this.patientService.updatePatient(this.patient).subscribe({
      next: () => {
        this.router.navigateByUrl('/patients', {
          state: { updateSuccess: true },
        });
      },
      error: (error: HttpErrorResponse) => {
        console.error(error);
      },
    });
  }
}
