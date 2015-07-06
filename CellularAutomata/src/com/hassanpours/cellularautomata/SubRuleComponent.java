package com.hassanpours.cellularautomata;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SubRuleComponent extends JPanel {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private static int iid;
	private int id;
	private String rulePlace1 = new String("in whole neighborhood");
	private String rulePlace2 = new String("in specific row");
	private String rulePlace3 = new String("in specific column");
	private String rulePlace4 = new String("in specific diagonal");
	private String whichString = new String("Which one?");
	private String aLiveString = new String("alive neighbors");
	private String iWantString = new String("I want");
	private String compSign[] = { "=", "<", ">" };
	private String neighborhoodType;

	private Integer[] cellNoInNeighborhood4pt = new Integer[3];
	private Integer[] cellNoInNeighborhood8pt = new Integer[3];
	private Integer[] cellNoInNeighborhood24pt = new Integer[5];

	private JComboBox<String> ruleComparisonSignCombo;

	private JComboBox<Integer> ruleRowColumnPos4pt;
	private JComboBox<Integer> ruleRowColumnPos8pt;
	private JComboBox<Integer> ruleRowColumnPos24pt;

	private JSpinner aliveNomSpinner4pt;
	private JSpinner aliveNomSpinner4pt1;
	private JSpinner aliveNomSpinner4pt2;
	private JSpinner aliveNomSpinner8pt;
	private JSpinner aliveNomSpinner8pt1;
	private JSpinner aliveNomSpinner24pt;
	private JSpinner aliveNomSpinner24pt1;

	private SpinnerModel aliveNomModel4pt = new SpinnerNumberModel(0, 0, 4, 1);
	private SpinnerModel aliveNomModel8pt = new SpinnerNumberModel(0, 0, 8, 1);
	private SpinnerModel aliveNomModel24pt = new SpinnerNumberModel(0, 0, 24, 1);

	private SpinnerModel aliveNomModelRC4pt1 = new SpinnerNumberModel(0, 0, 1,
			1);
	private SpinnerModel aliveNomModelRC4pt2 = new SpinnerNumberModel(0, 0, 3,
			1);
	private SpinnerModel aliveNomModelRC8pt = new SpinnerNumberModel(0, 0, 3, 1);
	private SpinnerModel aliveNomModelRC24pt = new SpinnerNumberModel(0, 0, 5,
			1);

	// private SpinnerModel aliveNomModel8pt = new SpinnerNumberModel(0, 0, 8,
	// 1);
	// private SpinnerModel aliveNomModel24pt = new SpinnerNumberModel(0, 0, 24,
	// 1);

	private JPanel ruleRowColumnPosMainPanel;
	private JPanel ruleRowColumnPosPanel4pt;
	private JPanel ruleRowColumnPosPanel8pt;
	private JPanel ruleRowColumnPosPanel24pt;

	private JPanel aliveNomMainPanel;
	private JPanel aliveNomPanel4pt;
	private JPanel aliveNomPanel8pt;
	private JPanel aliveNomPanel24pt;

	private JPanel aliveNomPanel4pt1;
	private JPanel aliveNomPanel4pt2;

	private JPanel aliveNomPanel8pt1;

	private JPanel aliveNomPanel24pt1;

	private CardLayout changeNeighborLayout;
	private CardLayout changeNeighborcLayout;

	private JRadioButton rulePlaceRadiobtn1;
	private JRadioButton rulePlaceRadiobtn2;
	private JRadioButton rulePlaceRadiobtn3;
	private JRadioButton rulePlaceRadiobtn4;

	private Box posTypeBox;
	private Box rulePosNomStyleBox;

	private JRadioButton ruleDiagonbtn1;
	private JRadioButton ruleDiagonbtn2;

	private JLabel removeSubruleIcon;
	private JLabel aLiveLabel;
	private JLabel iWantLabel;

	private JPanel pane = new JPanel();
	private SubRule subRule;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	GridBagConstraints constraints = new GridBagConstraints();

	public SubRuleComponent(String neighborhoodType, int ruleId)
			throws IOException {
		this.id = iid;
		SubRuleComponent.iid++;
		this.neighborhoodType = neighborhoodType;
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(800, 100));
		setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, Color.black));
		init();
		setupComponent();
		addHandlers();
	}

	public SubRuleComponent(SubRule subrule, String neighborhoodType)
			throws IOException {

		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(800, 100));
		this.neighborhoodType = neighborhoodType;
		init();
		this.subRule.coppy(subrule);
		setupComponent();
		addHandlers();
		setFromRule();

	}

	private void addHandlers() {

		ruleComparisonSignCombo.addActionListener(subRulecompSignActl);
		rulePlaceRadiobtn1.addActionListener(PosTypeActL);
		rulePlaceRadiobtn2.addActionListener(PosTypeActL);
		rulePlaceRadiobtn3.addActionListener(PosTypeActL);
		rulePlaceRadiobtn4.addActionListener(PosTypeActL);
		ruleRowColumnPos4pt.addActionListener(subRuleOnPos4ptActL);
		ruleRowColumnPos8pt.addActionListener(subRuleOnPos8ptActL);
		ruleRowColumnPos24pt.addActionListener(subRuleOnPos24ptActL);
		ruleDiagonbtn1.addActionListener(diagonalTypeActL);
		ruleDiagonbtn2.addActionListener(diagonalTypeActL);

	}

	public void init() throws IOException {

		setBorder(BorderFactory.createMatteBorder(1, 2, 1, 2, Color.black));
		subRule = new SubRule(id);

		ImageIcon redRemoveIcon = createImageIcon("list_remove.png", "Add", 30,
				30);

		changeNeighborLayout = new CardLayout();
		changeNeighborcLayout = new CardLayout();

		removeSubruleIcon = new JLabel(redRemoveIcon);
		iWantLabel = new JLabel(iWantString);

		ruleComparisonSignCombo = new JComboBox<>(compSign);

		aliveNomMainPanel = new JPanel(changeNeighborLayout);

		aliveNomSpinner4pt = new JSpinner(aliveNomModel4pt);
		aliveNomSpinner4pt1 = new JSpinner(aliveNomModelRC4pt1);
		aliveNomSpinner4pt2 = new JSpinner(aliveNomModelRC4pt2);

		aliveNomSpinner8pt = new JSpinner(aliveNomModel8pt);
		aliveNomSpinner8pt1 = new JSpinner(aliveNomModelRC8pt);
		aliveNomSpinner24pt = new JSpinner(aliveNomModel24pt);

		aliveNomSpinner24pt1 = new JSpinner(aliveNomModelRC24pt);

		aliveNomPanel4pt = new JPanel();
		aliveNomPanel4pt1 = new JPanel();
		aliveNomPanel4pt2 = new JPanel();

		aliveNomPanel8pt = new JPanel();
		aliveNomPanel8pt1 = new JPanel();
		aliveNomPanel24pt = new JPanel();
		aliveNomPanel24pt1 = new JPanel();

		aliveNomPanel4pt.add(aliveNomSpinner4pt);
		aliveNomPanel4pt1.add(aliveNomSpinner4pt1);
		aliveNomPanel4pt2.add(aliveNomSpinner4pt2);

		aliveNomPanel8pt.add(aliveNomSpinner8pt);
		aliveNomPanel8pt1.add(aliveNomSpinner8pt1);
		aliveNomPanel24pt.add(aliveNomSpinner24pt);
		aliveNomPanel24pt1.add(aliveNomSpinner24pt1);

		aLiveLabel = new JLabel(aLiveString);

		rulePlaceRadiobtn1 = new JRadioButton(rulePlace1);
		rulePlaceRadiobtn1.setActionCommand(rulePlace1);
		rulePlaceRadiobtn1.setSelected(true);

		rulePlaceRadiobtn2 = new JRadioButton(rulePlace2);
		rulePlaceRadiobtn2.setActionCommand(rulePlace2);
		rulePlaceRadiobtn3 = new JRadioButton(rulePlace3);
		rulePlaceRadiobtn3.setActionCommand(rulePlace3);
		rulePlaceRadiobtn4 = new JRadioButton(rulePlace4);
		rulePlaceRadiobtn4.setActionCommand(rulePlace4);

		ruleDiagonbtn1 = new JRadioButton("	/");
		ruleDiagonbtn1.setActionCommand("1");
		ruleDiagonbtn1.setSelected(true);
		ruleDiagonbtn2 = new JRadioButton("	\\");
		ruleDiagonbtn2.setActionCommand("2");

		for (int i = 0; i < 3; i++)
			cellNoInNeighborhood4pt[i] = i;

		for (int i = 0; i < 3; i++)
			cellNoInNeighborhood8pt[i] = i;

		for (int i = 0; i < 5; i++)
			cellNoInNeighborhood24pt[i] = i;

		ruleRowColumnPosMainPanel = new JPanel(changeNeighborcLayout);

		ruleRowColumnPosPanel4pt = new JPanel();
		ruleRowColumnPosPanel8pt = new JPanel();
		ruleRowColumnPosPanel24pt = new JPanel();

		ruleRowColumnPos4pt = new JComboBox<>(cellNoInNeighborhood4pt);
		ruleRowColumnPos8pt = new JComboBox<>(cellNoInNeighborhood8pt);
		ruleRowColumnPos24pt = new JComboBox<>(cellNoInNeighborhood24pt);

		ruleRowColumnPosPanel4pt.add(ruleRowColumnPos4pt);
		ruleRowColumnPosPanel8pt.add(ruleRowColumnPos8pt);
		ruleRowColumnPosPanel24pt.add(ruleRowColumnPos24pt);

		if (neighborhoodType.equals("4 pt")) {
			aliveNomMainPanel.add(aliveNomPanel4pt, "4 pt");
			aliveNomMainPanel.add(aliveNomPanel4pt1, "4 pt 1r");
			aliveNomMainPanel.add(aliveNomPanel4pt2, "4 pt 2r");
			aliveNomMainPanel.add(aliveNomPanel8pt, "8 pt");
			aliveNomMainPanel.add(aliveNomPanel24pt, "24 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel4pt, "4 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel8pt, "8 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel24pt, "24 pt");
			aliveNomMainPanel.add(aliveNomPanel8pt1, "8 pt ar");
			aliveNomMainPanel.add(aliveNomPanel24pt1, "24 pt 1");
		}

		if (neighborhoodType.equals("8 pt")) {
			aliveNomMainPanel.add(aliveNomPanel8pt, "8 pt");
			aliveNomMainPanel.add(aliveNomPanel8pt1, "8 pt ar");
			aliveNomMainPanel.add(aliveNomPanel24pt, "24 pt");
			aliveNomMainPanel.add(aliveNomPanel24pt1, "24 pt 1");
			aliveNomMainPanel.add(aliveNomPanel4pt, "4 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel8pt, "8 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel24pt, "24 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel4pt, "4 pt");
			aliveNomMainPanel.add(aliveNomPanel4pt1, "4 pt 1r");
			aliveNomMainPanel.add(aliveNomPanel4pt2, "4 pt 2r");

		}

		if (neighborhoodType.equals("24 pt")) {
			aliveNomMainPanel.add(aliveNomPanel24pt, "24 pt");
			aliveNomMainPanel.add(aliveNomPanel24pt1, "24 pt 1");
			aliveNomMainPanel.add(aliveNomPanel8pt, "8 pt");
			aliveNomMainPanel.add(aliveNomPanel4pt, "4 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel24pt, "24 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel8pt, "8 pt");
			ruleRowColumnPosMainPanel.add(ruleRowColumnPosPanel4pt, "4 pt");
			aliveNomMainPanel.add(aliveNomPanel4pt1, "4 pt 1r");
			aliveNomMainPanel.add(aliveNomPanel4pt2, "4 pt 2r");
			aliveNomMainPanel.add(aliveNomPanel8pt1, "8 pt ar");

		}

		posTypeBox = Box.createVerticalBox();
		ButtonGroup posTypeGroup = new ButtonGroup();
		posTypeGroup.add(rulePlaceRadiobtn1);
		posTypeGroup.add(rulePlaceRadiobtn2);
		posTypeGroup.add(rulePlaceRadiobtn3);
		posTypeGroup.add(rulePlaceRadiobtn4);
		posTypeBox.add(rulePlaceRadiobtn1);
		posTypeBox.add(rulePlaceRadiobtn2);
		posTypeBox.add(rulePlaceRadiobtn3);
		posTypeBox.add(rulePlaceRadiobtn4);

		rulePosNomStyleBox = Box.createVerticalBox();
		ButtonGroup ruleGroup2 = new ButtonGroup();
		ruleGroup2.add(ruleDiagonbtn1);
		ruleGroup2.add(ruleDiagonbtn2);
		rulePosNomStyleBox.add(ruleDiagonbtn1);
		rulePosNomStyleBox.add(ruleDiagonbtn2);

		removeSubruleIcon.addMouseListener(removeSubRuleActL);
		aliveNomSpinner4pt.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				subRule.setHowManyNom((int) aliveNomSpinner4pt.getValue());

			}
		});
		aliveNomSpinner4pt1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				subRule.setHowManyNom((int) aliveNomSpinner4pt1.getValue());

			}
		});
		aliveNomSpinner4pt2.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				subRule.setHowManyNom((int) aliveNomSpinner4pt2.getValue());

			}
		});
		aliveNomSpinner8pt.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				subRule.setHowManyNom((int) aliveNomSpinner8pt.getValue());

			}
		});
		aliveNomSpinner8pt1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				subRule.setHowManyNom((int) aliveNomSpinner8pt1.getValue());

			}
		});
		aliveNomSpinner24pt.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				subRule.setHowManyNom((int) aliveNomSpinner24pt.getValue());

			}
		});
		aliveNomSpinner24pt1.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				subRule.setHowManyNom((int) aliveNomSpinner24pt1.getValue());

			}
		});

		subRule.setHowManySign(0);
		subRule.setHowManyNom((int) aliveNomSpinner4pt.getValue());
		subRule.setOnType(rulePlace1);
		subRule.setOnPos(-1);
	}

	public SubRule getSubRule() {
		return subRule;
	}

	public void setSubRule(SubRule subRule) {
		this.subRule = subRule;
	}

	public String getNeighborhoodType() {
		return neighborhoodType;
	}

	public void setNeighborhoodType(String neighborhoodType) {
		this.neighborhoodType = neighborhoodType;
		changeNeighborLayout.show(aliveNomMainPanel, this.neighborhoodType);
		changeNeighborcLayout.show(ruleRowColumnPosMainPanel,
				this.neighborhoodType);
	}

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

	protected void setupComponent() {

		pane.setBorder(BorderFactory.createTitledBorder(whichString));
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(0, 20, 0, 10);
		addGB(removeSubruleIcon, 0, 0);
		constraints.insets = new Insets(0, 5, 0, 10);
		addGB(iWantLabel, 1, 0);
		constraints.insets = new Insets(0, 10, 0, 5);
		addGB(ruleComparisonSignCombo, 2, 0);
		constraints.insets = new Insets(0, 5, 0, 10);
		addGB(aliveNomMainPanel, 3, 0);
		constraints.insets = new Insets(0, 5, 0, 10);
		addGB(aLiveLabel, 4, 0);
		constraints.insets = new Insets(10, 10, 10, 10);
		addGB(posTypeBox, 5, 0);
		constraints.ipadx = 90;
		constraints.ipady = 15;
		constraints.insets = new Insets(10, 10, 10, 10);
		addGB(pane, 6, 0);

	}

	void addGB(Component component, int x, int y) {
		constraints.gridx = x;
		constraints.gridy = y;
		add(component, constraints);
	}

	ActionListener PosTypeActL = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			switch (e.getActionCommand()) {
			case "in whole neighborhood":
				pane.remove(rulePosNomStyleBox);
				pane.remove(ruleRowColumnPosMainPanel);
				switch (neighborhoodType) {
				case "4 pt":

					changeNeighborLayout.show(aliveNomMainPanel, "4 pt");
					break;
				case "8 pt":

					changeNeighborLayout.show(aliveNomMainPanel, "8 pt");
					break;
				case "24 pt":

					changeNeighborLayout.show(aliveNomMainPanel, "24 pt");
					break;
				}

				subRule.setOnPos(-1);
				subRule.setOnType(rulePlace1);
				validate();
				break;
			case "in specific row":
				pane.remove(rulePosNomStyleBox);
				pane.add(ruleRowColumnPosMainPanel);

				subRule.setOnType(rulePlace2);
				switch (neighborhoodType) {
				case "4 pt":
					subRule.setOnPos(ruleRowColumnPos4pt.getSelectedIndex());
					changeNeighborLayout.show(aliveNomMainPanel, "4 pt 1r");
					break;
				case "8 pt":
					subRule.setOnPos(ruleRowColumnPos8pt.getSelectedIndex());
					changeNeighborLayout.show(aliveNomMainPanel, "8 pt ar");
					break;
				case "24 pt":
					subRule.setOnPos(ruleRowColumnPos24pt.getSelectedIndex());
					changeNeighborLayout.show(aliveNomMainPanel, "24 pt 1");
					break;
				}
				validate();
				break;
			case "in specific column":
				pane.remove(rulePosNomStyleBox);
				pane.add(ruleRowColumnPosMainPanel);

				subRule.setOnType(rulePlace3);
				switch (neighborhoodType) {
				case "4 pt":
					subRule.setOnPos(ruleRowColumnPos4pt.getSelectedIndex());
					changeNeighborLayout.show(aliveNomMainPanel, "4 pt 1r");
					break;
				case "8 pt":
					subRule.setOnPos(ruleRowColumnPos8pt.getSelectedIndex());
					changeNeighborLayout.show(aliveNomMainPanel, "8 pt ar");
					break;
				case "24 pt":
					subRule.setOnPos(ruleRowColumnPos24pt.getSelectedIndex());
					changeNeighborLayout.show(aliveNomMainPanel, "24 pt 1");
					break;
				}

				validate();
				break;
			case "in specific diagonal":
				pane.remove(ruleRowColumnPosMainPanel);
				pane.add(rulePosNomStyleBox);
				subRule.setOnType(rulePlace4);
				subRule.setOnPos(11);
				validate();
				break;

			}

		}
	};
	MouseListener removeSubRuleActL = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			setVisible(false);
			id = (-1) * id;
			/* Remove the component also should be added here */

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	};
	ActionListener subRulecompSignActl = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			int i = ruleComparisonSignCombo.getSelectedIndex();
			subRule.setHowManySign(i);

		}
	};
	ActionListener subRuleOnPos4ptActL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			subRule.setOnPos(ruleRowColumnPos4pt.getSelectedIndex());

			switch (ruleRowColumnPos4pt.getSelectedIndex()) {
			case 0:
				changeNeighborLayout.show(aliveNomMainPanel, "4 pt 1r");
				break;
			case 1:
				changeNeighborLayout.show(aliveNomMainPanel, "4 pt 2r");
				break;
			case 2:
				changeNeighborLayout.show(aliveNomMainPanel, "4 pt 1r");
				break;
			}

		}
	};
	ActionListener subRuleOnPos8ptActL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			subRule.setOnPos(ruleRowColumnPos8pt.getSelectedIndex());

		}
	};
	ActionListener subRuleOnPos24ptActL = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			subRule.setOnPos(ruleRowColumnPos24pt.getSelectedIndex());

		}
	};
	ActionListener aliveNoActL = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			subRule.setHowManyNom((int) aliveNomSpinner4pt.getValue());

		}
	};
	ActionListener diagonalTypeActL = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "2":
				subRule.setOnPos(22);
				break;
			case "1":
				subRule.setOnPos(11);
				break;
			}

		}
	};

	void setFromRule() {
		ruleComparisonSignCombo.setSelectedIndex(subRule.getHowManySign());

		ruleComparisonSignCombo.setSelectedIndex(subRule.getHowManySign());
		switch (neighborhoodType) {
		case "4 pt":
			aliveNomSpinner4pt.setValue(subRule.getHowManyNom());
			break;
		case "8 pt":
			aliveNomSpinner8pt.setValue(subRule.getHowManyNom());
			break;
		case "24 pt":
			aliveNomSpinner24pt.setValue(subRule.getHowManyNom());
			break;
		}
		switch (subRule.getOnType()) {

		case "in whole neighborhood":
			rulePlaceRadiobtn1.setSelected(true);
			pane.remove(rulePosNomStyleBox);
			pane.remove(ruleRowColumnPosMainPanel);
			validate();
			break;

		case "in specific row":
			pane.remove(rulePosNomStyleBox);
			pane.add(ruleRowColumnPosMainPanel);
			rulePlaceRadiobtn2.setSelected(true);
			switch (neighborhoodType) {
			case "4 pt":
				subRule.setOnPos(ruleRowColumnPos4pt.getSelectedIndex());//
				break;
			case "8 pt":
				subRule.setOnPos(ruleRowColumnPos8pt.getSelectedIndex());//
				break;
			case "24 pt":
				subRule.setOnPos(ruleRowColumnPos24pt.getSelectedIndex());//
				break;
			}
			validate();
			break;
		case "in specific column":
			pane.remove(rulePosNomStyleBox);
			pane.add(ruleRowColumnPosMainPanel);
			rulePlaceRadiobtn3.setSelected(true);
			switch (neighborhoodType) {
			case "4 pt":
				subRule.setOnPos(ruleRowColumnPos4pt.getSelectedIndex());//
				break;
			case "8 pt":
				subRule.setOnPos(ruleRowColumnPos8pt.getSelectedIndex());//
				break;
			case "24 pt":
				subRule.setOnPos(ruleRowColumnPos24pt.getSelectedIndex());//
				break;
			}
			validate();
			break;
		case "in specific diagonal":
			pane.remove(ruleRowColumnPosMainPanel);
			pane.add(rulePosNomStyleBox);
			rulePlaceRadiobtn4.setSelected(true);

			switch (subRule.getOnPos()) {
			case 11:
				ruleDiagonbtn1.setSelected(true);
				break;
			case 22:
				ruleDiagonbtn2.setSelected(true);
				break;
			}
			validate();
			break;
		}
	}

}
