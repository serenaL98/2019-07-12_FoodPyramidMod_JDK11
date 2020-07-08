package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDAO;

public class Model {
	
	private FoodDAO dao;
	private List<Food> cibiPorz;
	
	//grafo semplice, pesato, non orientato
	private Graph<Food, DefaultWeightedEdge>grafo;
	private Map<Integer, Food> mappaCibi;
	private List<Collegamento> collegamenti;
	
	private List<Collegamento> ordinata;
	
	private Simulatore sim;
	
	public Model() {
		this.dao = new FoodDAO();
		this.cibiPorz = new ArrayList<>();
		this.mappaCibi = new HashMap<>();
		this.collegamenti = new ArrayList<>();
		
		this.sim = new Simulatore();
	}
	
	public List<Food> cibiPorzione(int porz){
		this.cibiPorz = this.dao.cibiPorzione(porz);
		
		return this.cibiPorz;
	}
	
	public void creaGrafo() {
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//VERTICI
		Graphs.addAllVertices(this.grafo, this.cibiPorz);
		
		//ARCHI
		for(Food f: this.cibiPorz) {
			mappaCibi.put(f.getFood_code(), f);
		}
		this.collegamenti = this.dao.prendiCollegamenti(mappaCibi);
		
		for(Collegamento c: this.collegamenti) {
			Graphs.addEdge(this.grafo, c.getF1(), c.getF2(), c.getMedia());
		}
		
	}

	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public String calorieCongiunte(Food input) {
		
		this.ordinata = new ArrayList<>();
	
		for(DefaultWeightedEdge d: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeSource(d).equals(input) )
				ordinata.add(new Collegamento(input, this.grafo.getEdgeTarget(d), null, this.grafo.getEdgeWeight(d)));
			if(this.grafo.getEdgeTarget(d).equals(input))
				ordinata.add(new Collegamento(input, this.grafo.getEdgeSource(d), null, this.grafo.getEdgeWeight(d)));
		}
		
		Collections.sort(ordinata);
		
		System.out.println(ordinata.size());
		System.out.println(Graphs.neighborListOf(this.grafo, input).size());
		
		String stampa = "";
		
		if(ordinata.size()== 0) {
			stampa = "Non sono presenti cibi con calorie congiunte per questo alimento.\n";
		}else if(ordinata.size()<5) {
				stampa = "Non sono presenti cinque cibi con calorie massime!\n"+input.getDisplay_name()+" contiene "+ordinata.size()+" alimenti congiunti.";
			}else {
				for(int i=0; i<5; i++) {
					stampa += ordinata.get(i).getF2().getDisplay_name()+" -> "+ordinata.get(i).getMedia()+" cal\n";
				}
			}
		
		return stampa;
	}
	
	
	//---PUNTO 2---
	
	public String simula(int K, Food input) {
		this.sim.inizio(K, input, this.grafo);
		this.sim.avvia();
		
		String stampa = "Preparati "+this.sim.getPreparati()+" cibi in "+this.sim.getTempo()+" minuti.";
		
		return stampa;
	}
	
}
