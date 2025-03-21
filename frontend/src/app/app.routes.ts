import { Routes } from '@angular/router';
import { PatientListComponent } from './components/patient/patient-list/patient-list.component';
import { PatientDetailComponent } from './components/patient/patient-detail/patient-detail.component';
import { PatientFormComponent } from './components/patient/patient-form/patient-form.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { AppComponent } from './app.component';

export const routes: Routes = [
  //   { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '', component: PatientListComponent },
  { path: 'patients', component: PatientListComponent },
  { path: 'patients/view/:id', component: PatientDetailComponent },
  { path: 'patients/edit/:id', component: PatientDetailComponent },
  { path: 'patients/create', component: PatientFormComponent },
  { path: '**', component: PageNotFoundComponent },
];
