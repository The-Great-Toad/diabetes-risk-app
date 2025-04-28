export class RiskAssessement {
  riskLevel!: string;
  color!: 'success' | 'warning' | 'danger' | 'info';
}

export enum RiskColor {
  NONE = 'success',
  BORDERLINE = 'info',
  IN_DANGER = 'danger',
  EARLY_ONSET = 'warning',
  DEFAULT = 'danger',
}
