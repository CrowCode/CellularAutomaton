package com.hassanpours.cellularautomata;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class RuleEditorFrame extends JFrame {

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

	private static final long serialVersionUID = 1L;

	public boolean applied = false;

	public boolean isApplyed() {
		return applied;
	}

	public void setApplyed(boolean applyed) {
		this.applied = applyed;
	}

	private int preSelected;
	private JPanel rulePanel;
	private JPanel subRulesPanel;
	private JPanel buttonPanel;
	private JScrollPane subRulesSPanel;

	private JRadioButton neighbor4RadioBtn;
	private JRadioButton neighbor8RadioBtn;
	private JRadioButton neighbor24RadioBtn;

	private JRadioButton activeInputRadioBtn;
	private JRadioButton passiveInputRadioBtn;

	private JRadioButton activeOutputRadioBtn;
	private JRadioButton passiveOutputRadioBtn;

	private JLabel subRulesIcon;
	private JLabel subRulesLabel;

	private String neighbor4 = new String("4 pt");
	private String neighbor8 = new String("8 pt");
	private String neighbor24 = new String("24 pt");
	private String active = new String("Active");
	private String passive = new String("Passive");

	private JButton addRuleBtn;
	private JButton removeRuleBtn;
	private JButton applyRuleBtn;
	private JButton cancelRuleBtn;

	private JComboBox<String> ruleSetCombo;

	private Box neighborsBox;
	private Box ruleInputBox;
	private Box ruleOutputBox;

	private Rule rule;
	private ArrayList<Rule> rulesList = new ArrayList<>();

	private Vector<String> ruleSetComboItems;

	private GridBagConstraints constraints = new GridBagConstraints();

	public RuleEditorFrame() throws IOException, ClassNotFoundException,
			InterruptedException {
		setSize(800, 600);
		setResizable(false);
		repaint();
		revalidate();
		init();
		rulesList = readObjectFromFile();
		for (int i = 0; i < rulesList.size(); i++) {
			System.out.println(rulesList.get(i));
			rulesList.get(i).prepareAfterSerializing();
			ruleSetComboItems.add(i, "Rule " + i);

			for (SubRuleComponent c : rulesList.get(i).getSubRuleCompoList()) {
				c.addAncestorListener(rmvActL);
				c.setVisible(false);
				subRulesPanel.add(c);
				System.out.println("pre Index" + preSelected);
				System.out.println("current Index"
						+ ruleSetCombo.getSelectedIndex());

			}
		}

		setupComponent();
		addHandlers();
		setLocationRelativeTo(null);
		setVisible(true);
		setExtendedState(JFrame.ICONIFIED);
		Thread.sleep(100);
		setState(JFrame.NORMAL);

	}

	void addGB(Component component, int x, int y) {
		constraints.gridx = x;
		constraints.gridy = y;
		rulePanel.add(component, constraints);
	}

	public void init() throws IOException {

		setLayout(new BorderLayout());
		rulePanel = new JPanel(new GridBagLayout());
		subRulesPanel = new JPanel();
		buttonPanel = new JPanel();
		subRulesPanel.setLayout(new BoxLayout(subRulesPanel, BoxLayout.Y_AXIS));
		rulePanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
				Color.black));

		subRulesSPanel = new JScrollPane(subRulesPanel,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		neighbor4RadioBtn = new JRadioButton(neighbor4);
		neighbor4RadioBtn.setActionCommand(neighbor4);
		neighbor4RadioBtn.setSelected(true);

		neighbor8RadioBtn = new JRadioButton(neighbor8);
		neighbor8RadioBtn.setActionCommand(neighbor8);

		neighbor24RadioBtn = new JRadioButton(neighbor24);
		neighbor24RadioBtn.setActionCommand(neighbor24);

		activeInputRadioBtn = new JRadioButton(active);
		activeInputRadioBtn.setActionCommand(active);
		activeInputRadioBtn.setSelected(true);

		passiveInputRadioBtn = new JRadioButton(passive);
		passiveInputRadioBtn.setActionCommand(passive);

		activeOutputRadioBtn = new JRadioButton(active);
		activeOutputRadioBtn.setActionCommand(active);
		activeOutputRadioBtn.setSelected(true);

		passiveOutputRadioBtn = new JRadioButton(passive);
		passiveOutputRadioBtn.setActionCommand(passive);

		ImageIcon addIcon = createImageIcon("add.png", "Add", 20, 20);
		ImageIcon removeIcon = createImageIcon("meanicons_49-512.png", "Add",
				20, 20);
		ImageIcon applyIcon = createImageIcon("checkmark_check.png", "Add", 20,
				20);
		ImageIcon cancelIcon = createImageIcon("cancel.png", "Add", 20, 20);
		ImageIcon greenAddIcon = createImageIcon("greenAdd.png", "Add", 20, 20);

		addRuleBtn = new JButton("Add", addIcon);
		removeRuleBtn = new JButton("Remove", removeIcon);

		subRulesIcon = new JLabel(greenAddIcon);
		subRulesLabel = new JLabel("Sub-Rule");

		ruleSetComboItems = new Vector<String>();
		ruleSetCombo = new JComboBox<>(ruleSetComboItems);

		applyRuleBtn = new JButton("Apply", applyIcon);
		cancelRuleBtn = new JButton("Cancel", cancelIcon);

		buttonPanel.add(applyRuleBtn);
		buttonPanel.add(cancelRuleBtn);

		neighborsBox = Box.createHorizontalBox();
		ButtonGroup neighborsGroup = new ButtonGroup();
		neighborsGroup.add(neighbor4RadioBtn);
		neighborsGroup.add(neighbor8RadioBtn);
		neighborsGroup.add(neighbor24RadioBtn);
		neighborsBox.add(neighbor4RadioBtn);
		neighborsBox.add(neighbor8RadioBtn);
		neighborsBox.add(neighbor24RadioBtn);
		neighborsBox.setBorder(BorderFactory
				.createTitledBorder("Neighborhood Type"));

		ruleInputBox = Box.createHorizontalBox();
		ButtonGroup ruleInputGroup = new ButtonGroup();
		ruleInputGroup.add(activeInputRadioBtn);
		ruleInputGroup.add(passiveInputRadioBtn);
		ruleInputBox.add(activeInputRadioBtn);
		ruleInputBox.add(passiveInputRadioBtn);
		ruleInputBox.setBorder(BorderFactory.createTitledBorder("Rule Input"));

		ruleOutputBox = Box.createHorizontalBox();
		ButtonGroup ruleOutputGroup = new ButtonGroup();
		ruleOutputGroup.add(activeOutputRadioBtn);
		ruleOutputGroup.add(passiveOutputRadioBtn);
		ruleOutputBox.add(activeOutputRadioBtn);
		ruleOutputBox.add(passiveOutputRadioBtn);
		ruleOutputBox
				.setBorder(BorderFactory.createTitledBorder("Rule Output"));

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				applied = false;
				System.gc();
			}

		});
	}

	void setFromRule(Rule theRule) throws IOException {

		switch (theRule.getNeighborhoodType()) {
		case "4 pt":

			neighbor4RadioBtn.setSelected(true);

			break;
		case "8 pt":

			neighbor8RadioBtn.setSelected(true);

			break;
		case "24 pt":

			neighbor24RadioBtn.setSelected(true);

			break;
		}

		switch (theRule.getRuleInputState()) {
		case "Active":

			activeInputRadioBtn.setSelected(true);

			break;
		case "Passive":

			passiveInputRadioBtn.setSelected(true);

			break;
		}

		switch (theRule.getRuleOutputState()) {
		case "Active":

			activeOutputRadioBtn.setSelected(true);

			break;
		case "Passive":

			passiveOutputRadioBtn.setSelected(true);

			break;
		}

	}

	protected void setupComponent() {
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 0, 0);
		constraints.gridwidth = 2;
		addGB(neighborsBox, 0, 0);
		constraints.gridwidth = 1;
		constraints.insets = new Insets(10, 0, 0, 0);
		addGB(ruleInputBox, 2, 0);
		constraints.insets = new Insets(10, 0, 0, 40);
		addGB(ruleOutputBox, 3, 0);
		constraints.insets = new Insets(0, 10, 0, 0);
		addGB(addRuleBtn, 4, 0);
		addGB(removeRuleBtn, 5, 0);
		constraints.insets = new Insets(20, 10, 20, 0);
		addGB(subRulesIcon, 0, 1);
		addGB(subRulesLabel, 1, 1);
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addGB(ruleSetCombo, 4, 1);
		add(rulePanel, BorderLayout.NORTH);
		add(subRulesSPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

	}

	protected void addHandlers() {
		addRuleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					if (!rulesList.isEmpty()) {
						rule = new Rule();
						setFromRule(rule);
						rulesList.add(rule);
						ruleSetComboItems.add(rulesList.size() - 1, "Rule "
								+ (rulesList.size() - 1));
						ruleSetCombo.setSelectedIndex(rulesList.size() - 1);
					} else {
						rule = new Rule();
						setFromRule(rule);
						rulesList.add(rule);
						ruleSetComboItems.add(0, "Rule " + (0));
						ruleSetCombo.setSelectedIndex(0);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		removeRuleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (rulesList.size() > 1) {
					rulesList.remove(ruleSetCombo.getSelectedIndex());
					ruleSetComboItems.remove(ruleSetCombo.getSelectedIndex());
					ruleSetCombo.setSelectedIndex(rulesList.size() - 1);
					System.out.println("Rule Removed!?");
				} else if (rulesList.size() == 1) {
					rulesList.remove(ruleSetCombo.getSelectedIndex());
					ruleSetComboItems.remove(ruleSetCombo.getSelectedIndex());
					ruleSetCombo.setSelectedItem(null);
					System.out.println("Rule Removed!?");
				}
			}
		});

		applyRuleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				for (Rule r : rulesList) {
					r.prepareBeforeSerializing();
				}
				applied = true;
				writeObjectIntoFile();
				GridMainFrame.setDefaultLookAndFeelDecorated(true);
			}
		});

		cancelRuleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WindowListener[] listeners = getWindowListeners();

				for (WindowListener listener : listeners) {
					listener.windowClosing(new WindowEvent(
							RuleEditorFrame.this, 0));
				}

			}
		});
		subRulesIcon.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

				if (!rulesList.isEmpty())
					try {

						SubRuleComponent c = rulesList.get(
								ruleSetCombo.getSelectedIndex())
								.addSubRuleCompo();
						c.addAncestorListener(rmvActL);
						subRulesPanel.add(c);

						revalidate();

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				else
					System.out.println("first add a rule!");

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

		neighbor4RadioBtn.addActionListener(neighbortypeActL);
		neighbor8RadioBtn.addActionListener(neighbortypeActL);
		neighbor24RadioBtn.addActionListener(neighbortypeActL);

		activeInputRadioBtn.addActionListener(ruleInputActL);
		passiveInputRadioBtn.addActionListener(ruleInputActL);

		activeOutputRadioBtn.addActionListener(ruleOutputActL);
		passiveOutputRadioBtn.addActionListener(ruleOutputActL);
		ruleSetCombo.addActionListener(ruleSetComboActL);

	}

	private void writeObjectIntoFile() {

		try (FileOutputStream fs = new FileOutputStream("RulesSet.bin");
				ObjectOutputStream os = new ObjectOutputStream(fs);) {

			if (!applied)
				os.writeInt(-1);
			else
				os.writeInt(rulesList.size());
			for (int i = 0; i < rulesList.size(); i++)
				os.writeObject(rulesList.get(i));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ArrayList<Rule> readObjectFromFile() throws ClassNotFoundException {

		try (FileInputStream fi = new FileInputStream("RulesSet.bin");
				ObjectInputStream os = new ObjectInputStream(fi);) {
			ArrayList<Rule> r = new ArrayList<>();
			int ok = os.readInt();
			if (ok != -1) {
				for (int i = 0; i < ok; i++) {
					Rule rq = (Rule) os.readObject();
					r.add(rq);
					System.out.println(rq.toString());
				}
			}
			return r;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	ActionListener neighbortypeActL = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand().toString()) {
			case "4 pt":
				if (!rulesList.isEmpty()
						&& ruleSetCombo.getSelectedIndex() != -1)
					rulesList.get(ruleSetCombo.getSelectedIndex())
							.setNeighborhoodType(neighbor4);
				else
					System.out.println("first add a rule!");
				break;
			case "8 pt":
				if (!rulesList.isEmpty()
						&& ruleSetCombo.getSelectedIndex() != -1)
					rulesList.get(ruleSetCombo.getSelectedIndex())
							.setNeighborhoodType(neighbor8);
				else
					System.out.println("first add a rule!");

				break;
			case "24 pt":
				if (!rulesList.isEmpty()
						&& ruleSetCombo.getSelectedIndex() != -1)
					rulesList.get(ruleSetCombo.getSelectedIndex())
							.setNeighborhoodType(neighbor24);
				else
					System.out.println("first add a rule!");

				break;
			}

			if (!rulesList.isEmpty() && ruleSetCombo.getSelectedIndex() != -1)
				try {
					for (SubRuleComponent i : rulesList.get(
							ruleSetCombo.getSelectedIndex())
							.getSubRuleCompoList()) {
						i.setNeighborhoodType(rulesList.get(
								ruleSetCombo.getSelectedIndex())
								.getNeighborhoodType());
						revalidate();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			else
				System.out.println("first add a rule!");
		}
	};
	ActionListener ruleInputActL = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand().toString()) {
			case "Active":
				if (!rulesList.isEmpty()
						&& ruleSetCombo.getSelectedIndex() != -1)
					rulesList.get(ruleSetCombo.getSelectedIndex())
							.setRuleInputState(active);
				else
					System.out.println("first add a rule!");
				break;
			case "Passive":
				if (!rulesList.isEmpty()
						&& ruleSetCombo.getSelectedIndex() != -1)
					rulesList.get(ruleSetCombo.getSelectedIndex())
							.setRuleInputState(passive);
				else
					System.out.println("first add a rule!");
				break;
			}

		}
	};
	ActionListener ruleOutputActL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand().toString()) {
			case "Active":
				if (!rulesList.isEmpty()
						&& ruleSetCombo.getSelectedIndex() != -1)
					rulesList.get(ruleSetCombo.getSelectedIndex())
							.setRuleOutputState(active);
				else
					System.out.println("first add a rule!");
				break;
			case "Passive":
				if (!rulesList.isEmpty()
						&& ruleSetCombo.getSelectedIndex() != -1)
					rulesList.get(ruleSetCombo.getSelectedIndex())
							.setRuleOutputState(passive);
				else
					System.out.println("first add a rule!");
				break;
			}

		}
	};

	AncestorListener rmvActL = new AncestorListener() {

		@Override
		public void ancestorRemoved(AncestorEvent event) {

			ArrayList<SubRuleComponent> list;
			try {
				list = rulesList.get(ruleSetCombo.getSelectedIndex())
						.getSubRuleCompoList();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getId() < 0) {

						rulesList.get(ruleSetCombo.getSelectedIndex())
								.removeSubruleComponent(
										Math.abs(list.get(i).getId()));

					}

					System.out.println("Removed:  " + list.get(i).toString());

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void ancestorMoved(AncestorEvent event) {

		}

		@Override
		public void ancestorAdded(AncestorEvent event) {

		}
	};
	ActionListener ruleSetComboActL = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			System.out.println("selected rule changed!");
			if (ruleSetCombo.getSelectedIndex() == -1) {
				for (Rule r : rulesList) {
					try {
						for (SubRuleComponent srC : r.getSubRuleCompoList()) {
							srC.setVisible(false);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} else
				try {
					ArrayList<SubRuleComponent> list = rulesList.get(
							preSelected)// any rule remove make exception
							.getSubRuleCompoList();
					for (SubRuleComponent s : list) {

						s.setVisible(false);
					}
					ArrayList<SubRuleComponent> list1 = rulesList.get(
							ruleSetCombo.getSelectedIndex())
							.getSubRuleCompoList();
					for (SubRuleComponent s : list1) {

						s.setVisible(true);
					}
					setFromRule(rulesList.get(ruleSetCombo.getSelectedIndex()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			preSelected = ruleSetCombo.getSelectedIndex();

		}
	};

}
