/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnUtenteSimile"
    private Button btnUtenteSimile; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbAnno"
    private ComboBox<Integer> cmbAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="cmbUtente"
    private ComboBox<User> cmbUtente; // Value injected by FXMLLoader

    @FXML // fx:id="txtX1"
    private TextField txtX1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	if (this.txtN.getText() == "") {
    		txtResult.setText("Inserisci un numero N");
    		return;
    	}
    	int n;
    	
    	try {
    		n = Integer.parseInt(txtN.getText());
    	}
    	catch(Exception e) {
    		txtResult.setText("Inserisci un numero intero");
    		return;
    	}
    	
    	if (this.cmbAnno.getValue() == null) {
    		txtResult.setText("Scegli un anno!");
    		return;
    	}
    	
    	int anno = cmbAnno.getValue();
    	
    	SimpleWeightedGraph<User,DefaultWeightedEdge> graph = model.creaGrafo(n, anno);
    	
    	txtResult.setText("Grafo creato con " + graph.vertexSet().size() + " vertici e " + graph.edgeSet().size() + " archi.\n\n");

    	cmbUtente.getItems().addAll(graph.vertexSet());
    }

    @FXML
    void doUtenteSimile(ActionEvent event) {
    	
    	if (this.cmbUtente.getValue() == null) {
    		txtResult.setText("Scegli un utente!");
    		return;
    	}
    	
    	User user = cmbUtente.getValue();
    	
    	txtResult.setText("Utenti più simili a " + user.toString() + ":\n");
    	
    	for (User u : model.getSimili(user))
    		txtResult.appendText(u.toString() + "    GRADO: " + (int) model.getPesoMax() + '\n');
    }
    
    @FXML
    void doSimula(ActionEvent event) {
    	
    	if (this.txtX1.getText() == "") {
    		txtResult.setText("Inserisci un numero N1");
    		return;
    	}
    	int n1;
    	
    	try {
    		n1 = Integer.parseInt(txtX1.getText());
    	}
    	catch(Exception e) {
    		txtResult.setText("Inserisci un numero intero");
    		return;
    	}
    	
    	if (this.txtX2.getText() == "") {
    		txtResult.setText("Inserisci un numero N2");
    		return;
    	}
    	int n2;
    	
    	try {
    		n2 = Integer.parseInt(txtX2.getText());
    	}
    	catch(Exception e) {
    		txtResult.setText("Inserisci un numero intero");
    		return;
    	}
    	
    	Map<Integer,Integer> mappa = model.run(n1, n2);
    	
    	txtResult.setText("RISULTATI:\n\n");
    	for (Integer intervistatore : mappa.keySet())
    		txtResult.appendText("Intervistatore " + intervistatore + ": " + mappa.get(intervistatore) + " intervistati\n");
    	
    	txtResult.appendText("\nNumero di giorni: " + model.getLastDay());
    }
    

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnUtenteSimile != null : "fx:id=\"btnUtenteSimile\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbAnno != null : "fx:id=\"cmbAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbUtente != null : "fx:id=\"cmbUtente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX1 != null : "fx:id=\"txtX1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	for (int i=2005; i<2014; i++)
    		cmbAnno.getItems().add(i);
    }
}
