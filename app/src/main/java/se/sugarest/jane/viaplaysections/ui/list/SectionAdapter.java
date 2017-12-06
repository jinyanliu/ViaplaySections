package se.sugarest.jane.viaplaysections.ui.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.databinding.DrawerListItemBinding;

/**
 * This class gets a list of section titles from inputs, then populates to the recycler view, which
 * in fact acts as left side drawer navigation view.
 * <p>
 * That means the controller in the MVC pattern.
 * <p>
 * Created by jane on 17-11-14.
 */
public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionAdapterViewHolder> {
    // An On-click handler to make it easy for ListFragment to interact with the RecyclerView
    private final SectionAdapterOnClickHandler mClickHandler;
    private ArrayList<String> mSectionTitleString = new ArrayList<>();

    /**
     * Creates a SectionAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public SectionAdapter(SectionAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If RecyclerView has more than one type of item (which this one don't)
     *                 this viewType can be used to provide a different layout.
     * @return A new SectionAdapterViewHolder that holds the View for each list item
     */
    @Override
    public SectionAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        DrawerListItemBinding drawerListItemBinding
                = DrawerListItemBinding.inflate(inflater, parent, false);

        return new SectionAdapterViewHolder(drawerListItemBinding);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, update the contents of the ViewHolder to display the movie
     * posters for each particular position, using the "position" argument that is conveniently
     * passed in.
     *
     * @param holder   The ViewHolder which should be updated to represent the
     *                 contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(SectionAdapterViewHolder holder, int position) {
        String sectionName = mSectionTitleString.get(position);
        holder.bind(sectionName);
    }

    /**
     * @return The number of items to display.
     */
    @Override
    public int getItemCount() {
        if (null != mSectionTitleString) {
            return mSectionTitleString.size();
        }
        return 0;
    }

    /**
     * This method is used to set the sections' titles string list data on a SectionAdapter
     */
    public void setUpTitleStringArray(ArrayList<String> newArray) {
        if (newArray != null) {
            mSectionTitleString.clear();
            mSectionTitleString.addAll(newArray);
            notifyDataSetChanged();
        }
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface SectionAdapterOnClickHandler {
        void onClick(String title);
    }

    /**
     * Cache of the children views for a section's title.
     */
    public class SectionAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final DrawerListItemBinding mBinding;

        public SectionAdapterViewHolder(DrawerListItemBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.drawerListItem.setOnClickListener(this);
        }

        public void bind(String sectionName) {
            mBinding.drawerListItem.setText(sectionName);
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String currentTitle = mSectionTitleString.get(adapterPosition);
            mClickHandler.onClick(currentTitle);
        }
    }
}
