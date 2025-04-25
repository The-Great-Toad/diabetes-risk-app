package com.diabetesrisk.risk_assesment_service.model;

public class Note {

    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Note{");
        sb.append("note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Note builder() {
        return new Note();
    }

    public Note note(String note) {
        this.note = note;
        return this;
    }
}
