package se.sugarest.jane.viaplaysections.data;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.data.database.SectionContract.SectionEntry;

/**
 * Created by jane on 17-11-14.
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionAdapterViewHolder> {

    private static final String LOG_TAG = SectionAdapter.class.getSimpleName();

    private final SectionAdapterOnClickHandler mClickHandler;
    private ArrayList<String> mSectionTitleString = new ArrayList<>();

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
            holder.mTitleTextView.setText(mSectionTitleString.get(position));
    }

    @Override
    public int getItemCount() {
        if (null != mSectionTitleString) {
            return mSectionTitleString.size();
        }
        return 0;
    }

    public interface SectionAdapterOnClickHandler {
        void onClick(String title);
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        if (null != mCursor) {
            for (int i = 0; i < mCursor.getCount(); i++) {
                mCursor.moveToPosition(i);
                String currentTitle = mCursor.getString(mCursor.getColumnIndex(SectionEntry.COLUMN_SECTION_TITLE));
                if (!mSectionTitleString.contains(currentTitle)) {
                    mSectionTitleString.add(currentTitle);
                }
                Log.i(LOG_TAG, "There are " + mSectionTitleString.size() + " different section titles available.");
            }
        }
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
            String currentTitle = mSectionTitleString.get(adapterPosition);
            mClickHandler.onClick(currentTitle);
        }
    }
}
