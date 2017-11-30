package se.sugarest.jane.viaplaysections.ui.detail;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Display a single section's information.
 * <p>
 * Created by jane on 17-11-28.
 */
public class DetailFragment extends LifecycleFragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

//    private String mSectionName;

    private TextView title;
    private TextView description;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private ArrayList<String> sectionNamesList;

//    public void setSectionName(String mSectionName) {
//        this.mSectionName = mSectionName;
//    }

    public void setSectionNamesList(ArrayList<String> sectionNamesList) {
        this.sectionNamesList = sectionNamesList;
    }

    /**
     * This field is used for data binding.
     */
//    private FragmentSectionContentTextViewBinding mBinding;
    private DetailFragmentViewModel mViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mBinding = DataBindingUtil.inflate(
//                inflater, R.layout.fragment_section_detail, container, false);
//        View rootView = mBinding.getRoot();

        View rootView = inflater.inflate(R.layout.fragment_section_detail, container, false);

//        mBinding.sectionTitleLabelTextView.setText(R.string.section_title_label);
//        mBinding.sectionDescriptionLabelTextView.setText(R.string.section_description_label);

        title = rootView.findViewById(R.id.section_title_content_text_view);
        description = rootView.findViewById(R.id.section_description_content_text_view);
        progressBar = rootView.findViewById(R.id.progress_bar);
        linearLayout = rootView.findViewById(R.id.linearlayout_detail_fragment);


        if (sectionNamesList != null && sectionNamesList.size() > 0) {
            for (int i = 0; i < sectionNamesList.size(); i++) {

                String currentSectionName = sectionNamesList.get(i);

                // Get the ViewModel from the factory
                DetailViewModelFactory factory = InjectorUtils.provideDetailFragmentModelFactory(this.getActivity()
                        .getApplicationContext(), currentSectionName);

                mViewModel = ViewModelProviders.of(this, factory).get(DetailFragmentViewModel.class);

                factory.getRepository().getSectionByName(currentSectionName);
                factory.getRepository().getAndSaveSingleSectionEntryDetails();

                // Observers changes in the SectionEntry with the name
                mViewModel.getSection().observe(this, sectionEntry -> {

                    // If the section details change, update the UI
                    if(sectionEntry == null){
                        showProgressBar();
                    } else if(currentSectionName.equals(sectionNamesList.get(0))) {
                        bindSectionToUI(sectionEntry);
                    }
                });
            }
        }


        return rootView;
    }

    private void bindSectionToUI(SectionEntry sectionEntry) {
//        mBinding.sectionTitleContentTextView.setText(sectionEntry.getTitle());
//        mBinding.sectionDescriptionContentTextView.setText(sectionEntry.getDescription());
        if (sectionEntry.getTitle().equals(sectionEntry.getName())) {
            // showProgressBar();
        } else {
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

}
