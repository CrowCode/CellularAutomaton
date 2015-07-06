package com.hassanpours.cellularautomata;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class GridPanel extends JComponent {

	private static final long serialVersionUID = 1L;

	private int w = 500;
	private int h = 500;
	private BufferedImage bufferImgGrid;
	private Graphics2D g2;
	private int size;
	private int[][] arrayOfPixels;
	private int[][] arrayOfCells;
	private int[][] arrayOfCellsTmp;
	int[][] tmp;

	private ArrayList<Rule> rulesList = new ArrayList<>();

	public GridPanel() {

		super();
		arrayOfPixels = new int[w][h];
		addMouseListener(new PopClickListener());
		setPreferredSize(new Dimension(w, h));
		this.size = 20;
		if (bufferImgGrid == null) {
			bufferImgGrid = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		}
		this.arrayOfCells = new int[w / size][h / size];
		tmp = new int[arrayOfCells.length][arrayOfCells.length];

		for (int i = 0; i < arrayOfCells.length; i++) {
			for (int j = 0; j < arrayOfCells[0].length; j++) {

				for (int k = i * size; k < (i + 1) * size; k++)
					for (int l = j * size; l < (j + 1) * size; l++)
						arrayOfPixels[k][l] = 0;
			}
		}
		g2 = (Graphics2D) bufferImgGrid.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int x = findCellX(e.getPoint().x);
				int y = findCellY(e.getPoint().y);

				if (!e.isPopupTrigger()) {

					if (arrayOfCells.length != w / size) {
						arrayOfCells = new int[w / size][h / size];
						arrayOfCellsTmp = new int[w / size][h / size];
						if (tmp.length > arrayOfCells.length) {
							for (int i = 0; i < arrayOfCells.length; i++) {
								for (int j = 0; j < arrayOfCells.length; j++) {
									arrayOfCells[i][j] = tmp[i][j];
								}
							}
						} else {
							for (int i = 0; i < tmp.length; i++) {
								for (int j = 0; j < tmp.length; j++) {
									arrayOfCells[i][j] = tmp[i][j];
								}
							}
						}

						for (int i = 0; i < arrayOfCells.length; i++) {
							for (int j = 0; j < arrayOfCells[0].length; j++) {
								if (arrayOfCells[i][j] == 0)
									for (int k = i * size; k < (i + 1) * size; k++)
										for (int l = j * size; l < (j + 1)
												* size; l++)
											arrayOfPixels[k][l] = 0;
								else if (arrayOfCells[i][j] == 1)
									for (int k = i * size; k < (i + 1) * size; k++)
										for (int l = j * size; l < (j + 1)
												* size; l++)
											arrayOfPixels[k][l] = 1;

							}
						}

					}

					if (arrayOfCells[x][y] == 0) {
						arrayOfCells[x][y] = 1;
						for (int k = x * size; k < (x + 1) * size; k++)
							for (int l = y * size; l < (y + 1) * size; l++)
								arrayOfPixels[k][l] = 1;
					} else if (arrayOfCells[x][y] == 1) {
						arrayOfCells[x][y] = 0;
						for (int k = x * size; k < (x + 1) * size; k++)
							for (int l = y * size; l < (y + 1) * size; l++)
								arrayOfPixels[k][l] = 0;
					}
					repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {

		if (bufferImgGrid == null) {
			bufferImgGrid = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		}

		bufferImgGrid = convertToBufferedImage(arrayOfPixels);
		g.drawImage(bufferImgGrid, 0, 0, this);

	}

	public void setSize(int size) {
		this.size = size;
		tmp = new int[arrayOfCells.length][arrayOfCells.length];
		for (int i = 0; i < arrayOfCells.length; i++) {
			for (int j = 0; j < arrayOfCells.length; j++) {
				tmp[i][j] = arrayOfCells[i][j];
			}
		}

	}

	public void paintCellAsAlive(int x, int y, int size) {

		g2.setColor(Color.CYAN);
		int spX = x * size + 1;
		int spY = y * size + 1;
		g2.fillRect(spX, spY, size - 1, size - 1);
		arrayOfPixels[x][y] = 0;
		repaint();

	}

	void paintCellAsDead(int x, int y, int size) {

		g2.setColor(Color.WHITE);
		int spX = x * size + 1;
		int spY = y * size + 1;
		g2.fillRect(spX, spY, size - 1, size - 1);
		arrayOfPixels[x][y] = 255;
		repaint();

	}

	private int findCellX(int x) {
		int i;
		i = x / size;
		return i;
	}

	private int findCellY(int y) {
		int j;
		j = y / size;
		return j;
	}

	public void cellArrayUpdate() throws IOException {

		for (Rule rc : rulesList) {
			RuleCheck rcheck = new RuleCheck(rc, arrayOfCells);
			for (SubRule sbr : rc.getSubRuleList()) {
				for (int i = 0; i < arrayOfCells.length; i++) {
					for (int j = 0; j < arrayOfCells[0].length; j++) {

						if (sbr.getOnType().equals("in specific row"))
							arrayOfCellsTmp = rcheck.inSpesificRow(sbr, i, j,
									sbr.getOnPos());
						if (sbr.getOnType().equals("in specific column"))
							arrayOfCellsTmp = rcheck.inSpesificCol(sbr, i, j,
									sbr.getOnPos());
						if (sbr.getOnType().equals("in whole neighborhood"))
							arrayOfCellsTmp = rcheck.wholeNeighborhood(sbr, i,
									j);
						if (sbr.getOnType().equals("in specific diagonal"))
							arrayOfCellsTmp = rcheck.ondiagonal(sbr, i, j,
									sbr.getOnPos());
					}
				}

				for (int i = 0; i < arrayOfCellsTmp.length; i++) {
					for (int j = 0; j < arrayOfCellsTmp[0].length; j++) {
						arrayOfCells[i][j] = arrayOfCellsTmp[i][j];
					}
				}
				gridArrayUpdate();
			}

		}
	}

	public void gridArrayUpdate() {
		for (int i = 0; i < arrayOfCells.length; i++) {
			for (int j = 0; j < arrayOfCells[0].length; j++) {
				if (arrayOfCells[i][j] == 0)
					for (int k = i * size; k < (i + 1) * size; k++)
						for (int l = j * size; l < (j + 1) * size; l++)
							arrayOfPixels[k][l] = 0;
				else if (arrayOfCells[i][j] == 1)
					for (int k = i * size; k < (i + 1) * size; k++)
						for (int l = j * size; l < (j + 1) * size; l++)
							arrayOfPixels[k][l] = 1;

			}
		}
		repaint();

	}

	BufferedImage convertToBufferedImage(int[][] array) {

		final BufferedImage outputImage = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) outputImage.getGraphics();

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				int a = array[i][j];

				if (a != 0)
					g.setColor(Color.CYAN);
				else
					g.setColor(new Color(250, 250, 250));

				g.fillRect(i, j, 1, 1);
			}
		}
		return outputImage;
	}

	public ArrayList<Rule> getRlistc() {
		return rulesList;
	}

	public void setRlistc(ArrayList<Rule> rlistc) {
		this.rulesList = rlistc;
	}

	public int[][] getArrayOfPixels() {
		return arrayOfPixels;
	}

	public void setArrayOfPixels(int[][] arrayOfPixels) {
		this.arrayOfPixels = arrayOfPixels;
	}

	public int[][] getArrayOfCells() {
		return arrayOfCells;
	}

	public void setArrayOfCells(int[][] arrayOfCells) {
		this.arrayOfCells = arrayOfCells;
		gridArrayUpdate();
	}

	public ArrayList<Rule> getRulesList() {
		return rulesList;
	}

	public void setRulesList(ArrayList<Rule> rulesList) {
		this.rulesList = rulesList;
	}

	class PopUpDemo extends JPopupMenu {
	
		private static final long serialVersionUID = 1L;
		JMenuItem anItem;

		public PopUpDemo() {
			anItem = new JMenuItem("Clear The Grid?!");
			add(anItem);
			anItem.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < tmp.length; i++) {
						for (int j = 0; j < tmp.length; j++) {

							arrayOfCells[i][j] = 0;
							if (arrayOfCellsTmp != null)
								arrayOfCellsTmp[i][j] = 0;
							tmp[i][j] = 0;
						}
						gridArrayUpdate();
					}

				}
			});
		}
	}

	class PopClickListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger())
				doPop(e);
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger())
				doPop(e);
		}

		private void doPop(MouseEvent e) {
			PopUpDemo menu = new PopUpDemo();
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	

}
