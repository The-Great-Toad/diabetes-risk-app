package com.diabetesrisk.risk_assesment_service.model;

public enum RiskLevel {

    /**
     * patient's notes doesn't contains any triggers
     */
    NONE("None"),
    /**
     * patient's notes contains between two and five triggers, and the patient's age is above 30
     */
    BORDERLINE("Borderline"),
    /**
     * <pre>
     *      Depends on the patient's age and sex ;
     *      - for a male under 30 years old, the patient's notes needs to have at least three triggers
     *      - for a female under 30 years old, the patient's notes needs to have at least four triggers
     *      - for a patient above 30 years old, the patient's notes needs to have at least six triggers
     * </pre>
     */
    IN_DANGER("InDanger"),
    /**
     * <pre>
     *      Depends on the patient's age and sex ;
     *      - for a male under 30 years old, the patient's notes needs to have at least five triggers
     *      - for a female under 30 years old, the patient's notes needs to have at least seven triggers
     *      - for a patient above 30 years old, the patient's notes needs to have at least height triggers
     * </pre>
     */
    EARLY_ONSET("EarlyOnset");

    private final String level;

    RiskLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return level;
    }
}
