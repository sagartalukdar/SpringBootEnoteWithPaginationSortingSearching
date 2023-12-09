package com.sb.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.sb.entity.Note;
import com.sb.entity.Users;

public interface NoteService {

	public Note saveNote(Note note) ;
	public Note getNotesById(int id);
	public List<Note> getNotesByUser(Users user);
	public Note updateNote(Note note);
	public Boolean deleteNote(int id);
	
	public List<Note> getSearches(String ss,Users user);
	
	public List<Note> sortByCategory(String category,Users user);
	public List<Note> sortByDate(String notedate,Users user);
	public List<Note> sortByCategoryandDate(String category,String date,Users user);
}
