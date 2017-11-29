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
public class SectionTextViewFragment extends Fragment {

    private static final String LOG_TAG = SectionTextViewFragment.class.getSimpleName();

    private String mContentText;
    private float mTextSize;

    private FragmentSectionContentTextViewBinding mBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public SectionTextViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_section_content_text_view, container, false);

        View rootView = mBinding.getRoot();

        if (mContentText != null && !mContentText.isEmpty()) {
            mBinding.sectionContentTextView.setText(mContentText);
        } else {
            Log.e(LOG_TAG, "This fragment doesn't have a text to show.");
        }

        if (mTextSize != 0.0f) {
            mBinding.sectionContentTextView.setTextSize(mTextSize);
        }

        return rootView;
    }

    public void setmContentText(String mContentText) {
        this.mContentText = mContentText;
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }
}
