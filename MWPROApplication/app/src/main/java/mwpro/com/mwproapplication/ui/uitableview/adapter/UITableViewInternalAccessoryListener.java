package mwpro.com.mwproapplication.ui.uitableview.adapter;

import mwpro.com.mwproapplication.ui.uitableview.model.IndexPath;

/**
 * This listener is used to notify the {@link UITableViewAdapterInternal} that user clicked on an accessory and then notify the application
 * 
 * @author dvilleneuve
 * 
 */
public interface UITableViewInternalAccessoryListener {

	void onCellAccessoryClick(IndexPath indexPath);
	
	boolean onCellAccessoryLongClick(IndexPath indexPath);

}
