package se.sugarest.jane.viaplaysections;

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

    private SectionAdapter sectionAdapter;
    private ArrayList<String> sectionTitlesStrings = new ArrayList<>();

    @Before
    public void setUp() {
        sectionAdapter = new SectionAdapter(null);
    }

    @Test
    public void getItemCountTest_oneSection() throws Exception {
        String oneSection = "Serier";
        sectionTitlesStrings.add(oneSection);
        sectionAdapter.setUpTitleStringArray(sectionTitlesStrings);
        Assert.assertEquals(1, sectionAdapter.getItemCount());
    }

    @Test
    public void getItemCountTest_twoSection() throws Exception {
        String firstSection = "Serier";
        String secondSection = "Film";
        sectionTitlesStrings.add(firstSection);
        sectionTitlesStrings.add(secondSection);
        sectionAdapter.setUpTitleStringArray(sectionTitlesStrings);
        Assert.assertEquals(2, sectionAdapter.getItemCount());
    }
}
