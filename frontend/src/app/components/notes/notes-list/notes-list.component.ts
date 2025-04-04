import {
  Component,
  inject,
  input,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { Note } from '../../../models/note';
import { NotesService } from '../../../services/notes.service';
import { MatListModule } from '@angular/material/list';
import { NgFor } from '@angular/common';
import { faNoteSticky, faPlus } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NotesFormComponent } from '../notes-form/notes-form.component';
import { Patient } from '../../../models/patient';

@Component({
  selector: 'app-notes-list',
  imports: [FontAwesomeModule, NgFor, NotesFormComponent, MatListModule],
  templateUrl: './notes-list.component.html',
  styleUrl: './notes-list.component.css',
})
export class NotesListComponent implements OnInit, OnDestroy {
  private noteService = inject(NotesService);

  public showNewNoteForm = signal<boolean>(false);

  private notes$!: Subscription;
  private subscriptions: Subscription[] = [];

  public patient = input<Patient>({} as Patient);
  public notes: Note[] = [];
  public faNote = faNoteSticky;
  public faPlus = faPlus;

  ngOnInit(): void {
    this.getPatientNotes();

    this.subscriptions.push(this.notes$);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  private getPatientNotes() {
    this.notes$ = this.noteService
      .getPatientNotes(this.patient().id)
      .subscribe({
        next: (notes) => {
          this.notes = notes;
          console.log(`Notes for patient ${this.patient().id}:`, this.notes);
        },
        error: (error) => {
          console.error('Error fetching notes:', error);
        },
      });
  }

  public openAddNoteDialog() {
    this.showNewNoteForm.set(true);
  }

  public onNoteAdded(newNote: Note | null) {
    if (newNote) {
      this.notes.push(newNote as Note);
      this.notes.sort(
        (a, b) => new Date(b.date).getTime() - new Date(a.date).getTime()
      );
      //   this.getPatientNotes();
    }
    this.showNewNoteForm.set(false);
  }
}
