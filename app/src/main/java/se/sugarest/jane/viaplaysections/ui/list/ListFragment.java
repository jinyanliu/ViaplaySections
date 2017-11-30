package se.sugarest.jane.viaplaysections.ui.list;

import android.arch.lifecycle.LifecycleFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.ui.detail.DetailFragment;

/**
 * Displays a list of section names.
 * Created by jane on 17-11-30.
 */
public class ListFragment extends LifecycleFragment implements SectionAdapter.SectionAdapterOnClickHandler {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private SectionAdapter mSectionAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_navigation_recycler_view, container, false);

        mRecyclerView = rootView.findViewById(R.id.navigation_drawer_recycler_view);

        setUpRecyclerViewWithAdapter();

        return rootView;
    }

    private void setUpRecyclerViewWithAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        if (mSectionAdapter == null) {
            mSectionAdapter = new SectionAdapter(this);
        }
        mRecyclerView.setAdapter(mSectionAdapter);
    }

    @Override
    public void onClick(String title) {

    }
}
