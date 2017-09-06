package mwpro.com.mwproapplication.ui.uitableview.adapter;

import android.content.Context;
import mwpro.com.mwproapplication.ui.uitableview.model.IndexPath;
import mwpro.com.mwproapplication.ui.uitableview.model.UITableCellItem;
import mwpro.com.mwproapplication.ui.uitableview.model.UITableHeaderItem;
import mwpro.com.mwproapplication.ui.uitableview.view.UITableCellView;
import mwpro.com.mwproapplication.ui.uitableview.view.UITableHeaderView;

public abstract class UITableViewAdapter {

	public int numberOfGroups() {
		return 1;
	}

	public int numberOfRows(int group) {
		return 0;
	}

	public abstract UITableHeaderItem headerItemForGroup(Context context, IndexPath indexPath);

	public abstract UITableCellItem cellItemForRow(Context context, IndexPath indexPath);

	public abstract UITableHeaderView headerViewForGroup(Context context, IndexPath indexPath, UITableHeaderItem cellItem, UITableHeaderView convertView);

	public abstract UITableCellView cellViewForRow(Context context, IndexPath indexPath, UITableCellItem cellItem, UITableCellView convertView);

}
