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
 * Created by jane on 17-11-28.
 */

public class SectionContentFragment extends Fragment {

    private static final String LOG_TAG = SectionContentFragment.class.getSimpleName();

    private String mContentText;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public SectionContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_content_text_view, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_content_text_view);

        if (mContentText != null && !mContentText.isEmpty()) {
            textView.setText(mContentText);
        } else {
            Log.e(LOG_TAG, "This fragment doesn't have a text to show.");
        }

        return rootView;
    }

    public void setmContentText(String mContentText) {
        this.mContentText = mContentText;
    }
}
