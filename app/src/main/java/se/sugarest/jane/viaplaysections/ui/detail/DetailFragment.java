package se.sugarest.jane.viaplaysections.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;
import se.sugarest.jane.viaplaysections.databinding.FragmentSectionDetailBinding;
import se.sugarest.jane.viaplaysections.utilities.InjectorUtils;

/**
 * This fragment class displays a single section's detail information on main screen.
 * <p>
 * Created by jane on 17-11-28.
 */
public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private ArrayList<String> mSectionNamesList = new ArrayList<>();
    private String mCurrentSectionName;
    private DetailFragmentViewModel mViewModel;
    private FragmentSectionDetailBinding mBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public DetailFragment() {
    }

    // Defines a new interface that triggers a callback in the host activity (MainActivity)
    private OnDetailDataBackListener mDataCallback;

    /**
     * Calls a method in the host activity (MainActivity) named onDetailDataBack
     */
    public interface OnDetailDataBackListener {
        void onDetailDataBack();
    }

    /**
     * Override onAttach to make sure that the host activity (MainActivity) has implemented the callback.
     * If not, it throws an exception.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mDataCallback = (OnDetailDataBackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDetailDataBackListener");
        }
    }

    /**
     * Setter method for keeping track of the sections names list this fragment can interact with.
     */
    public void setmSectionNamesList(ArrayList<String> mSectionNamesList) {
        if (mSectionNamesList != null && !mSectionNamesList.isEmpty()) {
            this.mSectionNamesList.clear();
            this.mSectionNamesList.addAll(mSectionNamesList);
            this.mCurrentSectionName = mSectionNamesList.get(0);
            Log.i(LOG_TAG, "CONFIGURATION setter: mSectionNamesList == " + mSectionNamesList.toString());
        } else {
            Log.w(LOG_TAG, "This fragment has an empty list of section names.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_section_detail, container, false);
        View rootView = mBinding.getRoot();

        if (mSectionNamesList != null && !mSectionNamesList.isEmpty()) {
            // Loop through the whole list, to get and save all the sections details after the
            // first installation with internet, so user is able to see all the information while offline
            for (int i = 0; i < mSectionNamesList.size(); i++) {

                String sectionName = mSectionNamesList.get(i);

                // Get the ViewModel from the factory
                DetailViewModelFactory factory = InjectorUtils.provideDetailFragmentModelFactory(this.getActivity()
                        .getApplicationContext(), sectionName);

                mViewModel = ViewModelProviders.of(this, factory).get(DetailFragmentViewModel.class);

                factory.getRepository().startFetchSectionByName(sectionName);

                // Observers changes in the SectionEntry
                mViewModel.getSection().observe(this, sectionEntry -> {

                    // If the sections details change, only update the UI associated with mCurrentSectionName.
                    if (sectionEntry == null && !hasInternet()) {
                        showEmptyView();
                    } else if (sectionName.equals(mCurrentSectionName)) {
                        bindSectionToUI(sectionEntry);
                    }
                });
            }
        } else if (!hasInternet()) {
            Log.w(LOG_TAG, "This fragment has a empty list of section names.");
            showEmptyView();
        }
        // Tells host activity (MainActivity) to switch to idle mode
        mDataCallback.onDetailDataBack();
        return rootView;
    }

    private void bindSectionToUI(SectionEntry sectionEntry) {

        if (!sectionEntry.getTitle().isEmpty()) {
            showLinearLayout();
            mBinding.sectionTitleContentTextView.setText(sectionEntry.getTitle());
            mBinding.sectionDescriptionContentTextView.setText(sectionEntry.getDescription());
        } else if (!hasInternet()) {
            showEmptyView();
        }
    }

    private void showLinearLayout() {
        mBinding.ivEmptyMessage.setVisibility(View.INVISIBLE);
        mBinding.linearlayoutDetailFragment.setVisibility(View.VISIBLE);
    }

    private void showEmptyView() {
        mBinding.ivEmptyMessage.setVisibility(View.VISIBLE);
        mBinding.linearlayoutDetailFragment.setVisibility(View.INVISIBLE);
    }

    private boolean hasInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();
    }
}
