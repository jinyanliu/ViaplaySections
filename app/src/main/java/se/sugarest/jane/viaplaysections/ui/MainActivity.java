package se.sugarest.jane.viaplaysections.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.idling_resource.SimpleIdlingResource;
import se.sugarest.jane.viaplaysections.ui.detail.DetailFragment;
import se.sugarest.jane.viaplaysections.ui.detail.DetailFragmentViewModel;
import se.sugarest.jane.viaplaysections.ui.list.ListFragment;

/**
 * This is the main controller of the whole app.
 * It initiates the app.
 */
public class MainActivity extends AppCompatActivity implements ListFragment.OnFirstSectionNameGetListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Bundle backgroundState;

    private String mClickedSectionName;
    private String mFirstSectionName;
    private Toast mToast;
    private DetailFragmentViewModel mDetailFragmentViewModel;
    private ImageView menuImage;
    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private Boolean mInitialized = false;

//    private ActivityMainBinding mBinding;

    public ArrayList<String> mSectionTitlesString = new ArrayList<>();

    public ArrayList<String> getmSectionTitlesString() {
        return mSectionTitlesString;
    }

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

//        // Set up ToolBar
//        setSupportActionBar(mBinding.appBar.toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        menuImage = findViewById(R.id.navigation_menu);
        drawerLayout = findViewById(R.id.drawer_layout);

        fragmentManager = getSupportFragmentManager();

        ListFragment navigationDrawerFragment = new ListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.navigation_drawer_container, navigationDrawerFragment)
                .commit();

        // Set up left drawer navigation
        menuImage.setOnClickListener((View view) -> {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.navigation_drawer_container))) {
                drawerLayout.closeDrawer(findViewById(R.id.navigation_drawer_container));
            } else if (!drawerLayout.isDrawerOpen(findViewById(R.id.navigation_drawer_container))) {
                drawerLayout.openDrawer(findViewById(R.id.navigation_drawer_container));
            }
        });


//        // Set up swipe refresh function
//        mBinding.swipeRefresh.setOnRefreshListener(
//                () -> {
//                    if (!hasInternet()) {
//                        mBinding.swipeRefresh.setRefreshing(false);
//                        if (mToast != null) {
//                            mToast.cancel();
//                        }
//                        mToast = Toast.makeText(MainActivity.this, getString(R.string.toast_message_offline_cannot_refresh), Toast.LENGTH_SHORT);
//                        mToast.setGravity(Gravity.BOTTOM, 0, 0);
//                        mToast.show();
//                    } else {
//                        if (getCurrentTitleOnTheAppBarText() == null ||
//                                getCurrentTitleOnTheAppBarText().toString().isEmpty()) {
//                            initialScreenWithInternet();
//                        } else {
//                            mClickedSectionName = getCurrentTitleOnTheAppBarText().toString().toLowerCase();
//                            refreshOneScreenWithInternet();
//                        }
//                    }
//                }
//        );

//        // Current content survive while rotates the phone
//        if (savedInstanceState != null && savedInstanceState.containsKey(CONFIGURATION_KEY)) {
//            mClickedSectionName = savedInstanceState.getString(CONFIGURATION_KEY);
//            populateSectionNameOnTheAppBar(mClickedSectionName);
//            loadEverythingFromDataBase();
//        } else {
//            if (hasInternet()) {
//                initialScreenWithInternet();
//            } else {
//                loadEverythingFromDataBase();
//            }
//        }

        // Get the IdlingResource instance
        getIdlingResource();
    }

    @Override
    public void onFirstSectionNameGet(String firstSectionName) {
        DetailFragment sectionDetailContentFragment = new DetailFragment();
        sectionDetailContentFragment.setSectionName(firstSectionName);
        fragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, sectionDetailContentFragment)
                .commit();
    }

//    private void initialScreenWithInternet() {
//        if (mIdlingResource != null) {
//            mIdlingResource.setIdleState(false);
//        }
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(this, getString(R.string.toast_message_data_is_loading), Toast.LENGTH_SHORT);
//        mToast.setGravity(Gravity.BOTTOM, 0, 0);
//        mToast.show();
//        // Create a ViewModel the first time the system calls an activity's onCreate() method.
//        // Re-created activities receive the same SectionNameViewModel instance created by the first activity.
//        mSectionNameViewModel = ViewModelProviders.of(this).get(SectionNameViewModel.class);
//        mSectionNameViewModel.init();
//        mSectionNameViewModel.getSectionNames().observe(this, sectionNames -> {
//            // Update Navigation Bar items
//            loadNavigationBarItemsFromInternet(sectionNames);
//            putSectionTitleDataIntoDatabase();
////            getSectionsInformationFromInternet();
//        });
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        String currentTitle = getCurrentTitleOnTheAppBarText().toString().toLowerCase();
//        if (!currentTitle.isEmpty()) {
//            outState.putString(CONFIGURATION_KEY, currentTitle);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (backgroundState != null
//                && backgroundState.getString(FORE_BACK_STATE_KEY) != null && backgroundState.getString(FORE_BACK_STATE_KEY) != null
//                && !backgroundState.getString(FORE_BACK_STATE_KEY).isEmpty()) {
//            mClickedSectionName = backgroundState.getString(FORE_BACK_STATE_KEY);
//            populateSectionNameOnTheAppBar(mClickedSectionName);
//            if (hasInternet()) {
//                refreshOneScreenWithInternet();
//            } else {
//                loadEverythingFromDataBase();
//            }
//        } else {
//            if (getCurrentTitleOnTheAppBarText() == null || getCurrentTitleOnTheAppBarText().toString().isEmpty()) {
//                if (hasInternet()) {
//                    initialScreenWithInternet();
//                } else {
//                    loadEverythingFromDataBase();
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (getCurrentTitleOnTheAppBarText() != null) {
//            backgroundState = new Bundle();
//            backgroundState.putString(FORE_BACK_STATE_KEY, getCurrentTitleOnTheAppBarText().toString().toLowerCase());
//        }
//    }

//
//
//    private void loadEverythingFromDataBase() {
//        getLoaderManager().initLoader(VIAPLAY_LOADER, null, MainActivity.this);
//    }
//
//    private void loadNavigationBarItemsFromInternet(List<ViaplaySection> viaplaySections) {
//        for (int i = 0; i < viaplaySections.size(); i++) {
//            filterOutDifferentSectionNames(viaplaySections.get(i).getTitle());
//        }
//        mSectionAdapter.setUpTitleStringArray(mSectionTitlesString);
//    }
//
//    private void putSectionTitleDataIntoDatabase() {
//        cleanSectionTableFromDatabase();
//        int count = mSectionTitlesString.size();
//        Vector<ContentValues> cVVector = new Vector<>(count);
//
//        for (int i = 0; i < count; i++) {
//            ContentValues values = new ContentValues();
//            // Temporarily store section title's information to every column because they couldn't be null
//            values.put(SectionEntry.COLUMN_SECTION_TITLE, mSectionTitlesString.get(i).toLowerCase());
//            values.put(SectionEntry.COLUMN_SECTION_LONG_TITLE, mSectionTitlesString.get(i));
//            values.put(SectionEntry.COLUMN_SECTION_DESCRIPTION, mSectionTitlesString.get(i));
//            cVVector.add(values);
//        }
//
//        if (cVVector.size() > 0) {
//            ContentValues[] cvArray = new ContentValues[cVVector.size()];
//            cVVector.toArray(cvArray);
//            int bulkInsertRows = getContentResolver().bulkInsert(
//                    SectionEntry.CONTENT_URI,
//                    cvArray);
//
//            if (bulkInsertRows == cVVector.size()) {
//                Log.i(LOG_TAG, "DB bulkInsert into SectionEntry successful.");
//            } else {
//                Log.e(LOG_TAG, "DB bulkInsert into SectionEntry unsuccessful. The number of bulkInsertRows is: "
//                        + bulkInsertRows + " and the number of data size is: " + cVVector.size());
//            }
//        }
//    }
//
//    private void cleanSectionTableFromDatabase() {
//        int rowsDeleted = getContentResolver().delete(SectionEntry.CONTENT_URI, null, null);
//        if (rowsDeleted != -1) {
//            Log.i(LOG_TAG, rowsDeleted + " rows in section table is been cleaned.");
//        } else {
//            Log.e(LOG_TAG, "delete section table in database failed.");
//        }
//    }

//    private void getSectionsInformationFromInternet() {
//        mFirstSectionName = mSectionTitlesString.get(0).toLowerCase();
//        for (int i = 0; i < mSectionTitlesString.size(); i++) {
//            String sectionName = mSectionTitlesString.get(i).toLowerCase();
//            mDetailFragmentViewModel = ViewModelProviders.of(this)
//                    .get(DetailFragmentViewModel.class);
//            mDetailFragmentViewModel.init(sectionName);
//            mDetailFragmentViewModel.getSingleJSONResponseLiveData().observe(this, singleJSONResponse -> {
//                putSectionInformationIntoDatabase(sectionName, singleJSONResponse);
//            });
//        }
//    }

//    private void putSectionInformationIntoDatabase(String sectionName, SingleJSONResponse singleJSONResponse) {
//        String currentLongTitle = singleJSONResponse.getTitle();
//        String currentDescription = singleJSONResponse.getDescription();
//
//        if (null != currentLongTitle && !currentLongTitle.isEmpty() && null != currentDescription
//                && !currentDescription.isEmpty()) {
//
//            // Set up the first state of the app
//            if (sectionName.equalsIgnoreCase(mFirstSectionName)) {
//                populateContentViews(currentLongTitle, currentDescription);
//                populateSectionNameOnTheAppBar(mFirstSectionName);
//            }
//
//            // Set up clicked section information
//            if (mClickedSectionName != null && !mClickedSectionName.isEmpty()) {
//                populateContentViews(currentLongTitle, currentDescription);
//            }
//
//            // Update database with complete information for one specific ViaplaySection
//            ContentValues values = new ContentValues();
//            values.put(SectionEntry.COLUMN_SECTION_LONG_TITLE, currentLongTitle);
//            values.put(SectionEntry.COLUMN_SECTION_DESCRIPTION, currentDescription);
//            String selection = SectionEntry.COLUMN_SECTION_TITLE;
//            String[] selectionArgs = {sectionName};
//
//            int rowsUpdated = getContentResolver().update(SectionEntry.CONTENT_URI, values,
//                    selection, selectionArgs);
//
//            if (rowsUpdated > 0) {
//                Log.i(LOG_TAG, "DB Update long title and description information for "
//                        + sectionName + " section is successful.");
//            }
//        }
//    }
//
//    private void populateSectionNameOnTheAppBar(String sectionName) {
//        mBinding.appBar.titleOnTheAppBar.setText(sectionName);
//    }

//    private void populateContentViews(String currentLongTitle, String currentDescription) {
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        showContentView();
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        DetailFragment sectionDetailContentFragment = new DetailFragment();
//        sectionDetailContentFragment.setContentText(currentLongTitle, currentDescription);
//        fragmentManager.beginTransaction()
//                .replace(R.id.detail_fragment_container, sectionDetailContentFragment)
//                .commit();
//
//        if (mIdlingResource != null) {
//            mIdlingResource.setIdleState(true);
//        }
//    }

//    @Override
//    public void onClick(String sectionTitle) {
//
//        showContentView();
//        mBinding.drawerLayout.closeDrawer(mBinding.leftDrawer);
//        populateSectionNameOnTheAppBar(sectionTitle);
//        mClickedSectionName = sectionTitle.toLowerCase();

//        if (hasInternet()) {
//            refreshOneScreenWithInternet();
//        } else {
//            loadEverythingFromDataBase();
//        }
//    }

//    private void refreshOneScreenWithInternet() {
//        if (mIdlingResource != null) {
//            mIdlingResource.setIdleState(false);
//        }
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(this, getString(R.string.toast_message_data_is_loading), Toast.LENGTH_SHORT);
//        mToast.setGravity(Gravity.BOTTOM, 0, 0);
//        mToast.show();
//
//        mDetailFragmentViewModel = ViewModelProviders.of(this)
//                .get(DetailFragmentViewModel.class);
//        mDetailFragmentViewModel.init(mClickedSectionName);
//        mDetailFragmentViewModel.getSingleJSONResponseLiveData().observe(this, singleJSONResponse -> {
//            putSectionInformationIntoDatabase(mClickedSectionName, singleJSONResponse);
//        });
//    }

//    public void filterOutDifferentSectionNames(String sectionName) {
//        if (!mSectionTitlesString.contains(sectionName.toLowerCase())) {
//            mSectionTitlesString.add(sectionName.toLowerCase());
//        }
//    }
//
//    private CharSequence getCurrentTitleOnTheAppBarText() {
//        return mBinding.appBar.titleOnTheAppBar.getText();
//    }

//    private void showEmptyView() {
//        mBinding.swipeRefresh.setRefreshing(false);
//        mBinding.detailFragmentContainer.setVisibility(View.INVISIBLE);
//        mBinding.emptyView.setVisibility(View.VISIBLE);
//    }
//
//    private void showContentView() {
//        mBinding.swipeRefresh.setRefreshing(false);
//        mBinding.detailFragmentContainer.setVisibility(View.VISIBLE);
//        mBinding.emptyView.setVisibility(View.INVISIBLE);
//    }

    private boolean hasInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();
    }


}
