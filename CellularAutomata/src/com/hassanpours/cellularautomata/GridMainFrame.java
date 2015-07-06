package com.hassanpours.cellularautomata;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GridMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JSlider zoomSlider;

	private boolean importR = false;
	private JLabel ruleListLabel;
	private JLabel zoomLabel;
	private JLabel simulationLabel;
	private JLabel stepLabel;

	private JFileChooser fileChooser;

	private JLabel animationSpeedLabel;

	private GridPanel gridBoard;
	private JButton addRuleBtn;
	private JButton ruleEditorBtn;
	private JButton stepBtn;

	private JToggleButton playPauseBtn;
	private JSpinner stepSpinner;

	private JScrollPane scrollPane;

	private JPanel AddRulePanel;
	private JPanel SimulationPanel;
	private JPanel gridPanel;
	private JSlider animationSpeedSlider;

	private Timer ruleCheckTimer;

	private JCheckBoxList cbList;

	private ArrayList<Rule> RulesList = new ArrayList<>();

	private static ImageIcon createImageIcon(String path, String description,
			int width, int height) throws IOException {
		Image img = ImageIO.read(new File(path));
		if (img != null) {
			Image resizedImage = img.getScaledInstance(width, height, 40);
			return new ImageIcon(resizedImage, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private GridBagConstraints constraints = new GridBagConstraints();
	private GridBagConstraints constraintsP = new GridBagConstraints();
	private GridBagConstraints constraintsS = new GridBagConstraints();

	public GridMainFrame() throws IOException {
		setSize(1000, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		writeObjectIntoFile();
		addWindowFocusListener(winListener);
		init();
		setupFrame();
		addListener();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	void addGB(Component component, int x, int y) {
		constraints.gridx = x;
		constraints.gridy = y;
		add(component, constraints);
	}

	void addGBP(Component component, int x, int y) {
		constraintsP.gridx = x;
		constraintsP.gridy = y;
		AddRulePanel.add(component, constraintsP);
	}

	void addGBS(Component component, int x, int y) {
		constraintsS.gridx = x;
		constraintsS.gridy = y;
		SimulationPanel.add(component, constraintsS);
	}

	public void init() throws IOException {

		setLayout(new GridBagLayout());
		AddRulePanel = new JPanel();
		String fileContents = new String("Automaton");
		fileChooser = new JFileChooser(fileContents);
		AddRulePanel.setLayout(new GridBagLayout());
		gridPanel = new JPanel();
		SimulationPanel = new JPanel();
		SimulationPanel.setLayout(new GridBagLayout());

		ImageIcon icon = createImageIcon("pencil43.png", "Java", 40, 40);
		ImageIcon addIcon = createImageIcon("add.png", "Add", 40, 40);
		gridBoard = new GridPanel();
		gridPanel.add(gridBoard);

		zoomSlider = new JSlider(0, 50, 20);
		animationSpeedSlider = new JSlider();

		zoomLabel = new JLabel("Grid Zoom");
		ruleListLabel = new JLabel("Active Rules");
		simulationLabel = new JLabel("Simulation Panel");

		stepLabel = new JLabel("Step By");

		animationSpeedLabel = new JLabel("Animation Speed");

		ruleEditorBtn = new JButton("Rule Editor", icon);
		addRuleBtn = new JButton("Add Rule", addIcon);

		stepBtn = new JButton("Step");

		ruleListLabel.setFont(new Font(getName(), Font.BOLD, 15));
		simulationLabel.setFont(new Font(getName(), Font.BOLD, 15));

		stepSpinner = new JSpinner();

		playPauseBtn = new JToggleButton("Play");

		cbList = new JCheckBoxList();
		scrollPane = new JScrollPane(cbList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black),
				"Active Rules"));
		SimulationPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black),
				"Simulation Panel"));
		AddRulePanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black),
				"Rule Panel"));
		gridPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black),
				"Grid"));

	}

	private void setupFrame() {

		setJMenuBar(createMenuBar());
		constraintsP.insets = new Insets(5, 0, 5, 0);
		constraintsP.fill = GridBagConstraints.BOTH;
		addGBP(ruleEditorBtn, 0, 0);
		addGBP(scrollPane, 0, 1);
		addGBP(addRuleBtn, 0, 2);

		constraintsS.insets = new Insets(5, 5, 5, 5);
		constraintsS.gridwidth = 3;
		constraintsS.fill = GridBagConstraints.BOTH;
		addGBS(zoomLabel, 1, 0);
		addGBS(zoomSlider, 0, 1);
		constraintsS.gridwidth = 1;
		addGBS(stepLabel, 0, 2);
		addGBS(stepSpinner, 1, 2);
		addGBS(stepBtn, 2, 2);
		constraintsS.gridwidth = 3;
		addGBS(playPauseBtn, 0, 3);
		addGBS(animationSpeedLabel, 1, 4);
		addGBS(animationSpeedSlider, 0, 5);

		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(10, 0, 10, 10);
		constraints.weightx = 1;
		addGB(AddRulePanel, 1, 1);
		addGB(SimulationPanel, 1, 2);
		constraints.gridwidth = 1;
		constraints.gridheight = 3;
		constraints.weighty = 3;
		constraints.weightx = 3;
		constraints.insets = new Insets(10, 10, 10, 10);
		addGB(gridPanel, 0, 0);

	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu ruleMenu = new JMenu("Rule");
		JMenu stateMenu = new JMenu("State");

		JMenuItem importRule = new JMenuItem("IMPORT...");
		JMenuItem exportRule = new JMenuItem("EXPORT...");

		JMenuItem importState = new JMenuItem("IMPORT...");
		JMenuItem exportState = new JMenuItem("EXPORT...");

		menuBar.add(ruleMenu);
		ruleMenu.add(importRule);
		ruleMenu.add(exportRule);

		menuBar.add(stateMenu);
		stateMenu.add(importState);
		stateMenu.add(exportState);

		importRule.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (fileChooser.showOpenDialog(GridMainFrame.this) == JFileChooser.APPROVE_OPTION) {

					System.out
							.println(fileChooser.getSelectedFile().toString());
					importR = true;
					try (FileInputStream fi = new FileInputStream(fileChooser
							.getSelectedFile().toString());
							ObjectInputStream os = new ObjectInputStream(fi);) {

						int ok = os.readInt();
						ArrayList<Rule> r = new ArrayList<>();
						if (ok == -1)
							System.out.println("no rule added");
						else {
							for (int i = 0; i < ok; i++)
								r.add((Rule) os.readObject());
						}

						RulesList = r;

						LinkedList<Object> RulesListBox = new LinkedList<Object>();
						for (Rule t : RulesList) {
							RulesListBox.add(new JCheckBox("Rule " + t.getId()));
							System.out.println(t.toString());

							try {
								for (SubRule f : t.getSubRuleList()) {
									System.out.println("\t" + f.toString());
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						cbList.setListData(RulesListBox);

					} catch (FileNotFoundException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (ClassNotFoundException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}

			}
		});

		exportRule.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(GridMainFrame.this) == JFileChooser.APPROVE_OPTION) {
					System.out
							.println(fileChooser.getSelectedFile().toString());

					try (FileOutputStream fs = new FileOutputStream(fileChooser
							.getSelectedFile().toString());
							ObjectOutputStream os = new ObjectOutputStream(fs);) {

						if (RulesList.isEmpty()) {
							os.writeInt(-1);
						} else {
							int[] indices = cbList.getSelectedIndices();
							if (indices.length == 0) {
								os.writeInt(RulesList.size());
								for (int i = 0; i < RulesList.size(); i++) {
									os.writeObject(RulesList.get(i));
								}
							} else {
								os.writeInt(indices.length);
								for (int i : indices) {
									os.writeObject(RulesList.get(i));
								}
							}
						}

					} catch (FileNotFoundException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}
			}
		});

		importState.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(GridMainFrame.this) == JFileChooser.APPROVE_OPTION) {
					System.out
							.println(fileChooser.getSelectedFile().toString());
					loadState(fileChooser.getSelectedFile().toString());
				}
			}
		});

		exportState.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(GridMainFrame.this) == JFileChooser.APPROVE_OPTION) {
					System.out
							.println(fileChooser.getSelectedFile().toString());

					saveState(fileChooser.getSelectedFile().toString());
				}
			}
		});

		return menuBar;
	}

	private void addListener() {
		addRuleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					if (RulesList.isEmpty())
						new RuleEditorFrame();
					else {
						writeObjectIntoFile();
						new RuleEditorFrame();
					}
				} catch (IOException e1) {

				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		ruleEditorBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				writeObjectIntoFile();
				try {
					new RuleEditorFrame();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (zoomSlider.getValue() > 0 && zoomSlider.getValue() < 10)
					gridBoard.setSize(10);
				else if (zoomSlider.getValue() > 10 && zoomSlider.getValue() < 20)
					gridBoard.setSize(20);
				else if (zoomSlider.getValue() > 20
						&& zoomSlider.getValue() < 25)
					gridBoard.setSize(25);
				else if (zoomSlider.getValue() > 25
						&& zoomSlider.getValue() < 50)
					gridBoard.setSize(50);
			}
		});

		playPauseBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (cbList.getSelectedIndex() != -1) {

					if (playPauseBtn.isSelected()) {
						ArrayList<Rule> listarray = new ArrayList<>();
						for (int i = 0; i < cbList.getSelectedIndices().length; i++) {

							listarray.add(RulesList.get(cbList
									.getSelectedIndices()[i]));
						}

						gridBoard.setRlistc(listarray);
						ruleCheckTimer = new Timer();
						ruleCheckTimer.scheduleAtFixedRate(new TimerTask() {

							@Override
							public void run() {
								try {
									gridBoard.cellArrayUpdate();

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}

						}, 5 * 1000, 1 * 1000);

					} else {

						ruleCheckTimer.cancel();
					}
				}
			}
		});
	}

	private ArrayList<Rule> readObjectFromFile() {

		try (FileInputStream fi = new FileInputStream("RulesSet.bin");
				ObjectInputStream os = new ObjectInputStream(fi);) {

			int ok = os.readInt();
			ArrayList<Rule> r = new ArrayList<>();
			if (ok == -1)
				System.out.println("no rule added");
			else {
				for (int i = 0; i < ok; i++)
					r.add((Rule) os.readObject());
			}

			return r;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private void writeObjectIntoFile() {

		try (FileOutputStream fs = new FileOutputStream("RulesSet.bin");
				ObjectOutputStream os = new ObjectOutputStream(fs);) {

			if (RulesList.isEmpty()) {
				os.writeInt(-1);
			} else {
				int[] indices = cbList.getSelectedIndices();
				if (indices.length == 0) {
					os.writeInt(RulesList.size());
					for (int i = 0; i < RulesList.size(); i++) {
						os.writeObject(RulesList.get(i));
					}
				} else {
					os.writeInt(indices.length);
					for (int i : indices) {
						os.writeObject(RulesList.get(i));
					}
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	WindowFocusListener winListener = new WindowFocusListener() {

		@Override
		public void windowLostFocus(WindowEvent e) {
		}

		@Override
		public void windowGainedFocus(WindowEvent e) {

			if (!importR)
				RulesList = readObjectFromFile();
			LinkedList<Object> RulesListBox = new LinkedList<Object>();
			for (Rule t : RulesList) {
				RulesListBox.add(new JCheckBox("Rule " + t.getId()));
			}
			cbList.setListData(RulesListBox);

		}
	};

	private void saveState(String path) {

		int[][] state = gridBoard.getArrayOfCells();

		try (FileOutputStream fs = new FileOutputStream(path);
				ObjectOutputStream os = new ObjectOutputStream(fs);) {

			os.writeInt(state.length);

			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state.length; j++) {
					os.writeInt(state[i][j]);
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loadState(String path) {

		int[][] state = null;
		try (FileInputStream fi = new FileInputStream(path);
				ObjectInputStream os = new ObjectInputStream(fi);) {

			int s = os.readInt();
			state = new int[s][s];
			for (int i = 0; i < state.length; i++) {
				for (int j = 0; j < state.length; j++) {
					state[i][j] = os.readInt();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gridBoard.setArrayOfCells(state);
	}
}
