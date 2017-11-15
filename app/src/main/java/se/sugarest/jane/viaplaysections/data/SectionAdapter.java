package se.sugarest.jane.viaplaysections.data;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.data.database.SectionContract.SectionEntry;

/**
 * Created by jane on 17-11-14.
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionAdapterViewHolder> {

    private final SectionAdapterOnClickHandler mClickHandler;

    private Cursor mCursor;

    public SectionAdapter(SectionAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    @Override
    public SectionAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.drawer_list_item, parent, false);
        return new SectionAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionAdapterViewHolder holder, int position) {
        if (null != mCursor) {
            mCursor.moveToPosition(position);
            String currentTitle = mCursor.getString(mCursor.getColumnIndex(SectionEntry.COLUMN_SECTION_TITLE));
            holder.mTitleTextView.setText(currentTitle);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    public interface SectionAdapterOnClickHandler {
        void onClick(int position);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a section title.
     */
    public class SectionAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTitleTextView;

        public SectionAdapterViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.drawer_list_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
