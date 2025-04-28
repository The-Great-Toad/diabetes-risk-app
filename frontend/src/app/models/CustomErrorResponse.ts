import { HttpErrorResponse } from '@angular/common/http';

export interface CustomErrorResponse {
  status: number;
  statusText: string;
  message: string;
  url: string;
}
