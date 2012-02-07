package com.mloeppky.matrices.client.uicomponents;

import gwthelper.client.ComposedHPanel;
import gwthelper.client.ComposedLabel;
import gwthelper.client.ComposedTextBox;
import gwthelper.client.ComposedVPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.mloeppky.matrices.client.Matrix;
import com.mloeppky.matrices.client.handlers.NewMatrixHandler;

public class NewIdentityMatrixDialogBox extends MatrixDialogBox {
	
	private final ComposedTextBox identityDimsTB = new ComposedTextBox().setText("0").getFocusOnMouseOver();
	
	public NewIdentityMatrixDialogBox(final NewMatrixHandler callback) {
		super(callback);
		
		this.setText("New Identity Matrix");
		
		generateMatrixButton.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!validIdentityDim()) {
					Window.alert("Please fix invalid cell data");
					return;
				}
				if (matrixNameTb.getText().equals(DEFAULT_MATRIX_NAME)) {
					Window.alert("Please enter a proper name for the Matrix");
					matrixNameTb.selectAll();
					return;
				}
				String name = matrixNameTb.getText();
				int dim = Integer.valueOf(identityDimsTB.getText());
				callback.onNewMatrix(Matrix.newIdentityMatrix(dim).setName(name));
				NewIdentityMatrixDialogBox.this.hide();
			}
			
			public boolean validIdentityDim() {
				try {
					Integer.valueOf(identityDimsTB.getText());
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}
		});
		
		ComposedHPanel identitySizePanel = new ComposedHPanel()
			.setSpacing(10)
			.addAll(new ComposedLabel().setText("Identity Size:"), identityDimsTB);
		
		ComposedHPanel generateMatrixPanel = new ComposedHPanel()
			.setSpacing(10)
			.addAll(matrixNameLabel, matrixNameTb, generateMatrixButton);
		
		this.add(new ComposedVPanel().addAll(identitySizePanel, generateMatrixPanel));
	}
}
