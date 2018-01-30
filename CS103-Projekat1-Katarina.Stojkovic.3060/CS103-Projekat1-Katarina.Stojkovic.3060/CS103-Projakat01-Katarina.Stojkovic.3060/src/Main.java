import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {
    //GUI komponente
    private JButton btnPlay, btnReset, btnRandom,btnAdd,btnDelRoot;
    private ButtonAction bl;
    private JLabel[] sorted;
    private JPanel board;
    private JLabel lblCurrLog;
    private Timer ctrl, ctrl2, ctrl3;
    private Heap heap = new Heap();
    private SortAndBuildHeap sabh = new SortAndBuildHeap();
   
    private int endIndex = 9;//poslednji indeks vrednost 9
    private int index = 0;
 
    // nizovi iz kojih uzimamo koordinate za iscrtavanje krugova
    private int[] cx = {425, 225, 625, 125, 325, 525, 725, 75, 175, 275};
    private int[] cy = {100, 200, 200, 300, 300, 300, 300, 400, 400, 400};
    //nizovi koje iz kojih uzimamo koordinate za isctavanje linija
    private int[] cxo = {425, 225, 625, 125, 325, 525, 725, 75, 175, 275};
    //
    private int[] cyo = {100, 200, 200, 300, 300, 300, 300, 400, 400, 400};
    //niz sa bojama krugova
    private Color[] cC = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
    private Color[] valsColor = {Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE};
    private int cDiam = 50;// Dimenzija kruga tj širina i dužina

   
    private JTextField txtNumber;// txtField za upisivanje broja 
   
    

    public Main() {
    	sabh.setI1(new ArrayList<Integer>());
    	sabh.setI2(new ArrayList<Integer>());
    	sabh.setWillSwap(new ArrayList<Boolean>());
    	sabh.setIsSibling(new ArrayList<Boolean>());
        heap.setVals(new int[10]);
        heap.setValsDisplay(new int[10]);
        //Pozivanje metode resetArray kako bi sve bilo na nuli tj ceo niz
        heap.resetArray();
        
        btnPlay = new JButton("Play");
        btnReset = new JButton("Reset");
        btnAdd = new JButton("Add");// definisanje buttona dodaj
        btnRandom = new JButton("Random");
        btnDelRoot = new JButton("Delete Root");
        
        //dodavanje ActionListener za pritisak na dugme dugmeta
        bl = new ButtonAction();
        btnPlay.addActionListener(bl);
        btnReset.addActionListener(bl);
        btnAdd.addActionListener(new ButtonAdd()); 
        btnDelRoot.addActionListener(new ButtonDelRoot()); 
        btnRandom.addActionListener(new ButtonRandom());
        
        //GUI
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        board = new DrawCanvas();//iscrtavanje slike na drawCanvas
        add(board);
        
        JPanel pnlNorth = new JPanel(new GridLayout(2, 11, 0, 5));
        pnlNorth.add(new JLabel("Index:"));
        for(int i = 0; i < 10; i++) {
            pnlNorth.add(new JLabel(""+i, SwingConstants.CENTER));
        }
        pnlNorth.add(new JLabel("Sorted:"));
        sorted = new JLabel[10];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = new JLabel("", SwingConstants.CENTER);
            sorted[i].setBorder(border);
            pnlNorth.add(sorted[i]);
        }
        lblCurrLog = new JLabel(" ", SwingConstants.CENTER);
        lblCurrLog.setBorder(border);
        
        JPanel pnl = new JPanel(new GridLayout(2, 1, 0, 20));
        pnl.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnl.add(pnlNorth);
        pnl.add(txtNumber = new JTextField());
       
        //dodavanje buttona na panel
        JPanel pnlSouth = new JPanel(new FlowLayout());
        pnlSouth.add(btnPlay);
        pnlSouth.add(btnReset);
        pnlSouth.add(btnAdd); 
        pnlSouth.add(btnRandom);
        pnlSouth.add(btnDelRoot);
        add(pnlSouth, BorderLayout.SOUTH);
        add(pnl, BorderLayout.NORTH);
        //Dodeljivanje pocetne vednosti timer-ima
        ctrl = new Timer(0, new TimerAction());
        ctrl.setInitialDelay(0);
        ctrl2 = new Timer(1, new TimerAction2());
        ctrl3 = new Timer(1, new TimerAction3());
        ctrl3.setInitialDelay(0);

        btnDelRoot.disable();
        btnPlay.disable();
        btnReset.disable();
        btnDelRoot.disable();
    }
    
    
    private class ButtonAdd implements ActionListener {

        public void actionPerformed(ActionEvent x) {
			if (Integer.parseInt(txtNumber.getText()) < 1000) {
				heap.add(Integer.parseInt(txtNumber.getText()));// pozivanje
																// metode
				// za dodavanje sa
				// JtexxtField-a
				txtNumber.setText("");
				repaint();// pozivanje metode za obnavljanje slike
				txtNumber.requestFocus();
				btnPlay.enable();
				btnDelRoot.disable();
				btnRandom.disable();
				btnReset.disable();
			} else
				JOptionPane.showMessageDialog(null, "You can't add number > 1000");
        }

    }

    
    private class ButtonRandom implements ActionListener {

        public void actionPerformed(ActionEvent x) {
        	heap.randomizeArray();//Pozivanje metoda za random elemente u opsegu 1000
            repaint();// pozivanje metode za obnavljanje slike
            btnPlay.enable();
            btnAdd.disable();
            btnDelRoot.disable();
            btnReset.disable();
        }

    }
    
     private class ButtonDelRoot implements ActionListener {

        public void actionPerformed(ActionEvent x) {
            heap.getVals()[0]=1000;
            heap.getValsDisplay()[0]=0;
            sabh.sort(heap.getVals());//komande koje treba da se izvrse u heap sort 
            startSwapAnimation();//pokrece animaciju
            btnAdd.enable();
            btnRandom.enable();
            btnReset.enable();
        }

    }
    // za button play
     private class ButtonAction implements ActionListener {
         public void actionPerformed(ActionEvent x) {
             if (x.getSource().equals(btnPlay)) {//kada je kliknuto play
                 if (ctrl.isRunning()) {//prvi timer za pomeranje je pokrenut
                     //svi se pauziraju
                     ctrl.stop();
                     ctrl2.stop();
                     ctrl3.stop();
                     //postavljanje brzine kada na 0
                     ctrl.setInitialDelay(0);
                     ctrl3.setInitialDelay(0);
                     btnPlay.setText("Play");
                 } else {
                     //u suprotnom prikazuje se pauze
                     btnPlay.setText("Pause");
                     sabh.sort(heap.getVals());//sortiraju se vrednosti
                     startSwapAnimation();//pokrece se animacija
                 }
             } else if (x.getSource().equals(btnReset)) {
                 ctrl.stop();
                 ctrl2.stop();
                 ctrl3.stop();
                 btnPlay.setText("Play");
                 //reset node position and colors
                 for (int i = 0; i < heap.getVals().length; i++) {
                     cC[i] = Color.WHITE;
                     valsColor[i] = Color.BLUE;
                     sorted[i].setText("");
                     cx[i] = cxo[i];
                     cy[i] = cyo[i];
                 }
                 heap.resetArray();
                 sabh.getI1().clear();
                 sabh.getI2().clear();
                 sabh.getWillSwap().clear();
                 sabh.getIsSibling().clear();
                 index = 0;
                 endIndex = 9;
                 repaint();
                 btnRandom.enable();
                 btnAdd.enable();
             }

         }
     }

     
     
     //Klasa DrawCanvas entenduje klasu JPanel kako bi se na panelu vrsilo iscrtavanje
    private class DrawCanvas extends JPanel {
        public DrawCanvas() {
            super(null);
        }

        //@paint
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.GRAY);// Postavljanje sive bolje koja je u pravougaoniku
            g.fillRect(0, 0, 900, 700);//Postavljanje vrednosti i velicine pravougaonika u kome se iscrtava animacija

            //Iscrtavanje linija za izmedju krugova
            g.setColor(Color.WHITE);
            for (int i = 0; i < 5; i++) {
                if (cC[2 * i + 1] != Color.RED)
                    g.drawLine(cxo[i] + cDiam / 2, cyo[i] + cDiam / 2, cxo[2 * i + 1] + cDiam / 2, cyo[2 * i + 1] + cDiam / 2);

                if (i != 4)
                    if (cC[2 * i + 2] != Color.RED)
                        g.drawLine(cxo[i] + cDiam / 2, cyo[i] + cDiam / 2, cxo[2 * i + 2] + cDiam / 2, cyo[2 * i + 2] + cDiam / 2);

            }

            //Crtanje krugova
            for (int i = cx.length - 1; i >= 0; i--) {
                g.setColor(cC[i]);//Za sve krugove uzimamo boju iz niza cC(color circle)
                g.fillOval(cx[i], cy[i], cDiam, cDiam); 
            }


            int fontSize = 15;
            g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
            //Iscrtavanje stringva tj brojeva u krugovima
            for (int i = 0; i < heap.getValsDisplay().length; i++) {
                g.setColor(valsColor[i]);
                g.drawString(Integer.toString(heap.getValsDisplay()[i]), cx[i] + cDiam / 4, cy[i] + cDiam / 2 + 5);
            }


        }
    }
    //Metoda za startovanje animacije
    public void startSwapAnimation() {
        //Pokrece se timer2 za menjanje boje
        ctrl2.start();
        //Ako je is liste willswap za dati indeks true
        if (sabh.getWillSwap().get(index)) {
            ctrl.start();//Pokrece se timer1 za pomeranje krugova
        } else//pokrece se timer3 za vracanje boje u pocetnu belu
            ctrl3.start();

        //setovanje delak tj brzine u milisekundama kliknemo na pauzu
        ctrl.setInitialDelay(000);
        ctrl3.setInitialDelay(000);
    }
    

    //Na akciju prvog timer-a se pomeraju krugovi
    private class TimerAction implements ActionListener {
        public void actionPerformed(ActionEvent x) {
            // pomeranje krugova uzimaju se njihovi indekci iz lista
            int index1 = sabh.getI1().get(index);
            int index2 = sabh.getI2().get(index);

            //Vrsimo uporedjivanje koordinata i pomeraj
            if (cxo[index1] > cxo[index2] && cx[index2] != cxo[index1]) {
                cx[index1]--;
                cx[index2]++;
            } else if (cxo[index1] < cxo[index2] && cx[index2] != cxo[index1]) {
                cx[index1]++;
                cx[index2]--;
            }
            if (cyo[index1] > cyo[index2] && cy[index2] != cyo[index1]) {
                cy[index1]--;
                cy[index2]++;
            } else if (cyo[index1] < cyo[index2] && cy[index2] != cyo[index1]) {
                cy[index1]++;
                cy[index2]--;
            }
            //kraj animacije ako je indeks2 i jednak indeksu1 iz liste za krug i obrnuti setuju se indeksi kako bi se postavio pravi krug na pravi kraj linije 
            if (cy[index2] == cyo[index1] && cx[index2] == cxo[index1]) {
                cx[index1] = cxo[index1];
                cx[index2] = cxo[index2];
                cy[index1] = cyo[index1];
                cy[index2] = cyo[index2];
                cC[index1] = Color.WHITE;
                cC[index2] = Color.WHITE;
                
                //Ako nam je prvi indeks iz liste jednak nuli
                if (index1 == 0)
                	//Ako je prvi nula a drugi posledni i vrednost prvog je veca od vednosti drugog
                    if (index1 == 0 && index2 == endIndex && heap.getValsDisplay()[index1] > heap.getValsDisplay()[index2]) {
                        //na poslednji element niza za prikaz sortiranja se postavlja 0 element iz prikazane liste
                        sorted[endIndex].setText(Integer.toString(heap.getValsDisplay()[0]));
                        endIndex--;
                    }
                //swap indeksa
                sabh.swap(heap.getValsDisplay(), index1, index2);
                repaint();
                //povecavanje indeksa za jedan
                index++;
                if (index < sabh.getI1().size()) {//ako je indeks mani od velicine i1 liste
                    ctrl.stop();//stopira se timer1 za pomeranje
                    ctrl2.stop();//stopira se timer2 za menjanje boje
                    startSwapAnimation();// pokrece se animacija
                } else {//ako nije manji vec su jednaki 
                    if (index == sabh.getI1().size()) {
                    	//na niz sorted se postavlja poslednji element
                        sorted[endIndex].setText(Integer.toString(heap.getValsDisplay()[0]));
                    }
                    //zaustavljaju se oba timer-a
                    ctrl.stop();
                    ctrl2.stop();
                }
            }

        }
    }

    //Menjanje boje krugovima koji se swap-uju
    private class TimerAction2 implements ActionListener {
        public void actionPerformed(ActionEvent x) {
            int index1 = sabh.getI1().get(index);
            int index2 = sabh.getI2().get(index);
            cC[index1] = Color.RED;
            cC[index2] = Color.RED;
            repaint();
           
         
        }
    }

    //Akcija na timer3 vraca boju u prvobitnu belu
    private class TimerAction3 implements ActionListener {
        public void actionPerformed(ActionEvent x) {
             
            int index1 = sabh.getI1().get(index);
            int index2 = sabh.getI2().get(index);
            cC[index1] = Color.WHITE;
            cC[index2] = Color.WHITE;
            repaint();
            //stopira timer2 i timer3
            ctrl2.stop();
            ctrl3.stop();
            index++;
            //Ako je indeks manji od velicine liste1 radimo pokrecemo swap animaciju
            if (index < sabh.getI1().size()) {
                startSwapAnimation();
            }

        }
    }
 



    //Kreiranje i prikaz Gui preko main klase jer ona extenduje JFrame
    public static void createAndShowGUI() {
        Main app = new Main();
        app.setTitle("Heap Animation");
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.setSize(900, 750);
        app.setLocationRelativeTo(null);
        app.setVisible(true);

    }

    


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();// pokretanje 
            }
        });

    }
    
 

       
 
}
