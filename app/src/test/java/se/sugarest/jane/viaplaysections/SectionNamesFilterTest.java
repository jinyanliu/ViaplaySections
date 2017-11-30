//package se.sugarest.jane.viaplaysections;
//
//import junit.framework.Assert;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.ArrayList;
//import java.util.stream.Stream;
//
//import se.sugarest.jane.viaplaysections.ui.list.MainActivity;
//
///**
// * Created by jane on 17-11-27.
// */
//@RunWith(MockitoJUnitRunner.class)
//public class SectionNamesFilterTest {
//
//    private ArrayList<String> originalSectionNamesStringList = new ArrayList<>();
//
//    @InjectMocks
//    MainActivity mMainActivity;
//
//    @Test
//    public void sectionNamesFilter_OnlyAddDifferentSectionNamesToStringArray() throws Exception {
//
//        Stream.of("Serier", "Film", "Sport", "Sport", "Sport", "Barn", "Store")
//                .forEach(sectionName -> originalSectionNamesStringList.add(sectionName));
//
//        for (int i = 0; i < originalSectionNamesStringList.size(); i++) {
//            mMainActivity.filterOutDifferentSectionNames(originalSectionNamesStringList.get(i));
//        }
//
//        Assert.assertEquals(5, mMainActivity.getmSectionTitlesString().size());
//
//    }
//}
