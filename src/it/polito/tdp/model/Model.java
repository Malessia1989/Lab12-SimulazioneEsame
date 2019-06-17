package it.polito.tdp.model;

import java.util.Collections;
import java.util.Comparator;
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
	
	EventsDao dao;
	Graph<Integer, DefaultWeightedEdge> grafo;
	List<Integer> distretti;
	
	
	public Model() {
		dao= new EventsDao();
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}

	public List<Integer> getAnno() {
		
		return dao.getAnni();
	}

	public void creaGrafo(Integer anno) {
		distretti= dao.getDistrettiByAnno(anno);
		Graphs.addAllVertices(grafo, this.distretti);
		
		for(Integer i1: grafo.vertexSet()) {
			for(Integer i2: grafo.vertexSet()) {
				if(!i1.equals(i2)) {
					if(this.grafo.getEdge(i1, i2) == null) {
						 
						Double LatMediai1=dao.getLatMedia(anno, i1);
						Double LonMediai1=dao.getLonMedia(anno, i1);
						
						Double LatMediai2=dao.getLatMedia(anno, i2);
						Double LonMediai2=dao.getLonMedia(anno, i2);
						
						double distance= LatLngTool.distance(new LatLng(LatMediai1, LonMediai1),
								new LatLng(LatMediai2, LonMediai2),LengthUnit.KILOMETER );
						
						Graphs.addEdgeWithVertices(this.grafo, i1, i2, distance);
						
					} 
					
				}
			}
		}
		
		System.out.println("Grafo creato!");
		System.out.println("# vertici: " + this.grafo.vertexSet().size());
		System.out.println("# archi: " + this.grafo.edgeSet().size());
		
	}
	
	public List <Vicino> getVicini(Integer distretto){
		List<Vicino> vicini= new LinkedList<>();
		List<Integer> neighbor=Graphs.neighborListOf(grafo, distretto);
		for(Integer n: neighbor) {
			//aggiungo alla lista il vicino n e il peso dell'arco(distanza) tra distreto e n
			
			vicini.add(new Vicino(n, grafo.getEdgeWeight(grafo.getEdge(distretto, n)) ));
			
			
		}
		Collections.sort(vicini);
		return vicini;
		
		
	}

	public List<Integer> getDistretti() {
		return distretti;
	}
	
}
