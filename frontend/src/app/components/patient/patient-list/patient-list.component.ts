import { Component, inject, OnInit, signal, viewChild } from '@angular/core';
import { Patient } from '../../../models/patient';
import { PatientService } from '../../../services/patient.service';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faEye, faEdit, faPlus } from '@fortawesome/free-solid-svg-icons';
import { MatTooltipModule } from '@angular/material/tooltip';

@Component({
  selector: 'app-patient-list',
  imports: [
    MatTableModule,
    MatProgressBarModule,
    MatPaginatorModule,
    MatIconModule,
    MatTooltipModule,
    FontAwesomeModule,
    RouterLink,
  ],
  templateUrl: './patient-list.component.html',
  styleUrl: './patient-list.component.css',
})
export class PatientListComponent implements OnInit {
  private patientService: PatientService = inject(PatientService);
  private router: Router = inject(Router);

  public isLoading = signal<boolean>(false);
  public isUpdateSuccessful = signal<boolean>(false);
  public isAddSuccessful = signal<boolean>(false);

  public patients: Patient[] = [];
  public addPatientTooltip: string = 'Add a new patient';

  public dataSource!: MatTableDataSource<Patient>;
  public displayedColumns: string[] = [
    'id',
    'lastname',
    'firstname',
    'birthDate',
    'gender',
    'address',
    'phoneNumber',
    'actions',
  ];

  paginator = viewChild.required<MatPaginator>('paginator');

  public faEye = faEye;
  public faEdit = faEdit;
  public faPlus = faPlus;

  constructor() {
    const navigation = this.router.getCurrentNavigation();

    if (navigation?.extras?.state) {
      /* check for update */
      if (navigation.extras.state['updateSuccess']) {
        this.isUpdateSuccessful.set(navigation.extras.state['updateSuccess']);
      }

      /* check for add */
      if (navigation.extras.state['createSuccess']) {
        this.isAddSuccessful.set(navigation.extras.state['createSuccess']);
      }
    }
  }

  ngOnInit() {
    this.isLoading.set(true);

    this.patientService.getPatients().subscribe((patients: Patient[]) => {
      this.patients = patients;
      this.isLoading.set(false);
      this.initDataSource();
    });
  }

  private initDataSource(): void {
    this.dataSource = new MatTableDataSource<Patient>(this.patients);
    this.dataSource.paginator = this.paginator();
  }

  public onEdit(patient: Patient): void {
    // console.log('edit: ', patient);
    this.router.navigate(['patients/form', patient.id]);
  }

  public onView(patient: Patient): void {
    // console.log('view: ', patient);
    this.router.navigate(['patients/detail', patient.id]);
  }
}
