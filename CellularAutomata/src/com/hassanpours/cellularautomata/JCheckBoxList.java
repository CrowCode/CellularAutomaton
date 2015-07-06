package com.hassanpours.cellularautomata;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

@SuppressWarnings({ "rawtypes", "serial" })
public class JCheckBoxList extends JList {
	protected static Border noFocusBorder = new EmptyBorder(10, 10, 0, 10);

	
	@SuppressWarnings("unchecked")
	public JCheckBoxList()

	{
		setCellRenderer(new CheckBoxCellRenderer());

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int index = locationToIndex(e.getPoint());

				if (index != -1) {
					JCheckBox checkbox = (JCheckBox) getModel().getElementAt(
							index);
					checkbox.setSelected(!checkbox.isSelected());
					repaint();
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					int index = getSelectedIndex();
					if (index != -1) {
						JCheckBox checkbox = (JCheckBox) getModel()
								.getElementAt(index);
						checkbox.setSelected(!checkbox.isSelected());
						repaint();
					}
				}
			}
		});

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	protected class CheckBoxCellRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JCheckBox checkbox = (JCheckBox) value;
			checkbox.setBackground(isSelected ? getSelectionBackground()
					: getBackground());
			checkbox.setForeground(isSelected ? getSelectionForeground()
					: getForeground());

			checkbox.setEnabled(isEnabled());
			checkbox.setFont(getFont());
			checkbox.setFocusPainted(false);

			checkbox.setBorderPainted(true);
			checkbox.setBorder(isSelected ? UIManager
					.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

			return checkbox;
		}

	}

	@SuppressWarnings("unchecked")
	public void setListData(LinkedList<Object> RulesList) {

		Object[] list = new Object[RulesList.size()];
		int j = 0;
		for (Object i : RulesList) {
			list[j++] = i;
		}
		super.setListData(list);
	}

	@Override
	public int[] getSelectedIndices() {

		int sum = 0;
		int[] selectedList = new int[getModel().getSize()];
		for (int i = 0; i < getModel().getSize(); i++) {
			JCheckBox checkbox = (JCheckBox) getModel().getElementAt(i);
			if(checkbox.isSelected())
				selectedList[i] = 1;
			sum += selectedList[i];
		}
		int []s = new int [sum];
		int j =0;
		
		for (int i = 0; i < getModel().getSize(); i++) {
			if(selectedList[i] == 1){
				s[j++] = i;
			}
		}
		return s;
	}

}
