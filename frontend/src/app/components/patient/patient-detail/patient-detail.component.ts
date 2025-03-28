import { Component, inject, Input, OnDestroy, signal } from '@angular/core';
import { Patient } from '../../../models/patient';
import { PatientService } from '../../../services/patient.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import {
  MatDatepickerInputEvent,
  MatDatepickerModule,
} from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { provideMomentDateAdapter } from '@angular/material-moment-adapter';
import { MAT_DATE_FORMAT } from '../../../models/MAT_DATE_FORMAT';
import { HttpErrorResponse } from '@angular/common/http';
import moment, { Moment } from 'moment';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { NotesListComponent } from '../../notes/notes-list/notes-list.component';

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
  public isConsulting = signal<boolean>(false);
  public action = signal<string>('');
  public hasDateOfBirthChanged = signal<boolean>(false);

  private patient$!: Subscription;
  private subscription: Subscription[] = [];

  @Input()
  set id(id: number) {
    this.patient$ = this.patientService.getPatient(id).subscribe((patient) => {
      this.patient = patient;
      console.log(`${this.action()} patient: ${this.patient}`);
      this.isLoading.set(false);
    });
    this.subscription.push(this.patient$);
  }

  public patient!: Patient;
  //   public patient!: Signal<Patient | undefined>;

  public minDate: Moment = moment().subtract(110, 'year');
  public maxDate: Moment = moment();

  constructor() {
    this.isLoading.set(true);
    const isUpdating: boolean = this.route.snapshot.routeConfig?.path
      ? this.route.snapshot.routeConfig.path.includes('edit')
      : false;
    this.isUpdating.set(isUpdating);
    this.isConsulting.set(!isUpdating);
  }

  ngOnDestroy(): void {
    this.subscription.forEach((sub) => sub.unsubscribe());
  }

  updateDate(event: MatDatepickerInputEvent<Date>): void {
    this.hasDateOfBirthChanged.set(true);
  }

  public updatePatient(): void {
    if (this.hasDateOfBirthChanged()) {
      this.patient.birthDate = moment(this.patient.birthDate).format(
        'YYYY-MM-DD'
      );
    }

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
