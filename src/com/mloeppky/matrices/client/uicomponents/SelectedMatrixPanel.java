package com.mloeppky.matrices.client.uicomponents;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_RIGHT;
import static com.mloeppky.matrices.client.uicomponents.Styles.DECORATOR_PANEL;
import static com.mloeppky.matrices.client.uicomponents.Styles.MATRIX_FLEX_TABLE;
import static com.mloeppky.matrices.client.uicomponents.Styles.MATRIX_FLEX_TABLE_CELL;
import static com.mloeppky.matrices.client.uicomponents.Styles.SCROLL_PANEL_GRAY;
import gwthelper.client.ComposedButton;
import gwthelper.client.ComposedCheckBox;
import gwthelper.client.ComposedFlexTable;
import gwthelper.client.ComposedHPanel;
import gwthelper.client.ComposedLabel;
import gwthelper.client.ComposedScrollPanel;
import gwthelper.client.ComposedVPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.mloeppky.matrices.client.Matrix;
import com.mloeppky.matrices.client.handlers.AddToEquationHandler;
import com.mloeppky.matrices.client.handlers.DeleteMatrixHandler;
import com.mloeppky.matrices.client.handlers.RenameMatrixHandler;

public class SelectedMatrixPanel extends ComposedVPanel {
	
	private final ComposedLabel header = new ComposedLabel().setText("Selected Matrix");
	private final ComposedLabel matrixNameLabel = new ComposedLabel();
	private final ComposedFlexTable matrixData = new ComposedFlexTable();
	private final ComposedFlexTable matrixInfo = new ComposedFlexTable();
	private final ComposedLabel echelonFormLabel = new ComposedLabel().setText("Echelon Form");
	private final ComposedCheckBox echelonFormCheckBox = new ComposedCheckBox();
	private final ComposedLabel reducedEFormLabel = new ComposedLabel().setText("Reduced E Form");
	private final ComposedCheckBox reducedEFormCheckBox = new ComposedCheckBox();
	private final ComposedButton addToEquation = new ComposedButton().setText("Add to Equation");
	private final ComposedButton rename = new ComposedButton().setText("Rename");
	private final ComposedButton delete = new ComposedButton().setText("Delete");
	
	private Matrix matrix;
	
	public SelectedMatrixPanel(final AddToEquationHandler addToEquationHandler, final RenameMatrixHandler renameMatrixHandler, final DeleteMatrixHandler deleteMatrixHandler) {
		
		setMatrix(null);
		
		addToEquation.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				addToEquationHandler.onAddToEquation(matrix);
			}
		});
		
		rename.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new RenameMatrixDialogBox(renameMatrixHandler, matrix);
			}
		});
		
		delete.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean delete = Window.confirm("Are you sure you want to delete matrix " + matrix.getName() + "?");
				if (delete) {
					deleteMatrixHandler.onDeleteMatrix(matrix);
				}
			}
		});
		
		matrixData.setStyleName(MATRIX_FLEX_TABLE);
		
		ComposedScrollPanel matrixDataScrollPanel = new ComposedScrollPanel()
			.add(matrixData)
			.setSize(400, 200)
			.setStyleName(SCROLL_PANEL_GRAY);
		
		matrixInfo
			.setRowWidgets(0, echelonFormLabel, echelonFormCheckBox)
			.setRowWidgets(1, reducedEFormLabel, reducedEFormCheckBox);
		
		ComposedHPanel dataAndInfoPanel = new ComposedHPanel()
			.addAll(matrixDataScrollPanel, matrixInfo)
			.setSpacing(10);
		
		ComposedHPanel buttonPanel = new ComposedHPanel()
			.addAll(addToEquation, rename, delete)
			.setSpacing(10);
		
		ComposedVPanel body = new ComposedVPanel()
			.addWithAlignment(ALIGN_CENTER, matrixNameLabel, dataAndInfoPanel)
			.addWithAlignment(ALIGN_RIGHT, buttonPanel)
			.setStyleName(DECORATOR_PANEL);
		
		this.addWithAlignment(ALIGN_CENTER, header, body);
		
		
	}
	
	public SelectedMatrixPanel setMatrix(Matrix matrix) {
		this.matrix = matrix;
		if (matrix == null) {
			matrixNameLabel.setText("");
			echelonFormCheckBox.setValue(false);
			reducedEFormCheckBox.setValue(false);
			matrixData.removeAllRows();
		} else {
			matrixNameLabel.setText(matrix.getName() + " (" + matrix.getRows() + "x" + matrix.getCols() + ")");
			echelonFormCheckBox.setValue(matrix.isEchelonForm());
			reducedEFormCheckBox.setValue(matrix.isReducedEchelonForm());
			matrixData.removeAllRows();
			for (int r = 0; r < matrix.getRows(); r++) {
				for (int c = 0; c < matrix.getCols(); c++) {
					matrixData.setText(r, c, "" + matrix.getFormatted(r, c))
						.setCellSpacing(1)
						.setSize(400, 200);
					matrixData.getCellFormatter().setHorizontalAlignment(r, c, ALIGN_RIGHT);
					matrixData.getCellFormatter().addStyleName(r, c, MATRIX_FLEX_TABLE_CELL);
				}
			}
		}
		return this;
	}
}
