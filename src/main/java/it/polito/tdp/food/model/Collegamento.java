package it.polito.tdp.food.model;

public class Collegamento implements Comparable<Collegamento>{
	
	private Food f1;
	private Food f2;
	private Integer con;
	private Double media;
	
	
	public Collegamento(Food f1, Food f2, Integer con, Double media) {
		super();
		this.f1 = f1;
		this.f2 = f2;
		this.con = con;
		this.media = media;
	}
	
	
	public Food getF1() {
		return f1;
	}
	public void setF1(Food f1) {
		this.f1 = f1;
	}
	public Food getF2() {
		return f2;
	}
	public void setF2(Food f2) {
		this.f2 = f2;
	}
	public Integer getCon() {
		return con;
	}
	public void setCon(Integer con) {
		this.con = con;
	}
	public Double getMedia() {
		return media;
	}
	public void setMedia(Double media) {
		this.media = media;
	}


	@Override
	public int compareTo(Collegamento o) {
		return -(this.media.compareTo(o.getMedia()));
	}
	
	

}
