package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Evento.EventType;

public class Simulatore {
	
	//INPUT
	private int K;	//#stazioni
	private Graph<Food, DefaultWeightedEdge> grafo;
	
	//OUTPUT
	private int preparati;
	private double tempo;
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Evento> coda;
	
	//MODELLO DEL MONDO
	private List<Food> daPreparare;	//lista dei cibi congiunti a quello selezionato
	private List<Food> rimanenti;
	private Map<Integer, Food> stazioneCibo;
	
	//STAMPA OUTPUT
	public int getPreparati() {
		return preparati;
	}

	public double getTempo() {
		return tempo;
	}
	
	//SIMULAZIONE
	public void inizio(int K, Food input, Graph<Food, DefaultWeightedEdge> grafo) {
		this.K = K;
		this.daPreparare = new ArrayList<>(grafo.vertexSet());
		this.preparati = 0;
		this.tempo = 0;
		this.grafo = grafo;
		this.coda = new PriorityQueue<>();
		this.rimanenti= new ArrayList<>(this.daPreparare);
		this.stazioneCibo = new HashMap<>();
		
		this.stazioneCibo.put(1, input);
		
		List<Collegamento> ordine = new ArrayList<>(this.listaOrdinata(input, grafo));
		
		//riempio la coda con tutti i cibi da preparare
		for(int i=2; i<this.K && i<ordine.size(); i++) {
			stazioneCibo.put(i, ordine.get(i).getF2());
			
			//ho fatto processare i cibi e quindi sono preparati
			Evento fine = new Evento(ordine.get(i).getF2(), ordine.get(i).getMedia(), EventType.PREPARATO);
			this.coda.add(fine);
		}
		
	}
	
	public void avvia() {
		while(!this.coda.isEmpty()) {
			Evento e = this.coda.poll();
			processEvent(e);
		}
	}

	public void processEvent(Evento e) {
		
		switch(e.getTipo()) {
		
		case ARRIVO:
			
			//devo trovare un cibo adiacente a quello appena preparato
			List<Collegamento> vicini = new ArrayList<>(this.listaOrdinata(e.getCibo(), grafo));
			Collegamento prossimo = null;
			
			for(Collegamento c: vicini) {
				for(Food f: this.rimanenti) {
					if(f.equals(c.getF2())) {
						prossimo = c;
						break;
					}
				}
			}
			
			if(prossimo != null) {
				//la metto nella stazione che non ha cibo
				for(int i=0; i<this.stazioneCibo.size(); i++) {
					if(stazioneCibo.get(i)==null) {
						stazioneCibo.put(i, prossimo.getF2());
						
						//creo evento di fine preparazione
						Evento fine = new Evento(prossimo.getF2(), e.getTempo()+prossimo.getMedia(), EventType.PREPARATO);
						this.coda.add(fine);
						
						break;
					}
				}
			}
			
			break;
			
		case PREPARATO:
			this.preparati++;
			this.tempo = e.getTempo();
			this.rimanenti.remove(e.getCibo());
			
			int indice = 0;
			for(int i=0; i<this.stazioneCibo.size(); i++) {
				if(stazioneCibo.get(i).equals(e.getCibo()))
					indice=i;
			}
			//ho rimosso il cibo che ha finito di essere preparato per lasciare la stazione libera
			stazioneCibo.remove(indice);
			
			//schedulo l'arrivo del successivo
			Evento nuovo = new Evento(e.getCibo(), e.getTempo(), EventType.ARRIVO);
			this.coda.add(nuovo);
			
			break;
			
		}
	}
	
	public List<Collegamento> listaOrdinata(Food input, Graph<Food, DefaultWeightedEdge> grafo){
		
		List<Collegamento> ordine = new ArrayList<>();
		List<Food> vicini = new ArrayList<>(Graphs.neighborListOf(grafo, input));
		for(Food f: vicini) {
			Double peso = grafo.getEdgeWeight(grafo.getEdge(input, f));
			ordine.add(new Collegamento(input, f, null, peso));
		}
		
		Collections.sort(ordine);
		
		return ordine;
	}

}
