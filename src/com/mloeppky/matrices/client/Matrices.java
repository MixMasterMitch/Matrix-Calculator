package com.mloeppky.matrices.client;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newTreeMap;
import static com.mloeppky.matrices.client.uicomponents.Styles.DECORATOR_PANEL;
import gwthelper.client.ComposedRootPanel;

import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mloeppky.matrices.client.handlers.AddToEquationHandler;
import com.mloeppky.matrices.client.handlers.DeleteMatrixHandler;
import com.mloeppky.matrices.client.handlers.NewMatrixHandler;
import com.mloeppky.matrices.client.handlers.RenameMatrixHandler;
import com.mloeppky.matrices.client.uicomponents.MatricesPanel;
import com.mloeppky.matrices.client.uicomponents.MatrixCellList;
import com.mloeppky.matrices.client.uicomponents.SelectedMatrixPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Matrices implements EntryPoint, NewMatrixHandler, AddToEquationHandler, RenameMatrixHandler, DeleteMatrixHandler {
	
	private final Map<String, Matrix> matricesByName = newTreeMap();
	
	private MatrixCellList cellList;
	
	private final ComposedRootPanel root = new ComposedRootPanel("Matrices");
	
	private final SelectedMatrixPanel slectedMatrixPanel = new SelectedMatrixPanel(this, this, this);
	
	@Override
	public void onModuleLoad() {
		
		cellList = new MatrixCellList(
			new SelectionChangeEvent.Handler() {
	
				@Override
				public void onSelectionChange(SelectionChangeEvent event) {
					slectedMatrixPanel.setMatrix(cellList.getSelected());
				}
				
			});

		root.add(new MatricesPanel(cellList, this));
		
		Matrix r = new MatrixBuilder()
			.addRow(.260, -.025, -.110)
			.build();
		
		Matrix f = new MatrixBuilder()
			.addRow(.1454, -.0728, .1164)
			.build();
		
		Matrix ab = new MatrixBuilder()
			.addRow(.300, -.100, -.400)
			.build();
		
		Matrix cd = new MatrixBuilder()
			.addRow(-1.4, .6, -1)
			.build();
		
		Matrix ac = new MatrixBuilder()
			.addRow(-.4, 1, -.4)
			.build();
		
		Matrix v = new MatrixBuilder()
			.addRow(-.816, .231, -.250, 0)
			.addRow(.408, .923, .362, 1350*9.81)
			.addRow(-.408, .308, -.899, 0)
			.build();

		root.add(slectedMatrixPanel);
		root.setStyleName(DECORATOR_PANEL);
		
	}

	@Override
	public void onNewMatrix(Matrix matrix) {
		matricesByName.put(matrix.getName(), matrix);
		cellList.setRowData(newArrayList(matricesByName.values()));
		slectedMatrixPanel.setMatrix(matrix);
	}

	@Override
	public void onDeleteMatrix(Matrix matrix) {
		matricesByName.remove(matrix.getName());
		cellList.setRowData(newArrayList(matricesByName.values()));
		slectedMatrixPanel.setMatrix(null);
	}

	@Override
	public void onRenameMatrix(Matrix matrix, String name) {
		matricesByName.remove(matrix.getName());
		matrix.setName(name);
		matricesByName.put(matrix.getName(), matrix);
		cellList.setRowData(newArrayList(matricesByName.values()));
		slectedMatrixPanel.setMatrix(matrix);
	}

	@Override
	public void onAddToEquation(Matrix matrix) {
		// TODO Auto-generated method stub
		
	}
}
