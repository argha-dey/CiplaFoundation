package com.ciplafoundation.nLevelTree;


import android.view.View;

import com.ciplafoundation.model.TreeDataModel;

public class NLevelItem implements NLevelListItem {

	private TreeDataModel treeDataModel;
	private NLevelItem parent;
	private NLevelView nLevelView;
	private boolean isExpanded = false;

	public NLevelItem(TreeDataModel treeDataModel, NLevelItem parent, NLevelView nLevelView) {
		this.treeDataModel = treeDataModel;
		this.parent = parent;
		this.nLevelView = nLevelView;
	}

	public TreeDataModel getTreeDataModel() {
		return treeDataModel;
	}

	@Override
	public boolean isExpanded() {
		return isExpanded;
	}

	@Override
	public NLevelListItem getParent() {
		return parent;
	}

	@Override
	public View getView() {
		return nLevelView.getView(this);
	}

	@Override
	public void toggle() {
		isExpanded = !isExpanded;
	}

	@Override
	public void collapseNode() {
		if(isExpanded()) {
			toggle();
		}
	}
}
