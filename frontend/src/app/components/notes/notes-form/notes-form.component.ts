import {
  Component,
  inject,
  input,
  OnDestroy,
  output,
  signal,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { NotesService } from '../../../services/notes.service';
import { Patient } from '../../../models/patient';
import { Note } from '../../../models/note';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-notes-form',
  imports: [
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatProgressBarModule,
  ],
  templateUrl: './notes-form.component.html',
  styleUrl: './notes-form.component.css',
})
export class NotesFormComponent implements OnDestroy {
  private noteService = inject(NotesService);

  public patient = input<Patient>({} as Patient);
  public noteAdded = output<Note | null>();

  public newNote = signal<Note>({} as Note);
  public isLoading = signal<boolean>(false);

  private notes$!: Subscription;
  private subscriptions: Subscription[] = [];

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  public addNote() {
    this.isLoading.set(true);
    this.completeNewNote();
    console.log('Adding note:', this.newNote());

    this.notes$ = this.noteService.addNote(this.newNote()).subscribe({
      next: (note) => {
        console.log('Note added:', note);
        this.newNote.set({} as Note);
        this.isLoading.set(false);
        this.noteAdded.emit(note);
      },
      error: (error) => {
        console.error('Error adding note:', error);
      },
    });
    this.subscriptions.push(this.notes$);
  }

  private completeNewNote() {
    this.newNote.set({
      ...this.newNote(),
      patientId: this.patient().id,
      patient: this.patient().lastname,
      date: new Date().toISOString(),
    } as Note);
  }

  public cancelNewNote() {
    this.newNote.set({} as Note);
    this.noteAdded.emit(null);
  }
}
