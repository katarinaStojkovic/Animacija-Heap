import java.util.ArrayList;

public class SortAndBuildHeap {
	private ArrayList<Integer> i1;
	private ArrayList<Integer> i2;
	private ArrayList<Boolean> willSwap;
	private ArrayList<Boolean> isSibling;// Svi oni koji se nalaze ispod neko
											// cvora ako se nalaze vraca true

	public SortAndBuildHeap() {
		super();
	}

	public SortAndBuildHeap(ArrayList<Integer> i1, ArrayList<Integer> i2, ArrayList<Boolean> willSwap,
			ArrayList<Boolean> isSibling) {
		super();
		this.i1 = i1;
		this.i2 = i2;
		this.willSwap = willSwap;
		this.isSibling = isSibling;
	}

	public ArrayList<Integer> getI1() {
		return i1;
	}

	public void setI1(ArrayList<Integer> i1) {
		this.i1 = i1;
	}

	public ArrayList<Integer> getI2() {
		return i2;
	}

	public void setI2(ArrayList<Integer> i2) {
		this.i2 = i2;
	}

	public ArrayList<Boolean> getWillSwap() {
		return willSwap;
	}

	public void setWillSwap(ArrayList<Boolean> willSwap) {
		this.willSwap = willSwap;
	}

	public ArrayList<Boolean> getIsSibling() {
		return isSibling;
	}

	public void setIsSibling(ArrayList<Boolean> isSibling) {
		this.isSibling = isSibling;
	}

	
	//Metod za sortiranje heap-a
    public int[] sort(int[] A) {
    	//poslednji levi element
        for (int i = (A.length - 1) / 2; i >= 0; i--) {// for pelja od polovine unazad postavlja vrednostu u niz A
        	A = build_heap(A, i, A.length - 1);
        }
        for (int i = 0; i < A.length - 1; i++) {
            i1.add(0);
            i2.add(A.length - 1 - i);
            willSwap.add(true);
            isSibling.add(false);
            A = swap(A, 0, A.length - 1 - i);
            A = build_heap(A, 0, A.length - 2 - i);
        }
        return A;
    }
    //Metoda za kreiranje heap-a
    public int[] build_heap(int[] A, int node, int limit) {
        int child = node * 2 + 1;// child je node puta dva + 1 jer jer krecemo od jedinice koju uporedjujemo sa limit odnosno root
        while (child <= limit) {//sve dok je child manji ili jednak limitu vrti while
            if (child + 1 <= limit) {//U koliko je child plus 1 manj ili jednak limit-u naredni broj
                i1.add(child);// i1 je prvi child
                i2.add(child + 1);// i2 je drugi child
                willSwap.add(false);//U tom slucaju dodajemo false jer nema swap-ovanja
                isSibling.add(true);// I postavljamo da su braca(siblig) na true
            }
            if (child + 1 <= limit && A[child] < A[child + 1])// Ako je child +1 manji ili jednak limitu i prvi child u nizu manji od drugo child-a
                child++;//poveæavamo child za jedan
            i1.add(child);//na i1 se didaje child
            i2.add(node);// na i2 node
            willSwap.add(!(A[child] < A[node]));//U koliko child1 nije manji od child2 postavlja vrednost na true a ukoliko jeste na false
            isSibling.add(false);// kazemo da nisu braca
            if (A[child] < A[node])
                break;
            A = swap(A, node, child);
            node = child;
            child = 2 * node + 1;
        }
        return A;
    }
    // Metoda za swap dva broja koja vraca swap niz dva broja
    public int[] swap(int[] A, int in1, int in2) {
        int temp = A[in1];
        A[in1] = A[in2];
        A[in2] = temp;
        return A;
    }

	@Override
	public String toString() {
		return "SortAndBuildHeap [i1=" + i1 + ", i2=" + i2 + ", willSwap=" + willSwap + ", isSibling=" + isSibling
				+ "]";
	}

    
}
