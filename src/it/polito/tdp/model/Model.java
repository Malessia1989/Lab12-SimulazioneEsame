package it.polito.tdp.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;

public class Model {
	
	Graph<Integer, DefaultWeightedEdge> grafo;
	private List<Integer> distretti;
	private EventsDao dao;
	
	
	
	public Model() {
		dao= new EventsDao();
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
	}
	
	public void creaGrafo(Integer anno) {
		this.distretti=dao.listAllDistrict(anno);
		
		Graphs.addAllVertices(grafo, this.distretti);
		
		for(Integer i1: this.grafo.vertexSet()) {
			for(Integer i2: this.grafo.vertexSet()) {
				//fare != non funziona! è una classe Integer!!!
				if(!i1.equals(i2)) {
					if(this.grafo.getEdge(i1, i2) == null) {
						Double latMedia1= dao.getLatMedia(anno,i1);
						Double lonMedia1= dao.getLonMedia(anno, i1);
						
						Double latMedia2= dao.getLatMedia(anno, i2);
						Double lonMedia2= dao.getLonMedia(anno, i2);
						
						
						Double distanzaMedia= LatLngTool.distance(new LatLng(latMedia1, lonMedia1),
																	new LatLng(latMedia2,lonMedia2), LengthUnit.KILOMETER);
						Graphs.addEdgeWithVertices(this.grafo,i1, i2, distanzaMedia);
					}
				}
			} 
		}
		System.out.println("");
		
		
	}
	
	public List<Vicino> getVicini(Integer distretto){
		List<Vicino> vicini= new LinkedList<>();
		List<Integer> neighbor= Graphs.neighborListOf(grafo,distretto);
		for(Integer n: neighbor) {
			vicini.add(new Vicino(n, this.grafo.getEdgeWeight(this.grafo.getEdge(distretto, n))));
		}
		
		Collections.sort(vicini);
		return vicini;
	}

	public List<Integer> getAnni() {
		// TODO Auto-generated method stub
		return dao.getAnni();
	}

	public List<Integer> getDistretti() {
		return distretti;
	}
	public int simula(Integer anno, Integer mese, Integer giorno, Integer N) {
		Simulatore s= new Simulatore();
		s.init(N, anno, mese, giorno, grafo);
		return s.run();
		
	}
	
}
