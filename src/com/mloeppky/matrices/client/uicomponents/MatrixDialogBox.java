package com.mloeppky.matrices.client.uicomponents;

import gwthelper.client.ComposedButton;
import gwthelper.client.ComposedDialogBox;
import gwthelper.client.ComposedLabel;
import gwthelper.client.ComposedTextBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mloeppky.matrices.client.handlers.NewMatrixHandler;

abstract class MatrixDialogBox extends ComposedDialogBox {
	
	protected final NewMatrixHandler callback;
	protected final ComposedButton generateMatrixButton = new ComposedButton().setText("Generate Matrix");
	protected final ComposedButton cancelButton = new ComposedButton().setText("Cancel");
	protected final ComposedLabel matrixNameLabel = new ComposedLabel().setText("Matrix name:");
	protected final ComposedTextBox matrixNameTb = new ComposedTextBox().getFocusOnMouseOver();
	
	protected static final String DEFAULT_MATRIX_NAME = "Enter name here... ";
	
	public MatrixDialogBox(final NewMatrixHandler callback) {
		this.callback = callback;
		matrixNameTb
			.setText(DEFAULT_MATRIX_NAME)
			.setWidth(150);
		cancelButton.addHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MatrixDialogBox.this.hide();
			}
			
		});

		this.setGlassEnabled(true)
			.setAutoHideEnabled(true)
			.show()
			.center();
	}
}
