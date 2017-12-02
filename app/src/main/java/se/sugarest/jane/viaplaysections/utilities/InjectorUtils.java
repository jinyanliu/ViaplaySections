package se.sugarest.jane.viaplaysections.utilities;

import android.content.Context;

import se.sugarest.jane.viaplaysections.data.repository.SectionRepository;
import se.sugarest.jane.viaplaysections.data.database.SectionDatabase;
import se.sugarest.jane.viaplaysections.data.network.SectionNetworkDataSource;
import se.sugarest.jane.viaplaysections.ui.detail.DetailViewModelFactory;
import se.sugarest.jane.viaplaysections.ui.list.ListViewModelFactory;

/**
 * This class provides static methods to inject the various classes needed for the app
 * <p>
 * Created by jane on 17-11-30.
 */
public class InjectorUtils {

    public static SectionRepository provideRepository(Context context) {
        SectionDatabase database = SectionDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        SectionNetworkDataSource networkDataSource =
                SectionNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return SectionRepository.getInstance(database.sectionDao(), networkDataSource, executors);
    }

    public static DetailViewModelFactory provideDetailFragmentModelFactory(Context context, String sectionName) {
        SectionRepository repository = provideRepository(context.getApplicationContext());
        return new DetailViewModelFactory(repository, sectionName);
    }

    public static ListViewModelFactory provideListFragmentViewModelFactory(Context context) {
        SectionRepository repository = provideRepository(context.getApplicationContext());
        return new ListViewModelFactory(repository);
    }
}
