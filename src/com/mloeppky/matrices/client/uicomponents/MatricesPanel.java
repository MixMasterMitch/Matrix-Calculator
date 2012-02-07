package com.mloeppky.matrices.client.uicomponents;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER;
import gwthelper.client.ComposedButton;
import gwthelper.client.ComposedHPanel;
import gwthelper.client.ComposedLabel;
import gwthelper.client.ComposedVPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mloeppky.matrices.client.handlers.NewMatrixHandler;

public class MatricesPanel extends ComposedVPanel {
	
	private MatricesPanel() {}
	
	public MatricesPanel(MatrixCellList cellList, final NewMatrixHandler handler) {

		final ComposedLabel header = new ComposedLabel().setText("Matrices");
		
		final ComposedButton addMatrixButton = new ComposedButton()
			.setText("New Matrix")
			.addHandler(new ClickHandler() {
	
				@Override
				public void onClick(ClickEvent event) {
					new NewMatrixDialogBox(handler);
				}
				
			});
			
		final ComposedButton addIdentityMatrixButton = new ComposedButton()
			.setText("New Identity")
			.addHandler(new ClickHandler() {
		
				@Override
				public void onClick(ClickEvent event) {
					new NewIdentityMatrixDialogBox(handler);
				}
				
			});
		
		
		final ComposedHPanel buttonPanel = new ComposedHPanel()
			.addAll(addMatrixButton, addIdentityMatrixButton)
			.setSpacing(10);
		
		super.addWithAlignment(ALIGN_CENTER, 
				header,
				cellList,
				buttonPanel);
	}
}
