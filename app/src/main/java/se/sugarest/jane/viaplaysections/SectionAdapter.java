package se.sugarest.jane.viaplaysections;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jane on 17-11-14.
 */

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionAdapterViewHolder> {

    private final SectionAdapterOnClickHandler mClickHandler;

    private ArrayList<String> mSectionTitles = new ArrayList<>();

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
        String currentTitle = mSectionTitles.get(position);
        holder.mTitleTextView.setText(currentTitle);
    }

    @Override
    public int getItemCount() {
        return mSectionTitles.size();
    }

    public interface SectionAdapterOnClickHandler {
        void onClick(int position);
    }

    public void setSectionData(ArrayList<String> sectionTitles) {
        mSectionTitles.clear();
        mSectionTitles.addAll(sectionTitles);
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

        }
    }
}
