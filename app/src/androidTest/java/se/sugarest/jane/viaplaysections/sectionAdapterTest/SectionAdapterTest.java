package se.sugarest.jane.viaplaysections.sectionAdapterTest;

import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.ui.list.SectionAdapter;

/**
 * Created by jane on 17-11-16.
 */
@RunWith(AndroidJUnit4.class)
public class SectionAdapterTest {

    private SectionAdapter mSectionAdapter;
    private ArrayList<String> mSectionTitlesStrings = new ArrayList<>();

    @Before
    public void setUp() {
        mSectionAdapter = new SectionAdapter(null);
    }

    @Test
    public void getItemCountTest_oneSection() throws Exception {
        String oneSection = "Serier";
        mSectionTitlesStrings.add(oneSection);
        mSectionAdapter.setUpTitleStringArray(mSectionTitlesStrings);
        Assert.assertEquals(1, mSectionAdapter.getItemCount());
    }

    @Test
    public void getItemCountTest_twoSection() throws Exception {
        String firstSection = "Serier";
        String secondSection = "Film";
        mSectionTitlesStrings.add(firstSection);
        mSectionTitlesStrings.add(secondSection);
        mSectionAdapter.setUpTitleStringArray(mSectionTitlesStrings);
        Assert.assertEquals(2, mSectionAdapter.getItemCount());
    }
}
