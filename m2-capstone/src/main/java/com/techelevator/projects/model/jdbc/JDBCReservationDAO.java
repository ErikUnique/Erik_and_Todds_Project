package com.techelevator.projects.model.jdbc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Park;
import com.techelevator.projects.model.Reservation;
import com.techelevator.projects.model.ReservationDAO;
import com.techelevator.projects.model.Site;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;   
	public JDBCReservationDAO(DataSource dataSource) {
		
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	
	}
	


	
	@Override
	public Reservation saveReservation(Reservation reservationObject) {
		
		ArrayList<Reservation> reservationList = new ArrayList <Reservation>();
		String sqlCreateReservation = "INSERT INTO reservation (site_id, name, from_date, to_date) VALUES (?, ?, ?, ?)";
		
		jdbcTemplate.update(sqlCreateReservation, reservationObject.getSite_id(), reservationObject.getName(), reservationObject.getFrom_date(), reservationObject.getTo_date());
		
		String name = reservationObject.getName();
		int site_id = reservationObject.getSite_id();
		String sqlGetReservation = "SELECT reservation_id from reservation WHERE name = ? AND site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservation, name, site_id);
		
		while(results.next()) {
			
			Reservation newReservationObject = createReservationObject(results);
			
			reservationList.add(newReservationObject);
		
		}
		return reservationList.get(reservationList.size()-1);
			
		
		
	}
	
	private Reservation createReservationObject(SqlRowSet results) {
		
		Reservation reservationObject = new Reservation();
		
		reservationObject.setReservation_id(results.getInt("reservation_id"));
		reservationObject.setSite_id(results.getInt("site_id"));
		reservationObject.setName(results.getString("name"));
		reservationObject.setFrom_date(results.getDate("from_date").toLocalDate());
		reservationObject.setTo_date(results.getDate("to_date").toLocalDate());
		reservationObject.setCreate_date(results.getDate("create_date").toLocalDate());
		
		return reservationObject;
		
	}
		

	
}
