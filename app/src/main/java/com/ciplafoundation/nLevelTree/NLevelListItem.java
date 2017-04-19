package com.ciplafoundation.nLevelTree;

import android.view.View;

public interface NLevelListItem {

	public boolean isExpanded();

	public void toggle();
	
	public void collapseNode();

	public NLevelListItem getParent();

	public View getView();
}