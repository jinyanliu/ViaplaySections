package se.sugarest.jane.viaplaysections.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import se.sugarest.jane.viaplaysections.R;

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

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public SectionTextViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_content_text_view, container, false);
        TextView textView = rootView.findViewById(R.id.section_content_text_view);

        if (mContentText != null && !mContentText.isEmpty()) {
            textView.setText(mContentText);
        } else {
            Log.e(LOG_TAG, "This fragment doesn't have a text to show.");
        }

        if (mTextSize != 0.0f) {
            textView.setTextSize(mTextSize);
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
