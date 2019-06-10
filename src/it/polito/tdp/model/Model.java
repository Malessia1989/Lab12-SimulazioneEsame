package it.polito.tdp.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<Distretto,DefaultWeightedEdge > grafo;
	
	public Model() {
		 
		dao= new EventsDao();
	}

	public List <Integer> getAnni() {
		
		return dao.getAnni();
	}
	
	public String creaGrafo(Integer anno) {
		
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Distretto> =dao.creaGrafo(anno);
		
		return null;
	}
	
}
