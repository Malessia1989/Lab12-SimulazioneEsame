package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.model.Event;


public class EventsDao {
	
	public List<Event> listAllEventsByDate(Integer anno, Integer mese, Integer giorno){
		String sql = "SELECT * " + 
				"FROM events where Year(reported_date)=? " + 
				"and month(reported_date)=? and day(reported_date)=? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, mese);
			st.setInt(3, giorno);
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Integer> listAllDistrict(Integer anno) {
		String sql="select distinct district_id id " + 
				"from events e " + 
				"where year(e.reported_date)=? ";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			
			ResultSet res = st.executeQuery() ;
			List<Integer> distretti=new ArrayList<>();
			
			while(res.next()) {
				try {
					int id=res.getInt("id");
					distretti.add(id);
				
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return distretti ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public Double getLatMedia(Integer anno,Integer distretto) {
		String sql="select AVG(geo_lat) as media from events "
				+ "where Year(reported_date)= ? and district_id=? "; 
		
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, distretto);
			
			ResultSet res = st.executeQuery() ;			
			if(res.next()) {
				try {
					conn.close();
					return res.getDouble("media");
				
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return null ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}

	public Double getLonMedia(Integer anno, Integer distretto) {
		String sql="select AVG(geo_lon) as media from events "
				+ "where Year(reported_date)= ? and district_id=? "; 
		
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setInt(2, distretto);
			
			ResultSet res = st.executeQuery() ;
			
			
			if(res.next()) {
				try {
					conn.close();
					return res.getDouble("media");
				
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return null ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Integer> getAnni() {
		String sql="select distinct year(reported_date) as anno " + 
				"from `events` ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
				
			ResultSet res = st.executeQuery() ;
			List<Integer > anni= new ArrayList<>();
			
			while(res.next()) {
				try {
					anni.add(res.getInt("anno"));
				
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public Integer getDistrettoMin(Integer anno) {
		
		String sql="select e.district_id id " + 
				"from `events` e " + 
				"where  Year(e.reported_date)=? " + 
				"group by e.district_id " + 
				"order by count(*) asc " + 
				" limit 1 ";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);	
			ResultSet res = st.executeQuery() ;
			
			
			if(res.next()) {
				try {
					conn.close();
					return res.getInt("id");
				
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return null;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

}
