package it.polito.tdp.model;

public class Vicino implements Comparable<Vicino>{
	
	Integer distretto;
	Double  distanza;
	public Vicino(Integer distretto, Double distanza) {
		super();
		this.distretto = distretto;
		this.distanza = distanza;
	}
	public Integer getDistretto() {
		return distretto;
	}
	public void setDistretto(Integer distretto) {
		this.distretto = distretto;
	}
	public double getDistanza() {
		return distanza;
	}
	public void setDistanza(double distanza) {
		this.distanza = distanza;
	}
	@Override
	public int compareTo(Vicino altro) {
		// TODO Auto-generated method stub
		return this.distanza.compareTo(altro.distanza);
	}
	
}
