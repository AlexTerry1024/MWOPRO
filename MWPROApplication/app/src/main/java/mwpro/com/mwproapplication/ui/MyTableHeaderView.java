package mwpro.com.mwproapplication.ui;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;

import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.listeners.TableHeaderClickListener;


class MyTableHeaderView extends ListView {

    private final Set<TableHeaderClickListener> listeners = new HashSet<>();
    private MyTableHeaderAdapter adapter;

    /**
     * Creates a new TableHeaderView.
     *
     * @param context The context that shall be used.
     */
    public MyTableHeaderView(final Context context) {
        super(context);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * Sets the {@link TableHeaderAdapter} that is used to render the header views of every single column.
     *
     * @param adapter The {@link TableHeaderAdapter} that should be set.
     */
    public void setAdapter(MyTableHeaderAdapter adapter) {
        this.adapter = adapter;
        super.setAdapter(adapter);
    }

    @Override
    public MyTableHeaderAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void invalidate() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.invalidate();
    }

    protected Set<TableHeaderClickListener> getHeaderClickListeners() {
        return listeners;
    }

    /**
     * Adds the given {@link TableHeaderClickListener} to this SortableTableHeaderView.
     *
     * @param listener The {@link TableHeaderClickListener} that shall be added.
     */
    public void addHeaderClickListener(final TableHeaderClickListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the given {@link TableHeaderClickListener} from this SortableTableHeaderView.
     *
     * @param listener The {@link TableHeaderClickListener} that shall be removed.
     */
    public void removeHeaderClickListener(final TableHeaderClickListener listener) {
        listeners.remove(listener);
    }

}
