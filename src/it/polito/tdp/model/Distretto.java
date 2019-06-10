package it.polito.tdp.model;

import com.javadocmd.simplelatlng.LatLng;

public class Distretto {
	
	private int id;
	private LatLng latLon;
	
	public Distretto(int id, LatLng latLon) {
		super();
		this.id = id;
		this.latLon = latLon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LatLng getLatLon() {
		return latLon;
	}
	public void setLatLon(LatLng latLon) {
		this.latLon = latLon;
	}
	
	
}
