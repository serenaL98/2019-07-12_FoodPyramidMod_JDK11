package it.polito.tdp.food;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtPorzioni;

    @FXML
    private TextField txtK;

    @FXML
    private Button btnAnalisi;

    @FXML
    private Button btnCalorie;

    @FXML
    private Button btnSimula;

    @FXML
    private ComboBox<Food> boxFood;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalorie(ActionEvent event) {
    	
    	Food input = this.boxFood.getValue();
    	
    	if(input == null) {
    		txtResult.setText("Non hai selezionato un alimento!");
    		return;
    	}
    	txtResult.appendText("\n\nCinque cibi con massime calorie congiunte a "+input.getDisplay_name()+":\n"+this.model.calorieCongiunte(input));

    	this.btnSimula.setDisable(false);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	
    	String porzioni = this.txtPorzioni.getText();
    	
    	try {
    		Integer porz = Integer.parseInt(porzioni);
    		
    		this.boxFood.getItems().addAll(this.model.cibiPorzione(porz));
    		
    		txtResult.appendText("Crea grafo...");
    		this.model.creaGrafo();
    		txtResult.appendText("\n\n#VERTICI: "+this.model.numeroVertici());
    		txtResult.appendText("\n#ARCHI: "+this.model.numeroArchi());
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero intero positivo per indicare il numero di porzioni!");
    		return;
    	}
    	this.btnCalorie.setDisable(false);
    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	String kappa = this.txtK.getText();
    	Food input = this.boxFood.getValue();
    	
    	if(input== null) {
    		txtResult.setText("Non hai selezionato un alimento!");
    		return;
    	}
    	
    	try {
    		
    		int K = Integer.parseInt(kappa);
    		
    		txtResult.appendText(""+this.model.simula(K, input));
    		
    	}catch(NumberFormatException e) {
    		txtResult.setText("Inserire un numero intero da 1 a 10.");
    		return;
    	}
    	
    }

    @FXML
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.btnCalorie.setDisable(true);
		this.btnSimula.setDisable(true);
	}
}
