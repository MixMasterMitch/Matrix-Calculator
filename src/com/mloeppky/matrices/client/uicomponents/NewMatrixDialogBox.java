package com.mloeppky.matrices.client.uicomponents;

import static com.mloeppky.matrices.client.uicomponents.Styles.INVALID_CELL_DATA;
import static com.mloeppky.matrices.client.uicomponents.Styles.VALID_CELL_DATA;
import gwthelper.client.ComposedButton;
import gwthelper.client.ComposedFlexTable;
import gwthelper.client.ComposedHPanel;
import gwthelper.client.ComposedTextBox;
import gwthelper.client.ComposedVPanel;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.mloeppky.matrices.client.Matrix;
import com.mloeppky.matrices.client.handlers.NewMatrixHandler;

public class NewMatrixDialogBox extends MatrixDialogBox {
	
	private final ComposedFlexTable matrixTable;
	
	public NewMatrixDialogBox(final NewMatrixHandler callback) {
		super(callback);
		
		this.setText("New Matrix");
		
		ComposedButton addRowButton = new ComposedButton()
			.setText("Add Row")
			.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					int numRows = matrixTable.getRowCount();
					if (numRows == 0) {
						matrixTable.setWidget(0, 0, newMatrixCell());
					} else {
						int numCols = matrixTable.getCellCount(0);
						for (int c = 0; c < numCols; c++) {
							matrixTable.setWidget(numRows, c, newMatrixCell());
						}
					}
				}
			});
		
		ComposedButton removeRowButton = new ComposedButton()
			.setText("Remove Row")
			.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					matrixTable.removeRow(matrixTable.getRowCount() - 1);
				}
			});
		
		ComposedHPanel rowPanel = new ComposedHPanel()
			.addAll(addRowButton, removeRowButton)
			.setSpacing(10);
		
		ComposedButton addColButton = new ComposedButton()
			.setText("Add Column")
			.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (matrixTable.getRowCount() == 0) {
						matrixTable.setWidget(0, 0, newMatrixCell());
					} else {
						int numCols = matrixTable.getCellCount(0);
						for (int r = 0; r < matrixTable.getRowCount(); r++) {
							matrixTable.setWidget(r, numCols, newMatrixCell());
						}
					}
				}
			});
		
		ComposedButton removeColButton = new ComposedButton()
			.setText("Remove Column")
			.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					int numCols = matrixTable.getCellCount(0);
					if (numCols == 1) {
						matrixTable.removeAllRows();
					} else {
						for (int r = 0; r < matrixTable.getRowCount(); r++) {
							matrixTable.removeCell(r, numCols - 1);
						}
					}
				}
			});
		
		ComposedVPanel columnPanel = new ComposedVPanel()
			.addAll(addColButton, removeColButton)
			.setSpacing(10);
		
		generateMatrixButton.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!validCellData()) {
					Window.alert("Please fix invalid cell data");
					return;
				}
				if (matrixNameTb.getText().equals(DEFAULT_MATRIX_NAME)) {
					Window.alert("Please enter a proper name for the Matrix");
					matrixNameTb.selectAll();
					return;
				}
				String name = matrixNameTb.getText();
				int numRows = matrixTable.getRowCount();
				if (numRows == 0) {
					callback.onNewMatrix(new Matrix(0, 0).setName(name));
					NewMatrixDialogBox.this.hide();
					return;
				}
				int numCols = matrixTable.getCellCount(0);
				Matrix newMatrix = new Matrix(numRows, numCols);
				for (int r = 0; r < numRows; r++) {
					for (int c = 0; c < numCols; c++) {
						newMatrix.set(r, c, Double.valueOf(((ComposedTextBox) matrixTable.getWidget(r, c)).getText()));
					}
				}
				callback.onNewMatrix(newMatrix.setName(name));
				NewMatrixDialogBox.this.hide();
			}
			
			public boolean validCellData() {
				int numRows = matrixTable.getRowCount();
				if (numRows == 0) {
					return true;
				}
				int numCols = matrixTable.getCellCount(0);
				for (int r = 0; r < numRows; r++) {
					for (int c = 0; c < numCols; c++) {
						try {
							Double.valueOf(((ComposedTextBox) matrixTable.getWidget(r, c)).getText());
						} catch (NumberFormatException e) {
							return false;
						}
					}
				}
				return true;
			}
		});
		
		ComposedHPanel generateMatrixPanel = new ComposedHPanel()
			.setSpacing(10)
			.addAll(matrixNameLabel, matrixNameTb, generateMatrixButton);
		
		
		matrixTable = new ComposedFlexTable()
			.setRowWidgets(0, newMatrixCell(), newMatrixCell())
			.setRowWidgets(1, newMatrixCell(), newMatrixCell());
		
		ComposedFlexTable body = new ComposedFlexTable()
			.setRowWidgets(0, matrixTable, columnPanel)
			.setRowWidgets(1, rowPanel, generateMatrixPanel);
		body.getFlexCellFormatter().setColSpan(2, 0, 2);
		body.setWidget(2, 0, generateMatrixPanel);
		
		this.add(body);
	}
	
	private static ComposedTextBox newMatrixCell() {
		final ComposedTextBox cell = new ComposedTextBox()
			.setWidth(50)
			.setText("0")
			.setStyleName(VALID_CELL_DATA)
			.getFocusOnMouseOver();
		cell.addHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					try {
						Double.valueOf(cell.getText());
						cell.setStyleName(VALID_CELL_DATA);
					} catch (NumberFormatException e) {
						cell.setStyleName(INVALID_CELL_DATA);
					}
				}
			});
		return cell;
	}
}
