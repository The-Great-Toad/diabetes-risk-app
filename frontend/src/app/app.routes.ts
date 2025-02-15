import { Routes } from '@angular/router';
import { PatientListComponent } from './components/patient/patient-list/patient-list.component';
import { PatientDetailComponent } from './components/patient/patient-detail/patient-detail.component';
import { PatientFormComponent } from './components/patient/patient-form/patient-form.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { AppComponent } from './app.component';

export const routes: Routes = [
  //   { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '/', component: AppComponent },
  { path: 'patient-list', component: PatientListComponent },
  { path: 'patient-detail', component: PatientDetailComponent },
  { path: 'patient-form', component: PatientFormComponent },
  { path: '**', component: PageNotFoundComponent },
];
