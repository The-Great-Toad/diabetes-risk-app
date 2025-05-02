import { Routes } from '@angular/router';
import { PatientListComponent } from './components/patient/patient-list/patient-list.component';
import { PatientDetailComponent } from './components/patient/patient-detail/patient-detail.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { PatientCreateComponent } from './components/patient/patient-create/patient-create.component';

export const routes: Routes = [
  { path: '', redirectTo: 'patients', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'patients',
    canActivate: [AuthGuard],
    children: [
      { path: '', component: PatientListComponent },
      { path: 'view/:id', component: PatientDetailComponent },
      { path: 'edit/:id', component: PatientDetailComponent },
      { path: 'create', component: PatientCreateComponent },
    ],
  },
  { path: '**', canActivate: [AuthGuard], component: PageNotFoundComponent },
];
