package com.mloeppky.matrices.client.uicomponents;

import static com.mloeppky.matrices.client.uicomponents.Styles.SCROLL_PANEL;
import gwthelper.client.ComposedCellList;
import gwthelper.client.ComposedScrollPanel;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mloeppky.matrices.client.Matrix;

public class MatrixCellList extends ComposedScrollPanel {
	
	private static final int WIDTH = 175;
	private static final int CELL_HEIGHT = 18;
	private static final int NUM_VISIBLE_CELLS = 8;
	
	private final ComposedCellList<Matrix> cellList;

	public MatrixCellList(SelectionChangeEvent.Handler changeHandler) {
		cellList = new ComposedCellList<Matrix>(new MatrixCell(), new MatrixCellKeyProvider())
	    	.setPageSize(30)
	    	.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE)
	    	.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION)
			.setWidth(WIDTH)
			.setHeight(CELL_HEIGHT * NUM_VISIBLE_CELLS);
	    
	    //Add selection model
	    final SingleSelectionModel<Matrix> selectionModel = new SingleSelectionModel<Matrix>(new MatrixCellKeyProvider());
	    cellList.setSelectionModel(selectionModel);
	    selectionModel.addSelectionChangeHandler(changeHandler);
	    
	    add(cellList);
	    setWidth(WIDTH);
	    setStyleName(SCROLL_PANEL);
	}
	
	/**
	 * @param values
	 * @return
	 * @see gwthelper.client.ComposedAbstractHasData#setRowData(java.util.List)
	 */
	public final MatrixCellList setRowData(
			List<? extends Matrix> values) {
		cellList.setRowData(values);
		if (values.size() > NUM_VISIBLE_CELLS) {
			cellList.setWidth(WIDTH - 15);
		}
		return this;
	}

	public Matrix getSelected() {
		return ((SingleSelectionModel<Matrix>) cellList.getSelectionModel()).getSelectedObject();
	}
}

class MatrixCell extends AbstractCell<Matrix> {
	
	@Override
	public void render(Context context, Matrix value, SafeHtmlBuilder sb) {
		// Value can be null, so do a null check..
		if (value == null) {
			return;
		}
		sb.appendEscaped(value.getName() + " (" + value.getRows() + "x" + value.getCols() + ")");
	}
}

class MatrixCellKeyProvider implements ProvidesKey<Matrix> {

	@Override
	public Object getKey(Matrix item) {
		return item.getName();
	}
	
}

