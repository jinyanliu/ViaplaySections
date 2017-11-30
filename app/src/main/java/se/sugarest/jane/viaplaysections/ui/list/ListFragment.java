package se.sugarest.jane.viaplaysections.ui.list;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.ui.detail.DetailFragment;
import se.sugarest.jane.viaplaysections.utilities.InjectorUtils;

/**
 * Displays a list of section names.
 * Created by jane on 17-11-30.
 */
public class ListFragment extends LifecycleFragment implements SectionAdapter.SectionAdapterOnClickHandler {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private SectionAdapter mSectionAdapter;

    private ListFragmentViewModel mViewModel;

    private int mPosition = RecyclerView.NO_POSITION;

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


        ListViewModelFactory factory = InjectorUtils.provideListFragmentViewModelFactory(getActivity().getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(ListFragmentViewModel.class);

        mViewModel.getSections().observe(this, sectionEntries -> {
            ArrayList<String> sectionNamesList = new ArrayList<>();

            for (int i = 0; i < sectionEntries.size(); i++) {
                String currentSectionName = sectionEntries.get(i).getName();
                sectionNamesList.add(currentSectionName);
            }
            mSectionAdapter.setUpTitleStringArray(sectionNamesList);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
        });

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
