import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import el3.NodeColor;
import el3.Graph;
import el3.Node;
import el3.Cursor;


/**
 * Classe principale permettant de trouver un coloriage optimal d'un graphe.
 */
public class VisualColoring {
	/**
	 * Fonction principale, prend en argument le nom du fichier contenant la
	 * description du graphe à colorier.
	 */
	public static void main(String[] args) {
		Graph g = null;
		if (args.length < 1) {
			System.err.println("# Usage : java ColoriageVisuel <fichier>");
			System.exit(1);
		} else {
			try {
				g = Graph.readFromFile(args[0]);
			} catch (Exception e) {
				System.err.println(e.toString());
				System.exit(1);
			}
		}
		
		if(!g.hasCoordinates()) {
			System.err.println(
					"ColoriageVisuel ne peut être utilisé que sur des " +
					"graphes complètement munis de coordonnées.");
			System.exit(1);
					
		}
		
		CallStackPanel pnlCallStack = new CallStackPanel(); 
		JScrollPane pnlScrollCallStack = new JScrollPane(
				pnlCallStack, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		CandidatePanel pnlSitu = new CandidatePanel();
		
		JSplitPane pnlVisu = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlSitu, pnlScrollCallStack);
		
		Dimension minimumSize = new Dimension(100, 150);
		pnlScrollCallStack.setMinimumSize(minimumSize);
		pnlSitu.setMinimumSize(minimumSize);
		
		pnlVisu.setOneTouchExpandable(false);
		
		Interactor interact = new Interactor(pnlVisu, pnlCallStack);
		GraphFrame gf = new GraphFrame(pnlVisu, interact);
	
		gf.setLocationRelativeTo(null);
		gf.setVisible(true);
		pnlVisu.setDividerLocation(pnlVisu.getHeight() /  2);
		
		// Meilleure solution trouvée pour l'instant.
		Candidate best_solution = null;
		// Indique si on a pu trouver une solution.
		boolean ok = true;
		int nbColorsMax = g.size();
		while (ok) {
			DecoratedCandidate candidate = new DecoratedCandidate(g, nbColorsMax, interact);
			pnlSitu.setSituation(candidate);

			gf.setNumColors(candidate.getMaxColors());
			pnlCallStack.setSituation(candidate);
			ok = candidate.color();

			if (!ok) {
				// ?
			} else {
				// On mémorise la meilleure situation trouvée
				best_solution = candidate;
				// Et on regarde si on ne peut pas faire mieux
				if (candidate.countUsedColors() < 2) {
					break;
				}
				nbColorsMax = candidate.countUsedColors() - 1;
				interact.userWait(Interactor.BUTTON_STAGE);
			}
			System.out.println();
		}
		
		interact.userWait(Interactor.BUTTON_STOP);
		gf.setVisible(false);
		
		if(best_solution == null) {
			JOptionPane.showMessageDialog(null, "Pas de solution");
		} else {
			JFrame f = new JFrame("MEILLEURE SOLUTION en " + best_solution.countUsedColors() + " couleurs");
			f.setPreferredSize(new Dimension(800, 600));
			f.setLayout(new BorderLayout());
			CandidatePanel sp = new CandidatePanel();
			sp.setSituation(best_solution);
			f.add(sp, BorderLayout.CENTER);
			JPanel pnlButtons = new JPanel(new FlowLayout());
			JButton btnOK = new JButton("OK");
			btnOK.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			pnlButtons.add(btnOK);
			f.add(pnlButtons, BorderLayout.SOUTH);
			f.pack();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.setLocationRelativeTo(null);
			f.setVisible(true);
		}
	}
}



class ColorListCellRenderer extends JPanel implements ListCellRenderer {
	private static final long serialVersionUID = -3649110991961104794L;
	
	private NodeColor color;
	private static final int w = 120, h = 20;
	private String text;
	
    public ColorListCellRenderer() {
        setOpaque(true);
        setBackground(Color.WHITE);
    }
    @Override
	public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
        this.color = (NodeColor)value;
		this.text = value.toString();
		
        return this;
    }
    
    @Override
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(this.color.color());
		g.fillOval(10, 8, 8, 8);
		g.setColor(Color.BLACK);
		g.drawString(this.text, 30, h-4);

    }
    
    @Override
	public int getWidth() { return w; }
    @Override
	public int getHeight() { return h; }
    @Override
	public Dimension getMinimumSize() { return new Dimension(w, h); }
    @Override
	public Dimension getPreferredSize() { return new Dimension(w, h); }
    
}

class GraphFrame extends JFrame {
	private static final long serialVersionUID = -4973061216267881086L;
	
	private final Interactor ci;
	private JList colorList;
	private JLabel lblCouleurs;
	
//	public JButton btnStep, btnStage, btnEnd;
	
	public void setNumColors(int n) {
		Object[] colors = new Object[n];
		NodeColor currentColor = new NodeColor();
		
		for(int i=1; i<=n; i++) {
			colors[i-1] = currentColor;
			currentColor = currentColor.getNextColor();
		}
		
		this.colorList.setListData(colors);
		
		this.lblCouleurs.setText("<html>max. <font color='red'>" + n + "</font> couleur" + (n>1 ? "s" : "") + ":<br><br></html>");
	}
	
	GraphFrame(JComponent pnlVisu, Interactor ci_) {
		this.ci = ci_;
		setTitle("Visualisateur de graphes");
		setPreferredSize(new Dimension(800, 600));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		content.add(pnlVisu, BorderLayout.CENTER);
		
		JPanel pnlCmd = new JPanel(new BorderLayout());
		
		JPanel pnlCmdButtons = new JPanel();
		pnlCmdButtons.setLayout(new GridLayout(3, 1, 5, 10));
		pnlCmdButtons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pnlCmd.add(pnlCmdButtons, BorderLayout.SOUTH);
		
		JButton btnStep = new JButton("Pas suivant");
		btnStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphFrame.this.ci.userSignal();
			}
		});
		pnlCmdButtons.add(btnStep);
		
		
		JButton btnStage = new JButton("Étape suivante");
		btnStage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphFrame.this.ci.userSignal();
			}
		});
		pnlCmdButtons.add(btnStage);
		
		JButton btnEnd = new JButton("Fin");
		btnEnd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphFrame.this.ci.userSignal();
			}
		});
		pnlCmdButtons.add(btnEnd);
		
		ci_.setButtons(btnStep, btnStage, btnEnd);

		JPanel pnlColors = new JPanel();
		pnlColors.setLayout(new BorderLayout());
		pnlColors.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		pnlCmd.add(pnlColors, BorderLayout.CENTER);
		
		this.lblCouleurs = new JLabel();
		pnlColors.add(this.lblCouleurs, BorderLayout.NORTH);
		this.lblCouleurs.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.colorList = new JList();
		pnlColors.add(new JScrollPane(this.colorList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		pnlColors.setEnabled(false);
		this.colorList.setCellRenderer(new ColorListCellRenderer());
		this.colorList.setAlignmentX(Component.CENTER_ALIGNMENT);

		
		content.add(pnlCmd, BorderLayout.EAST);
		
		pack();
	}
}


class CallStackPanel extends JPanel {
	private static final long serialVersionUID = 3921818149186147859L;
	
	private DecoratedCandidate situDec;
	
	public Dimension newPreferredSize = null;
	public Rectangle newVisibleRectangle = null;
	
	public synchronized void setSituation(DecoratedCandidate situ) {
		this.situDec = situ;
		repaint();
	}
	
	@Override
	public synchronized void paintComponent(Graphics g) {
		super.paintComponent(g);
    	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(this.situDec == null) return;
		InstructionSequence root = this.situDec.getTopLevelCall();
		if(root != null) {
			Coords c = root.paint(g, 2, 10);
			this.newPreferredSize = new Dimension(c.xMax+2, c.yMax+2);
			this.newVisibleRectangle = new Rectangle(c.xLast, c.yMax - 20, 80, 22);
		}
	}
}


abstract class Instruction {
	public abstract Coords paint(Graphics g, int x, int y);
}


class Colorer extends Instruction {
	private Node noeud;
	private NodeColor couleur;

	public Colorer(Node n, NodeColor c) {
		this.noeud = n;
		this.couleur = c;
	}


	@Override
	public Coords paint(Graphics g, int x, int y) {
		String texte = this.noeud.getContents().toString();
		Rectangle2D texteDims = g.getFontMetrics().getStringBounds(texte, g);
		
		g.setColor(this.couleur.color());
		g.fillOval(x, y-8, 8, 8);
		g.drawString(texte, x+10, y);
		
		return new Coords(
				(int)(x + 10 + texteDims.getWidth()),
				(int)(y + texteDims.getHeight()));
	}
}


class InstructionSequence extends Instruction {
	protected LinkedList<Instruction> instructions;
	private final String texte;
	
	public InstructionSequence(String txt) {
		this.texte = txt;
		this.instructions = new LinkedList<Instruction>();
	}
	
	public void add(Instruction i) {
		this.instructions.add(i);
	}


	@Override
	public Coords paint(Graphics g, int x, int y) {		
		g.setColor(Color.BLACK);
		g.drawString(this.texte, x, y);
		
		Rectangle2D texteDims = g.getFontMetrics().getStringBounds(this.texte, g);
		int xF = x + (int)texteDims.getWidth();
		int yF = y-(int)(texteDims.getHeight()/2)+2;
		g.drawLine(xF, yF, xF + 15, yF);
		g.drawLine(xF + 15, yF, xF + 12, yF - 2);
		g.drawLine(xF + 15, yF, xF + 12, yF + 2);
		
		// tracé de toutes les instructions dans l'appel récursif
		int yCur = y;
		int lastY = y + (int)texteDims.getHeight();
		int maxX = xF + 18;
		int lastX = maxX;
		for(Instruction instr : this.instructions) {
			Coords c = instr.paint(g, xF + 18, yCur);
			lastY = c.yMax;
			if(c.xMax > maxX) maxX = c.xMax;
			lastX = c.xLast;
			yCur = lastY;
		}
		
		return new Coords(maxX, lastY, lastX);
	}
}

class Colorier extends InstructionSequence {
	private static final int MAXLEN = 10;
	public Colorier() {
		super("colorier()");
	}
	public Colorier(Object o) {
		super("colorier(" + trunc(o.toString()) + ")");
	}
	// Tronque une chaîne de caractères à une longueur MAXLEN
	// en ne gardant que les premiers caractères et le dernier,
	// et en insérant des points de suspensions à la place des
	// caractères omis.
	static private String trunc(String s) {
		if (s.length() >= MAXLEN) {
			// Unicode 2026 = ellipsis (points de suspension)
			return s.substring(0,MAXLEN-2) + "\u2026" + s.substring(s.length()-1);
		} else {
			return s;
		}
	}
}

class RootInstruction extends InstructionSequence {
	public RootInstruction() {
		super("");
	}
}


class Return extends Instruction {
	private boolean value;
	
	public Return(boolean val) {
		this.value = val;
	}


	@Override
	public Coords paint(Graphics g, int x, int y) {
		String texte = this.value ? "ret TRUE" : "ret FALSE";
		Rectangle2D texteDims = g.getFontMetrics().getStringBounds(texte, g);

		g.setColor(Color.BLACK);
		g.drawString(texte, x, y);
		g.drawLine(x+5, y+1, x, y+6);
		g.drawLine(x-15, y+6, x, y+6);
		g.drawLine(x-15, y+6, x-12, y+4);
		g.drawLine(x-15, y+6, x-12, y+8);
		
		return new Coords(x+(int)texteDims.getWidth(), y+(int)texteDims.getHeight());
	}
}



class Coords {
	public int xMax, yMax, xLast;
	
	public Coords(int x_Max, int y_Max, int x_Last) {
		this.xMax = x_Max;
		this.yMax = y_Max;
		this.xLast = x_Last;
	}
	
	public Coords(int x_Max, int y_Max) {
		this(x_Max, y_Max, x_Max);
	}
}


class Interactor {
	private boolean waiting;
	private final JComponent panel;
	private final CallStackPanel callStack;
	private final BufferedImage tempArea = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
	private Dimension oldPreferredSize = null;
	private Rectangle oldVisibleRectangle = null;
	private JButton btnStep, btnStage, btnEnd;
	
	public static final int BUTTON_STEP = 1, BUTTON_STAGE = 2, BUTTON_STOP = 3;
	
	public Interactor(JComponent graphPanel, CallStackPanel callStackPanel) {
		this.waiting = false;
		this.panel = graphPanel;
		this.callStack = callStackPanel;
	}
	
	public void setButtons(JButton step, JButton stage, JButton end) {
		this.btnStep = step;
		this.btnStage = stage;
		this.btnEnd = end;
	}
	
	public void userWait(int type) {
		if(this.btnStep != null && this.btnStage != null) {
			switch(type) {
			case BUTTON_STEP: this.btnStep.setEnabled(true); this.btnStage.setEnabled(false); this.btnEnd.setEnabled(false); break;
			case BUTTON_STAGE: this.btnStep.setEnabled(false); this.btnStage.setEnabled(true); this.btnEnd.setEnabled(false); break;
			case BUTTON_STOP: this.btnStep.setEnabled(false); this.btnStage.setEnabled(false); this.btnEnd.setEnabled(true); break;
			default: throw new RuntimeException("Bad argument when calling userWait().");
			}
		}
		
		this.callStack.paintComponent(this.tempArea.getGraphics());
		
		if(this.callStack.newPreferredSize != null && !this.callStack.newPreferredSize.equals(this.oldPreferredSize)) {
			this.callStack.setPreferredSize(this.callStack.newPreferredSize);
			this.oldPreferredSize = this.callStack.newPreferredSize;
			this.callStack.revalidate();
		}
		
		if(this.callStack.newVisibleRectangle != null && !this.callStack.newVisibleRectangle.equals(this.oldVisibleRectangle)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					Interactor.this.callStack.scrollRectToVisible(Interactor.this.callStack.newVisibleRectangle);
				}
			});
			
			this.oldVisibleRectangle = this.callStack.newVisibleRectangle;
		}
		
		this.panel.repaint();
		
		synchronized(this) {
			this.waiting = true;
			try {
				while(this.waiting) wait();
			} catch (InterruptedException e) {
				// ???
			}
		}
	}
	
	public synchronized void userSignal() {
		if (!this.waiting) return;
		this.waiting = false;
		notifyAll();
	}
}


class DecoratedCandidate extends Candidate {
	private final Stack<InstructionSequence> calls = new Stack<InstructionSequence>(); 
	private final Interactor interactor;
	
	public DecoratedCandidate(Graph g, int nbCouleurs, Interactor interacteur) {
		super(g, nbCouleurs);
		this.interactor = interacteur;
		this.calls.push(new RootInstruction());
	}
	
	public DecoratedCandidate(Graph g, Interactor interacteur) {
		super(g);
		this.interactor = interacteur;
		this.calls.push(new RootInstruction());
	}

	@Override
	protected void setNodeColor(Node n, NodeColor c) {
		// call
		super.setNodeColor(n, c);

		// post
		this.calls.peek().add(new Colorer(n, c));
		this.interactor.userWait(Interactor.BUTTON_STEP);
	}

	@Override
	public boolean color() {
		// pre
		Colorier thisCall = new Colorier();
		this.calls.peek().add(thisCall);
		this.calls.push(thisCall);
		this.interactor.userWait(Interactor.BUTTON_STEP);
		
		// call
		boolean result = super.color();
		
		// post
		thisCall.add(new Return(result));
		this.calls.pop();
		
		this.interactor.userWait(Interactor.BUTTON_STEP);
		
		// finally
		return result;
	}
	
	@Override
	public boolean color(Cursor<Node> c) {
		// pre
		Colorier thisCall = null;
		if (c == null) {
			thisCall = new Colorier("null");			
		} else {
			thisCall = new Colorier(c.element());
		}
		this.calls.peek().add(thisCall);
		this.calls.push(thisCall);
		this.interactor.userWait(Interactor.BUTTON_STEP);
		
		// call
		boolean result = super.color(c);
		
		// post
		thisCall.add(new Return(result));
		this.calls.pop();
		
		this.interactor.userWait(Interactor.BUTTON_STEP);
		
		// finally
		return result;
	}

	public InstructionSequence getTopLevelCall() {
		if(this.calls.size() == 0) return null;
		return this.calls.get(0);
	}
	
}


class CandidatePanel extends JPanel {
	private static final long serialVersionUID = -4306378080523161648L;

	private final static Color colTexte = new Color(0, 0, 200);
	
	private Candidate situation = null;
	
	public void setSituation(Candidate situ) {
		this.situation = situ;
	}
	
	public CandidatePanel() {
		setBackground(Color.WHITE);
	}

	private int xConv(double x) {
		return (int) (((x+1)*getWidth())/2); 
	}
	
	private int yConv(double y) {
		return (int) (((y+1)*getHeight())/2);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
    	((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(this.situation == null) return;
		
		// d'abord, dessin de tous les arcs
		g.setColor(Color.BLACK);
		for(Node courant : this.situation.getGraph()) {
			for(Node voisin : courant.adjacentNodes()) {
					Point2D.Double posCourant = courant.getPosition();
					Point2D.Double posVoisin = voisin.getPosition();
					g.drawLine(
						xConv(posCourant.getX()), 
						yConv(posCourant.getY()),
						xConv(posVoisin.getX()),
						yConv(posVoisin.getY()));
				}
		}
		
		// ensuite, les noeuds
		for(Node courant : this.situation.getGraph()) {
			Point2D.Double posCourant = courant.getPosition();
			if(this.situation.isColored(courant)) {
				g.setColor(this.situation.getNodeColor(courant).color());
			} else {
				g.setColor(Color.GRAY);
			}
			g.fillOval(xConv(posCourant.getX())-8, yConv(posCourant.getY())-8, 16, 16);
			g.setColor(colTexte);
			String coulStr = this.situation.getNodeColor(courant) == null ?
					"" : " <" + this.situation.getNodeColor(courant).toString() + ">";
			g.drawString(
					courant.getContents() + coulStr,
					xConv(posCourant.getX())+10, 
					yConv(posCourant.getY())+10);
		}
	}
}
