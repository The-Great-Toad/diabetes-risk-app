import { MatDateFormats } from '@angular/material/core';

export const MAT_DATE_FORMAT: MatDateFormats = {
  parse: {
    dateInput: 'DD/MM/YYYY', // Date parsed from Input
  },
  display: {
    dateInput: 'DD/MM/YYYY', // Date shown in Input
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};
