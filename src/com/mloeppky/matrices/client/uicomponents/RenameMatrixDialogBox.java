package com.mloeppky.matrices.client.uicomponents;

import gwthelper.client.ComposedHPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.mloeppky.matrices.client.Matrix;
import com.mloeppky.matrices.client.handlers.RenameMatrixHandler;

public class RenameMatrixDialogBox extends MatrixDialogBox {
	
	public RenameMatrixDialogBox(final RenameMatrixHandler callback, final Matrix matrix) {
		super(null);
		
		this.setText("Rename Matrix");
		
		generateMatrixButton.setText("Rename").addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (matrixNameTb.getText().equals(DEFAULT_MATRIX_NAME)) {
					Window.alert("Please enter a proper name for the Matrix");
					matrixNameTb.selectAll();
					return;
				}
				String name = matrixNameTb.getText();
				callback.onRenameMatrix(matrix, name);
				RenameMatrixDialogBox.this.hide();
			}
		});
		
		ComposedHPanel generateMatrixPanel = new ComposedHPanel()
			.setSpacing(10)
			.addAll(matrixNameLabel, matrixNameTb, generateMatrixButton);
		
		this.add(generateMatrixPanel);
	}
}
