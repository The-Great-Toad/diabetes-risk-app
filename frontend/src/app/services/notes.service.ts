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

  public getPatientNotes(patientId: number): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.apiUrl}/${patientId}`);
  }

  public addNote(note: Note): Observable<Note> {
    return this.http.post<Note>(this.apiUrl, note);
  }
}
