import java.util.Random;

import javax.swing.JOptionPane;

public class Heap {
	
	private int[] vals;
	private int[] valsDisplay;
	private int brojac = 0; /// brojac

	public Heap() {

	}

	public Heap(int[] vals, int[] valsDisplay, int brojac) {
		this.vals = vals;
		this.valsDisplay = valsDisplay;
		this.brojac = brojac;
	}


	public int[] getVals() {
		return vals;
	}

	public void setVals(int[] vals) {
		this.vals = vals;
	}

	public int[] getValsDisplay() {
		return valsDisplay;
	}

	public void setValsDisplay(int[] valsDisplay) {
		this.valsDisplay = valsDisplay;
	}

	
	public int getBrojac() {
		return brojac;
	}

	public void setBrojac(int brojac) {
		this.brojac = brojac;
	}

	
	public void add(int broj) {
		if (brojac < vals.length) { // provera da li prekoracio duzinu
			vals[brojac] = broj; // dodavanje broja u niz vals
			valsDisplay[brojac] = vals[brojac]; // dodavanje iz niza vals na niz
												// koji se prikazuje
			brojac++; // povecanje brojaca
		} else {
			JOptionPane.showMessageDialog(null, "You can't add more than 10 numbers!!!");
		}
	}
	
	// Metoda za kreiranje radnom brojeva u opsegu 1000 i dodavanje na niz za
	// prikaz
	public void randomizeArray() {
		Random r = new Random();
		for (int i = 0; i < vals.length; i++) {
			vals[i] = r.nextInt(1000);
			valsDisplay[i] = vals[i];
		}
	}

	// Metoda za reset niza za prikaz tj setovanja njegovih podataka na nulu
	public void resetArray() {
		for (int i = 0; i < vals.length; i++) {
			vals[i] = 0;
			valsDisplay[i] = vals[i];
		}
	}
}
