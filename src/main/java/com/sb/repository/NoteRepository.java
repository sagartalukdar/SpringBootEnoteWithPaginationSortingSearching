package com.sb.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sb.entity.Note;
import com.sb.entity.Users;

public interface NoteRepository extends JpaRepository<Note,Integer>{

	public List<Note> findByUser(Users user);
	@Query("select u from Note u Where (u.title Like ?1 or u.description Like ?1 or u.date Like ?1) and u.user=?2" )
	public List<Note> getBySearch(String ss,Users user);
	
	@Query("select a from Note a Where a.category=?1 and a.user=?2")
    public List<Note> sortByCategory(String category,Users user);
	@Query("select b from Note b Where b.date=?1 and b.user=?2")
    public List<Note> sortByDate(LocalDate notedate,Users user);
    @Query("select s from Note s Where s.category=?1 and s.date=?2 and s.user=?3")
    public List<Note> sortedByCategoryandDate(String category,LocalDate notedate,Users user);
    
    @Query("select z from Note z Where z.user=?1")
    public Page<Note> findAllByUser(Users user ,Pageable pga);
}
