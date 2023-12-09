package com.sb.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.sb.entity.Note;
import com.sb.entity.Users;
import com.sb.repository.NoteRepository;
@Service
public class NoteServiceimpl implements NoteService{

	@Autowired
	private NoteRepository nr;
	@Override
	public Note saveNote(Note note) {
		// TODO Auto-generated method stub
		Note n =nr.save(note);
		return n;
	}

	@Override
	public Note getNotesById(int id) {
		// TODO Auto-generated method stub
		return nr.findById(id).get();
	}

	@Override
	public List<Note> getNotesByUser(Users user) {
		// TODO Auto-generated method stub
		List<Note> nl=nr.findByUser(user);
		return nl;
	}

	@Override
	public Note updateNote(Note note) {
		// TODO Auto-generated method stub
		return nr.save(note);
	}

	@Override
	public Boolean deleteNote(int id) {
		// TODO Auto-generated method stub
		Note note=nr.findById(id).get();
		if(note!=null) {
			nr.delete(note);
			return true;
		}
		return false;
	}

	@Override
	public List<Note> getSearches(String ss,Users user) {
		String S="%"+ss+"%";
		List<Note> nl= nr.getBySearch(S,user);
		return nl;
	}

	@Override
	public List<Note> sortByCategory(String category,Users user) {
		// TODO Auto-generated method stub
		return nr.sortByCategory(category,user);
	}

	@Override
	public List<Note> sortByDate(String notedate,Users user) {
		// TODO Auto-generated method stub
		LocalDate ld=LocalDate.parse(notedate);
		return nr.sortByDate(ld,user);
	}

	@Override
	public List<Note> sortByCategoryandDate(String category, String date,Users user) {
		// TODO Auto-generated method stub
		LocalDate ld=LocalDate.parse(date);
		return nr.sortedByCategoryandDate(category,ld,user);
	}

}
