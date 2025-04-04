import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Note } from '../models/note';

@Injectable({
  providedIn: 'root',
})
export class NotesService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/notes';

  /**
   * Retrieves a list of notes for a given patient id.
   *
   * @param patientId patient id to get notes for
   * @returns A list of notes for the patient
   */
  public getPatientNotes(patientId: number): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.apiUrl}/${patientId}`);
  }

  /**
   * Adds a new note to the medical record of a patient.
   *
   * @param note The note to be added
   * @returns The added note
   */
  public addNote(note: Note): Observable<Note> {
    return this.http.post<Note>(this.apiUrl, note);
  }
}
