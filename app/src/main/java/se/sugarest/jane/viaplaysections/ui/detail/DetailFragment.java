package se.sugarest.jane.viaplaysections.ui.detail;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.data.database.SectionEntry;
import se.sugarest.jane.viaplaysections.utilities.InjectorUtils;

/**
 * This detail fragment displays a single section's detail information on main screen.
 * <p>
 * Created by jane on 17-11-28.
 */
public class DetailFragment extends LifecycleFragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private TextView title;
    private TextView description;
    private ImageView imageView;
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

    // Define a new interface OnDetailDataBackListener that triggers a callback in the host activity
    private OnDetailDataBackListener mDataCallback;

    // OnDetailDataBackListener interface, calls a method in the host activity named onDataBack
    public interface OnDetailDataBackListener {
        void onDetailDataBack();
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callbacks interface
        // If not, it throws an exception
        try {
            mDataCallback = (OnDetailDataBackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnDetailDataBackListener");
        }
    }

    /**
     * Setter method for keeping track of the section names list this fragment can interact with.
     */
    public void setmSectionNamesList(ArrayList<String> mSectionNamesList) {
        if (mSectionNamesList != null && mSectionNamesList.size() > 0) {
            this.mSectionNamesList.clear();
            this.mSectionNamesList.addAll(mSectionNamesList);
            this.mCurrentSectionName = mSectionNamesList.get(0);
            Log.i(LOG_TAG, "CONFIGURATION setter: mSectionNamesList == " + mSectionNamesList.toString());
        } else {
            Log.e(LOG_TAG, "This fragment has a null list of section names.");
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
        imageView = rootView.findViewById(R.id.iv_empty_message);
        linearLayout = rootView.findViewById(R.id.linearlayout_detail_fragment);

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
                    if (sectionEntry == null && !hasInternet()) {
                        showEmptyView();
                    } else if (sectionName.equals(mCurrentSectionName)) {
                        bindSectionToUI(sectionEntry);
                    }
                });
            }
        } else if (!hasInternet()) {
            Log.e(LOG_TAG, "This fragment has a null list of section names.");
            showEmptyView();
        }
        mDataCallback.onDetailDataBack();
        return rootView;
    }

    private void bindSectionToUI(SectionEntry sectionEntry) {
        //        mBinding.sectionTitleLabelTextView.setText(R.string.section_title_label);
//        mBinding.sectionDescriptionLabelTextView.setText(R.string.section_description_label);
//        mBinding.sectionTitleContentTextView.setText(sectionEntry.getTitle());
//        mBinding.sectionDescriptionContentTextView.setText(sectionEntry.getDescription());

        if (!(sectionEntry.getName()).equals(sectionEntry.getTitle())) {
            showLinearLayout();
            title.setText(sectionEntry.getTitle());
            description.setText(sectionEntry.getDescription());
        } else if (!hasInternet()) {
            showEmptyView();
        }
    }

    private void showLinearLayout() {
        imageView.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    private void showEmptyView() {
        imageView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);
    }

    private boolean hasInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();
    }
}
