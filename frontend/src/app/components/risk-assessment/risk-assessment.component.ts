import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  inject,
  input,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { RiskAssessement, RiskColor } from '../../models/risk-assessement';
import { RiskAssessmentService } from '../../services/risk-assessment.service';
import { Subscription } from 'rxjs';
import { CustomErrorResponse } from '../../models/CustomErrorResponse';

@Component({
  selector: 'app-risk-assessment',
  imports: [],
  templateUrl: './risk-assessment.component.html',
  styleUrl: './risk-assessment.component.css',
})
export class RiskAssessmentComponent implements OnInit, OnDestroy {
  private riskAssessmentService = inject(RiskAssessmentService);

  public id = input<number>(0);
  public riskAssessment = signal<RiskAssessement>({} as RiskAssessement);

  private risk$!: Subscription;
  private subscription: Subscription[] = [];

  ngOnInit(): void {
    this.risk$ = this.riskAssessmentService
      .getRiskAssessment(this.id())
      .subscribe({
        next: (riskAssessment: string) => {
          this.initRiskAssessment(riskAssessment);
        },
        error: (error: CustomErrorResponse) => {
          console.log(error);
          this.initRiskAssessment('SERVICE_UNAVAILABLE');
        },
      });

    this.subscription.push(this.risk$);
  }

  ngOnDestroy(): void {
    this.subscription.forEach((sub) => sub.unsubscribe());
  }

  /**
   * Initializes the risk assessment object with the risk level and color based on the risk assessment value.
   *
   * @param riskAssessment Risk assessment value from the backend
   */
  private initRiskAssessment(riskAssessment: string) {
    switch (riskAssessment) {
      case 'NONE':
        this.riskAssessment.set({
          riskLevel: riskAssessment,
          color: RiskColor.NONE,
        });
        break;
      case 'BORDERLINE':
        this.riskAssessment.set({
          riskLevel: riskAssessment,
          color: RiskColor.BORDERLINE,
        });
        break;
      case 'IN_DANGER':
        this.riskAssessment.set({
          riskLevel: riskAssessment,
          color: RiskColor.IN_DANGER,
        });
        break;
      case 'EARLY_ONSET':
        this.riskAssessment.set({
          riskLevel: riskAssessment,
          color: RiskColor.EARLY_ONSET,
        });
        break;
      default:
        this.riskAssessment.set({
          riskLevel: riskAssessment,
          color: RiskColor.DEFAULT,
        });
    }
    // console.log('Risk assessment: ', this.riskAssessment());
  }
}
