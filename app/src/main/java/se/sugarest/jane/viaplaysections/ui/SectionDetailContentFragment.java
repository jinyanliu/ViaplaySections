package se.sugarest.jane.viaplaysections.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.databinding.FragmentSectionContentTextViewBinding;

/**
 * This class is a Fragment class re-usable for 4 TextViews on main screen: title label text,
 * title content text, description label text and description content text.
 * <p>
 * Created by jane on 17-11-28.
 */
public class SectionDetailContentFragment extends Fragment {

    private static final String LOG_TAG = SectionDetailContentFragment.class.getSimpleName();

    private String mTitleContentText;
    private String mDescriptionContentText;

    private FragmentSectionContentTextViewBinding mBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public SectionDetailContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_section_content_text_view, container, false);

        View rootView = mBinding.getRoot();

        mBinding.sectionTitleLabelTextView.setText(R.string.section_title_label);
        mBinding.sectionDescriptionLabelTextView.setText(R.string.section_description_label);

        if (mTitleContentText != null && !mTitleContentText.isEmpty()
                && mDescriptionContentText != null && !mDescriptionContentText.isEmpty()) {
            mBinding.sectionTitleContentTextView.setText(mTitleContentText);
            mBinding.sectionDescriptionContentTextView.setText(mDescriptionContentText);
        } else {
            Log.e(LOG_TAG, "This detail fragment doesn't have title and description content to show.");
        }

        return rootView;
    }

    public void setContentText(String titleContent, String descriptionContent) {
        this.mTitleContentText = titleContent;
        this.mDescriptionContentText = descriptionContent;
    }
}
