package se.sugarest.jane.viaplaysections.ui.detail;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;
import se.sugarest.jane.viaplaysections.utilities.InjectorUtils;

/**
 * Display a single section's information on main screen.
 * <p>
 * Created by jane on 17-11-28.
 */
public class DetailFragment extends LifecycleFragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private TextView title;
    private TextView description;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private ArrayList<String> mSectionNamesList = new ArrayList<>();
    private String mCurrentSectionName;


    // This field is used for data binding.
    // private FragmentSectionContentTextViewBinding mBinding;

    private DetailFragmentViewModel mViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public DetailFragment() {
    }

    /**
     * Setter method for keeping track of the section names list this fragment can interact with.
     */
    public void setmSectionNamesList(ArrayList<String> mSectionNamesList) {
        if (mSectionNamesList != null) {
            this.mSectionNamesList.clear();
            this.mSectionNamesList.addAll(mSectionNamesList);
            this.mCurrentSectionName = mSectionNamesList.get(0);
            Log.i(LOG_TAG, "CONFIGURATION setter: mSectionNamesList == " + mSectionNamesList.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mBinding = DataBindingUtil.inflate(
//                inflater, R.layout.fragment_section_detail, container, false);
//        View rootView = mBinding.getRoot();

        View rootView = inflater.inflate(R.layout.fragment_section_detail, container, false);

        title = rootView.findViewById(R.id.section_title_content_text_view);
        description = rootView.findViewById(R.id.section_description_content_text_view);
        progressBar = rootView.findViewById(R.id.progress_bar);
        linearLayout = rootView.findViewById(R.id.linearlayout_detail_fragment);

//        // Load the saved state(the list of section names) if there is one
//        if (savedInstanceState != null) {
//            mCurrentSectionName = savedInstanceState.getString(DETAIL_FRAGMENT_CURRENT_SECTION_NAME);
//            Log.i(LOG_TAG, "CONFIGURATION savedInstanceState : mCurrentSectionName == " + mCurrentSectionName);
//        }

        if (mSectionNamesList != null && mSectionNamesList.size() > 0) {
            // Loop through the whole list, to get and save all the section's information after the
            // first installation with internet, so user is able to see all the information while offline
            for (int i = 0; i < mSectionNamesList.size(); i++) {

                String sectionName = mSectionNamesList.get(i);

                // Get the ViewModel from the factory
                DetailViewModelFactory factory = InjectorUtils.provideDetailFragmentModelFactory(this.getActivity()
                        .getApplicationContext(), sectionName);

                mViewModel = ViewModelProviders.of(this, factory).get(DetailFragmentViewModel.class);

                factory.getRepository().getSectionByName(sectionName);
                factory.getRepository().getAndSaveSingleSectionEntryDetails();

                // Observers changes in the SectionEntry with the name
                mViewModel.getSection().observe(this, sectionEntry -> {

                    // If the section details change, update the UI
                    if (sectionEntry == null) {
                        showProgressBar();
                    } else if (sectionName.equals(mCurrentSectionName)) {
                        bindSectionToUI(sectionEntry);
                    }
                });
            }
        } else {
            Log.e(LOG_TAG, "This fragment has a null list of secion names.");
        }
        return rootView;
    }

    private void bindSectionToUI(SectionEntry sectionEntry) {
        //        mBinding.sectionTitleLabelTextView.setText(R.string.section_title_label);
//        mBinding.sectionDescriptionLabelTextView.setText(R.string.section_description_label);
//        mBinding.sectionTitleContentTextView.setText(sectionEntry.getTitle());
//        mBinding.sectionDescriptionContentTextView.setText(sectionEntry.getDescription());
        if (sectionEntry.getName() != sectionEntry.getTitle()) {
            showLinearLayout();
            title.setText(sectionEntry.getTitle());
            description.setText(sectionEntry.getDescription());
        }
    }

    private void showLinearLayout() {
        progressBar.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);

    }

//    /**
//     * Save the current state of this fragment
//     */
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putString(DETAIL_FRAGMENT_CURRENT_SECTION_NAME, mCurrentSectionName);
//        Log.i(LOG_TAG, "CONFIGURATION outState: mCurrentSectionName == " + mCurrentSectionName);
//    }
}
