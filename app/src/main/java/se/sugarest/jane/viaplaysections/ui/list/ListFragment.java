package se.sugarest.jane.viaplaysections.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.databinding.FragmentNavigationRecyclerViewBinding;
import se.sugarest.jane.viaplaysections.utilities.InjectorUtils;

import static se.sugarest.jane.viaplaysections.utilities.Constants.LIST_FRAGMENT_SECTION_NAME_LIST;

/**
 * This fragment class displays a list of section names.
 * <p>
 * Created by jane on 17-11-30.
 */
public class ListFragment extends Fragment implements SectionAdapter.SectionAdapterOnClickHandler {

    private static final String LOG_TAG = ListFragment.class.getSimpleName();

    private SectionAdapter mSectionAdapter;
    private ListFragmentViewModel mViewModel;
    private ArrayList<String> mCurrentSectionNameList = new ArrayList<>();
    private boolean mInitialized = false;

    FragmentNavigationRecyclerViewBinding mBinding;

    // Defines a new interface that triggers a callback in the host activity (MainActivity)
    private OnDataBackListener mDataCallback;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public ListFragment() {
    }

    /**
     * Calls a method in the host activity (MainActivity) named onDataBack
     */
    public interface OnDataBackListener {
        void onDataBack(ArrayList<String> sectionNamesList);
    }

    /**
     * Override onAttach to make sure that the host activity (MainActivity) has implemented the callback.
     * If not, it throws an exception
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mDataCallback = (OnDataBackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDataBackListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_navigation_recycler_view, container, false);
        View rootView = mBinding.getRoot();

        setUpRecyclerViewWithAdapter();

        // Get the ViewModel from the factory
        ListViewModelFactory factory = InjectorUtils.provideListFragmentViewModelFactory(getActivity()
                .getApplicationContext());

        mViewModel = ViewModelProviders.of(this, factory).get(ListFragmentViewModel.class);

        // Observers changes in the SectionEntry
        mViewModel.getSections().observe(this, sectionEntries -> {

            if (sectionEntries != null && !sectionEntries.isEmpty()) {

                ArrayList<String> sectionNamesList = new ArrayList<>();

                for (int i = 0; i < sectionEntries.size(); i++) {
                    String currentSectionName = sectionEntries.get(i).getName().toLowerCase();
                    sectionNamesList.add(currentSectionName);
                }

                if (!mInitialized) {
                    mInitialized = true;
                    mCurrentSectionNameList.clear();
                    if (savedInstanceState != null) {
                        mCurrentSectionNameList.addAll(savedInstanceState.getStringArrayList(LIST_FRAGMENT_SECTION_NAME_LIST));
                    } else {
                        mCurrentSectionNameList.addAll(sectionNamesList);
                    }
                    mDataCallback.onDataBack(mCurrentSectionNameList);
                }

                mSectionAdapter.setUpTitleStringArray(sectionNamesList);

            } else {

                mDataCallback.onDataBack(mCurrentSectionNameList);

            }
        });

        return rootView;
    }

    private void setUpRecyclerViewWithAdapter() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mBinding.navigationDrawerRecyclerView.setLayoutManager(layoutManager);
        mBinding.navigationDrawerRecyclerView.setHasFixedSize(true);
        if (mSectionAdapter == null) {
            mSectionAdapter = new SectionAdapter(this);
        }
        mBinding.navigationDrawerRecyclerView.setAdapter(mSectionAdapter);
    }

    @Override
    public void onClick(String title) {
        ArrayList<String> sectionNamesList = new ArrayList<>();
        sectionNamesList.add(title);

        mCurrentSectionNameList.clear();
        mCurrentSectionNameList.addAll(sectionNamesList);

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
