package se.sugarest.jane.viaplaysections.ui.list;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.utilities.InjectorUtils;

import static se.sugarest.jane.viaplaysections.utilities.Constants.LIST_FRAGMENT_SECTION_NAME_LIST;

/**
 * Displays a list of section names.
 * Created by jane on 17-11-30.
 */
public class ListFragment extends LifecycleFragment implements SectionAdapter.SectionAdapterOnClickHandler {

    private static final String LOG_TAG = ListFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;

    private SectionAdapter mSectionAdapter;

    private ListFragmentViewModel mViewModel;

    private int mPosition = RecyclerView.NO_POSITION;

    private ArrayList<String> mCurrentSectionNameList = new ArrayList<>();

    private boolean mInitialized = false;

    // Define a new interface OnDataBackListener that triggers a callback in the host activity
    OnDataBackListener mDataCallback;

    // OnDataBackListener interface, calls a method in the host activity named onDataBack
    public interface OnDataBackListener {
        void onDataBack(ArrayList<String> sectionNamesList);
    }

    // Override onAttach to make sure that the container activity has implemented the callbacks
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callbacks interface
        // If not, it throws an exception
        try {
            // mSectionNameCallback = (OnCurrentSectionSelectedListener) context;
            mDataCallback = (OnDataBackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCurrentSectionSelectedListener");
        }
    }

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

        factory.getRepository().getAndSaveSectionEntryList();

        mViewModel.getSections().observe(this, sectionEntries -> {

            if (sectionEntries != null && sectionEntries.size() != 0) {

                ArrayList<String> sectionNamesList = new ArrayList<>();

                for (int i = 0; i < sectionEntries.size(); i++) {
                    String currentSectionName = sectionEntries.get(i).getName().toLowerCase();
                    sectionNamesList.add(currentSectionName);
                }

                if (!mInitialized) {
                    mInitialized = true;
                    if (savedInstanceState != null) {
                        mDataCallback.onDataBack(savedInstanceState.getStringArrayList(LIST_FRAGMENT_SECTION_NAME_LIST));
                        mCurrentSectionNameList.clear();
                        mCurrentSectionNameList.addAll(savedInstanceState.getStringArrayList(LIST_FRAGMENT_SECTION_NAME_LIST));
                    } else {
                        mCurrentSectionNameList.clear();
                        mCurrentSectionNameList.addAll(sectionNamesList);
                        mDataCallback.onDataBack(sectionNamesList);
                    }
                }

                mSectionAdapter.setUpTitleStringArray(sectionNamesList);
                if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
                mRecyclerView.smoothScrollToPosition(mPosition);
            } else {
                mDataCallback.onDataBack(mCurrentSectionNameList);
            }
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
        ArrayList<String> sectionNamesList = new ArrayList<>();
        sectionNamesList.add(title);

        mCurrentSectionNameList.clear();
        mCurrentSectionNameList.addAll(sectionNamesList);

        //mSectionNameCallback.onCurrentSectionSelected(title.toLowerCase());
        mDataCallback.onDataBack(sectionNamesList);
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(LIST_FRAGMENT_SECTION_NAME_LIST, mCurrentSectionNameList);
        Log.i(LOG_TAG, "CONFIGURATION outState: mCurrentSectionNameList == " + mCurrentSectionNameList);
    }
}
