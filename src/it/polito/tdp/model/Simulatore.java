package it.polito.tdp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.db.EventsDao;
import it.polito.tdp.model.Evento.TipoEvento;

public class Simulatore {
	//tipi di evento
	// 1. evento criminoso
	//		1.1 La centrale seleziona l'agente piu vicino
	
	// 1.2 Setta l'agente a occupato
	// 2. Arriva agente 
	//      2.1 definisco quanto dura l'intervento
	// 		2.2 controlla se l'evento � mal gestito 
	// 3. Crimine terminato
	//		3.1 Libero l'agente                                                                                              
	
	// Strutture dati che ci servono
	
	private Integer malGestiti;
	//parametri 
	private Integer N;
	private Integer anno;
	private Integer mese;
	private Integer giorno;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private PriorityQueue<Evento> queue;
	
	//mappa di distretto-numAgenti
	private Map<Integer, Integer> agenti;
	
	
	public void init(Integer N, Integer anno, Integer mese, Integer giorno,
			Graph<Integer,DefaultWeightedEdge> grafo) {
		
		this.N=N;
		this.anno=anno;
		this.mese=mese;
		this.giorno=giorno;
		this.grafo=grafo;
		
		this.malGestiti=0;
		this.agenti= new HashMap<>();
		for(Integer d: grafo.vertexSet()) {
			this.agenti.put(d, 0);
		}
		
		// devo scegliere dove sta la centrale
		EventsDao dao= new EventsDao();
		Integer minD= dao.getDistrettoMin(anno);
		this.agenti.put(minD, this.N);
		
		//creo la coda
		this.queue= new PriorityQueue<>();
		
		for(Event e: dao.listAllEventsByDate(anno, mese, giorno)) {
			queue.add(new Evento(TipoEvento.CRIMINE, e.getReported_date(), e));
			
		}
		
	}
	
	public void run() {
		Evento e;	
		
		while (( e= queue.poll()) != null){
			switch (e.getTipo()) {
			
			case CRIMINE:
				System.out.println("NUOVO CRIMINE!" + e.getCrimine().getIncident_id());
				Integer partenza = null;
				partenza = cercaAgente(e.getCrimine().getDistrict_id());

				if (partenza != null) {
					// c'� un agente libero
					if (partenza.equals(e.getCrimine().getDistrict_id())) {
						// tempo arrivo =0 : � come il caso ARRIVA_AGENTE
						System.out.println("AGENTE ARRIVA PER CRIMINE " +e.getCrimine().getIncident_id()); 
						Long duration= getDuration(e.getCrimine().getOffense_category_id());
						this.queue.add(new Evento(TipoEvento.GESTITO, e.getData().plusSeconds(duration), e.getCrimine()));
						
						// agente lontano: recupero la distanza e calcolare quanto ci mette ad arrivare
					} else {
						Double distance = this.grafo.getEdgeWeight(this.grafo.getEdge(partenza,
								e.getCrimine().getDistrict_id()));
						Long seconds = (long) ((distance * 1000) / (60 / 3.6));
						this.queue.add(
								new Evento(TipoEvento.ARRIVA_AGENTE, e.getData().plusSeconds(seconds), e.getCrimine()));
					}

						Long duration = getDuration(e.getCrimine().getOffense_category_id());
						this.queue
								.add(new Evento(TipoEvento.GESTITO, e.getData().plusSeconds(duration), e.getCrimine()));	
				}				
				break;
				
			case ARRIVA_AGENTE:
				System.out.println("AGENTE ARRIVA PER CRIMINE " +e.getCrimine().getIncident_id()); 
				Long duration= getDuration(e.getCrimine().getOffense_category_id());
				this.queue.add(new Evento(TipoEvento.GESTITO, e.getData().plusSeconds(duration), e.getCrimine()));
				
				//controllo se il crimine � mal gestito
				if(e.getData().isAfter(e.getCrimine().getReported_date().plusMinutes(15))) {
					System.out.println("CRIMINE "+ e.getCrimine().getIncident_id() + "MAL GESTITO!!!");
					this.malGestiti++;
				}
				break;

			case GESTITO:
				System.out.println("CRIMINE " + e.getCrimine().getIncident_id() + "GESTITO!");
				this.agenti.put(e.getCrimine().getDistrict_id(), this.agenti.get(e.getCrimine().getDistrict_id() + 1));
				break;
			}
		}
		System.out.println("TERMINATO !! MAL GESTITI: "+ this.malGestiti);
	}

	private Long getDuration(String offense_category_id) {
		if (offense_category_id.equals("all_other_crimes")) {
			Random r = new Random();
			if (r.nextDouble() > 0.5)
				return Long.valueOf(2 * 60 * 60);
			else
				return Long.valueOf(1 * 60 * 60);
		} else {
			return Long.valueOf(2 * 60 * 60);
		}
	}
	private Integer cercaAgente(Integer district_id) {
		Double distanza= Double.MAX_VALUE;	//setto un valore molto alto
		Integer distretto=null;
		
		for(Integer d: agenti.keySet()) {
			if(this.agenti.get(d) > 0) {
				if(district_id.equals(d)) {
					//distanza=0
					distanza= Double.valueOf(0);
					distretto=d;
									
				}
				else if( this.grafo.getEdgeWeight(this.grafo.getEdge(district_id, d)) < distanza) {
					distanza = this.grafo.getEdgeWeight(this.grafo.getEdge(district_id, d));
					distretto=d;
				}
			}
		}
		return distretto;
	}
	
	
}
