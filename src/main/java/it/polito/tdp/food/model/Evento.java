package it.polito.tdp.food.model;

public class Evento implements Comparable<Evento>{
	
	public enum EventType{
		ARRIVO, PREPARATO
	}

	private Food cibo;
	private Double tempo;
	private EventType tipo;
	
	public Evento(Food cibo, Double tempo, EventType tipo) {
		super();
		this.cibo = cibo;
		this.tempo = tempo;
		this.tipo = tipo;
	}
	
	public Food getCibo() {
		return cibo;
	}
	
	public Double getTempo() {
		return tempo;
	}
	
	public EventType getTipo() {
		return tipo;
	}
	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

	@Override
	public int compareTo(Evento o) {
		return this.tempo.compareTo(o.getTempo());
	}
	
}
