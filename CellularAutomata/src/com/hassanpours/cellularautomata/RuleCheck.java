package com.hassanpours.cellularautomata;

public class RuleCheck {

	private int id = 0;
	private Rule rule = new Rule();
	private int[][] arrayOfCells;
	private int[][] arrayOfCellsTmp;

	public RuleCheck(Rule Rule, int[][] GridPixels) {
		this.id++;
		arrayOfCellsTmp = new int[GridPixels.length][GridPixels.length];
		this.rule = Rule;
		this.arrayOfCells = GridPixels;
		for (int i = 0; i < arrayOfCells.length; i++) {
			for (int j = 0; j < arrayOfCells.length; j++) {
				arrayOfCellsTmp[i][j] = arrayOfCells[i][j];
			}
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Rule getRules() {
		return rule;
	}

	public void setRules(Rule rule) {
		this.rule = rule;
	}

	public int[][] getGridPixels() {
		return arrayOfCells;
	}

	public void setGridPixels(int[][] gridPixels) {
		arrayOfCells = gridPixels;
	}

	private int wholeNeighborhoodsum4pt(int cellX, int cellY) {
		int sum = 0;
		if (cellX - 1 > 0 && cellX + 1 < arrayOfCells.length && cellY - 1 > 0
				&& cellY + 1 < arrayOfCells.length)
			sum = arrayOfCells[cellX - 1][cellY]
					+ arrayOfCells[cellX + 1][cellY]
					+ arrayOfCells[cellX][cellY - 1]
					+ arrayOfCells[cellX][cellY + 1];
		return sum;
	}

	private int wholeNeighborhoodsum8pt(int cellX, int cellY) {
		int sum = 0;
		for (int i = cellX - 1; i < cellX + 2; i++) {
			for (int j = cellY - 1; j < cellY + 2; j++) {
				if (i >= 0 && j >= 0 && i < arrayOfCells.length
						&& j < arrayOfCells.length)
					sum += arrayOfCells[i][j];
			}
		}
		return sum;
	}

	private int wholeNeighborhoodsum24pt(int cellX, int cellY) {
		int sum = 0;
		for (int i = cellX - 2; i < cellX + 3; i++) {
			for (int j = cellY - 2; j < cellY + 3; j++) {
				if (i >= 0 && j >= 0 && i < arrayOfCells.length
						&& j < arrayOfCells.length)
					sum += arrayOfCells[i][j];
			}
		}
		return sum;
	}

	private void toChange(int i, int j, String ruleInputState,
			String ruleOutPutState) {

		switch (ruleInputState) {
		case "Active":
			if (arrayOfCells[i][j] == 1)
				switch (ruleOutPutState) {

				case "Active":
					arrayOfCellsTmp[i][j] = 1;
					
					break;
				case "Passive":
					arrayOfCellsTmp[i][j] = 0;
					
					break;
				}

			break;
		case "Passive":
			if (arrayOfCells[i][j] == 0)
				switch (ruleOutPutState) {

				case "Active":
					arrayOfCellsTmp[i][j] = 1;
					
					break;
				case "Passive":
					arrayOfCellsTmp[i][j] = 0;
					
					break;
				}

			break;
		}
	}

	public int[][] wholeNeighborhood(SubRule subrule, int cellX, int cellY) {

		int sum = 0;
		switch (rule.getNeighborhoodType()) {
		case "4 pt":
			sum = wholeNeighborhoodsum4pt(cellX, cellY);
			break;
		case "8 pt":
			sum = wholeNeighborhoodsum8pt(cellX, cellY);
			if (sum != 0)
				break;
		case "24 pt":
			sum = wholeNeighborhoodsum24pt(cellX, cellY);
			break;
		}

		switch (subrule.getHowManySign()) {
		case 0:
			if (sum == subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());

			}
			break;
		case 1:
			if (sum < subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());

			}
			break;
		case 2:
			if (sum > subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());

			}
			break;
		}

		return arrayOfCellsTmp;
	}

	public int inRowSum4pt(int cellX, int cellY, int RowNo) {
		int sum = 0;

		if (RowNo == 0) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				sum = arrayOfCells[cellX - 1][cellY];
		} else if (RowNo == 1) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				sum = arrayOfCells[cellX][cellY - 1]
						+ arrayOfCells[cellX][cellY + 1];
			;
		} else if (RowNo == 2) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				sum = arrayOfCells[cellX + 1][cellY];
		}
		return sum;
	}

	public int inRowSum8pt(int cellX, int cellY, int RowNo) {
		int sum = 0;

		if (RowNo == 0) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				for (int i = cellY - 1; i < cellY + 2; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[cellX - 1][i];
				}
		} else if (RowNo == 1) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				for (int i = cellY - 1; i < cellY + 2; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[cellX][i];
				}
		} else if (RowNo == 2) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				for (int i = cellY - 1; i < cellY + 2; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[cellX + 1][i];
				}
		}

		return sum;
	}

	public int inRowSum24pt(int cellX, int cellY, int RowNo) {
		int sum = 0;

		if (RowNo == 0) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellY - 2; i < cellY + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[cellX - 2][i];
				}
		} else if (RowNo == 1) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellY - 2; i < cellY + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[cellX - 1][i];
				}
			;
		} else if (RowNo == 2) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellY - 2; i < cellY + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[cellX][i];
				}
		} else if (RowNo == 3) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellY - 2; i < cellY + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[cellX + 1][i];
				}
		} else if (RowNo == 4) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellY - 2; i < cellY + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[cellX + 2][i];
				}
		}

		return sum;
	}

	public int[][] inSpesificRow(SubRule subrule, int cellX, int cellY,
			int RowNo) {
		int sum = 0;
		switch (rule.getNeighborhoodType()) {
		case "4 pt":
			sum = inRowSum4pt(cellX, cellY, RowNo);
			break;
		case "8 pt":
			sum = inRowSum8pt(cellX, cellY, RowNo);
			break;
		case "24 pt":
			sum = inRowSum24pt(cellX, cellY, RowNo);
			break;
		}
		switch (subrule.getHowManySign()) {
		case 0:
			if (sum == subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		case 1:
			if (sum < subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		case 2:
			if (sum > subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		}
		return arrayOfCellsTmp;

	}

	public int inColSum4pt(int cellX, int cellY, int ColNo) {
		int sum = 0;

		if (ColNo == 0) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				sum = arrayOfCells[cellX][cellY - 1];
		} else if (ColNo == 1) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				sum = arrayOfCells[cellX - 1][cellY]
						+ arrayOfCells[cellX + 1][cellY];
			;
		} else if (ColNo == 2) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				sum = arrayOfCells[cellX][cellY + 1];
		}

		return sum;
	}

	public int inColSum8pt(int cellX, int cellY, int ColNo) {

		int sum = 0;
		if (ColNo == 0) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				for (int i = cellX - 1; i < cellX + 2; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[i][cellY - 1];
				}
		} else if (ColNo == 1) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				for (int i = cellX - 1; i < cellX + 2; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[i][cellY];
				}
			;
		} else if (ColNo == 2) {
			if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
					&& cellX - 1 > 0 && cellX + 1 < arrayOfCells.length)
				for (int i = cellX - 1; i < cellX + 2; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[i][cellY + 1];
				}
		}
		return sum;
	}

	public int inColSum24pt(int cellX, int cellY, int ColNo) {
		int sum = 0;

		if (ColNo == 0) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellX - 2; i < cellX + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[i][cellY - 2];
				}
		} else if (ColNo == 1) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellX - 2; i < cellX + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[i][cellY - 1];
				}
			;
		} else if (ColNo == 2) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellX - 2; i < cellX + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[i][cellY];
				}
		} else if (ColNo == 3) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellX - 2; i < cellX + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[i][cellY + 1];
				}
		} else if (ColNo == 4) {
			if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
					&& cellX - 2 > 0 && cellX + 2 < arrayOfCells.length)
				for (int i = cellX - 2; i < cellX + 3; i++) {
					if (i >= 0 && i < arrayOfCells.length)
						sum += arrayOfCells[i][cellY + 2];
				}
		}

		return sum;
	}

	public int[][] inSpesificCol(SubRule subrule, int cellX, int cellY,
			int ColNo) {
		int sum = 0;
		switch (rule.getNeighborhoodType()) {
		case "4 pt":
			sum = inColSum4pt(cellX, cellY, ColNo);
			break;
		case "8 pt":
			sum = inColSum8pt(cellX, cellY, ColNo);
			break;
		case "24 pt":
			sum = inColSum24pt(cellX, cellY, ColNo);
			break;
		}
		switch (subrule.getHowManySign()) {
		case 0:
			if (sum == subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		case 1:
			if (sum < subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		case 2:
			if (sum > subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		}

		return arrayOfCellsTmp;
	}

	private int digonalSum8pt(int cellX, int cellY, int diagon) {
		int sum = 0;
		if (diagon == 22) {
			for (int i = cellX - 1; i < cellX + 2; i++) {
				for (int j = cellY - 1; j < cellY + 2; j++) {
					if (i == j)
						if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
								&& cellX - 1 > 0
								&& cellX + 1 < arrayOfCells.length)
							sum += arrayOfCells[i][j];
				}
			}
		} else if (diagon == 11) {
			for (int i = cellX - 1; i < cellX + 2; i++) {
				for (int j = cellY - 1; j < cellY + 2; j++) {
					if (i + j == 2)
						if (cellY - 1 > 0 && cellY + 1 < arrayOfCells.length
								&& cellX - 1 > 0
								&& cellX + 1 < arrayOfCells.length)
							sum += arrayOfCells[i][j];
				}
			}
		}
		return sum;
	}

	private int digonalSum24pt(int cellX, int cellY, int diagon) {
		int sum = 0;
		if (diagon == 11) {
			for (int i = cellX - 2; i < cellX + 3; i++) {
				for (int j = cellY - 2; j < cellY + 3; j++) {
					if (i == j)
						if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
								&& cellX - 2 > 0
								&& cellX + 2 < arrayOfCells.length)
							sum += arrayOfCells[i][j];
				}
			}
		} else if (diagon == 22) {
			for (int i = cellX - 2; i < cellX + 3; i++) {
				for (int j = cellY - 2; j < cellY + 3; j++) {
					if (i + j == 4)
						if (cellY - 2 > 0 && cellY + 2 < arrayOfCells.length
								&& cellX - 2 > 0
								&& cellX + 2 < arrayOfCells.length)
							sum += arrayOfCells[i][j];
				}
			}
		}

		return sum;
	}

	public int[][] ondiagonal(SubRule subrule, int cellX, int cellY,
			int diagonal) {
		int sum = 0;
		switch (rule.getNeighborhoodType()) {
		case "4 pt":

			break;
		case "8 pt":
			sum = digonalSum8pt(cellX, cellY, diagonal);
			
			break;
		case "24 pt":
			sum = digonalSum24pt(cellX, cellY, diagonal);
			break;
		}
		switch (subrule.getHowManySign()) {
		case 0:
			if (sum == subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		case 1:
			if (sum < subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		case 2:
			if (sum > subrule.getHowManyNom()) {
				toChange(cellX, cellY, rule.getRuleInputState(),
						rule.getRuleOutputState());
			}
			break;
		}

		return arrayOfCellsTmp;
	}

}
