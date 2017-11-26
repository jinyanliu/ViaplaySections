package se.sugarest.jane.viaplaysections.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.ViaplaySectionInformationViewModel;
import se.sugarest.jane.viaplaysections.ViaplaySectionNameViewModel;
import se.sugarest.jane.viaplaysections.data.SectionAdapter;
import se.sugarest.jane.viaplaysections.data.database.SectionContract.SectionEntry;
import se.sugarest.jane.viaplaysections.data.type.SingleJSONResponse;
import se.sugarest.jane.viaplaysections.data.type.ViaplaySection;
import se.sugarest.jane.viaplaysections.idlingResource.SimpleIdlingResource;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_LOADER;

/**
 * This is the main controller of the whole app.
 * It initiates the app.
 */
public class MainActivity extends AppCompatActivity implements SectionAdapter.SectionAdapterOnClickHandler
        , android.app.LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Bundle backgroundState;
    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private ImageView mImageNavigationMenu;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private TextView mTextViewTitleOnTheAppBar;
    private TextView mEmptyView;
    private LinearLayout mContentView;
    private RecyclerView mRecyclerView;
    private SectionAdapter mSectionAdapter;
    private String mClickedSectionName;
    private String mFirstSectionName;
    private Toast mToast;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViaplaySectionNameViewModel mSectionNameViewModel;
    private ViaplaySectionInformationViewModel mSectionInformationViewModel;

    private ArrayList<String> mSectionTitlesString = new ArrayList<>();

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

        // Set up ToolBar
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mTextViewTitle = findViewById(R.id.section_title);
        mTextViewDescription = findViewById(R.id.section_description);
        mTextViewTitleOnTheAppBar = findViewById(R.id.title_on_the_app_bar);
        mImageNavigationMenu = findViewById(R.id.navigation_menu);
        mEmptyView = findViewById(R.id.empty_view);
        mContentView = findViewById(R.id.content_activity_main);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        setUpRecyclerViewWithAdapter();

        // Set up left drawer navigation
        mImageNavigationMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                    mDrawerLayout.closeDrawer(mRecyclerView);
                } else if (!mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                    mDrawerLayout.openDrawer(mRecyclerView);
                }
            }
        });

        // Set up swipe refresh function
        mSwipeRefreshLayout.setOnRefreshListener(
                () -> {
                    if (!hasInternet()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast = Toast.makeText(MainActivity.this, getString(R.string.toast_message_offline_cannot_refresh), Toast.LENGTH_SHORT);
                        mToast.setGravity(Gravity.BOTTOM, 0, 0);
                        mToast.show();
                    } else {
                        if (mTextViewTitleOnTheAppBar.getText() == null || mTextViewTitleOnTheAppBar.getText().toString() == null ||
                                mTextViewTitleOnTheAppBar.getText().toString().isEmpty()) {
                            initialScreenWithInternet();
                        } else {
                            mClickedSectionName = mTextViewTitleOnTheAppBar.getText().toString().toLowerCase();
                            refreshOneScreenWithInternet();
                        }
                    }
                }
        );

        if (hasInternet()) {
            initialScreenWithInternet();
        } else {
            loadEverythingFromDataBase();
        }


//        // Current content survive while configuration change happens
//        if (savedInstanceState != null && savedInstanceState.containsKey(CONFIGURATION_KEY)) {
//            mCurrentTitleLowerCase = savedInstanceState.getString(CONFIGURATION_KEY);
//            setPageContent(mCurrentTitleLowerCase);
//        } else {
//            refreshScreen();
//        }

        // Get the IdlingResource instance
        getIdlingResource();
    }

    private void initialScreenWithInternet() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, getString(R.string.toast_message_data_is_loading), Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.show();
        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        mSectionNameViewModel = ViewModelProviders.of(this).get(ViaplaySectionNameViewModel.class);
        mSectionNameViewModel.getSectionNames().observe(this, sectionNames -> {
            // Update Navigation Bar items
            loadNavigationBarItemsFromInternet(sectionNames);
            putSectionTitleDataIntoDatabase();
            getSectionsInformationFromInternet();
        });
    }

    //    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        String currentTitle = mTextViewTitleOnTheAppBar.getText().toString().toLowerCase();
//        if (currentTitle != null && !currentTitle.isEmpty()) {
//            outState.putString(CONFIGURATION_KEY, currentTitle);
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (backgroundState != null
//                && backgroundState.getString(FORE_BACK_STATE_KEY) != null
//                && !backgroundState.getString(FORE_BACK_STATE_KEY).isEmpty()) {
//            mCurrentTitleLowerCase = backgroundState.getString(FORE_BACK_STATE_KEY);
//            setPageContent(mCurrentTitleLowerCase);
//        } else {
//            String currentStringInTitleContentView = mTextViewTitle.getText().toString();
//            if (currentStringInTitleContentView == null || currentStringInTitleContentView.isEmpty()) {
//                refreshScreen();
//            }
//        }
//    }
//
//    private void setPageContent(String currentTitle) {
//        mCurrentTitleLowerCase = currentTitle.toLowerCase();
//        mTextViewTitleOnTheAppBar.setText(mCurrentTitleLowerCase);
//        loadEverythingFromDataBase();
//        loadContentFromDatabase(mCurrentTitleLowerCase);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mTextViewTitleOnTheAppBar.getText() != null && mTextViewTitleOnTheAppBar.getText().toString() != null) {
//            backgroundState = new Bundle();
//            mCurrentTitleLowerCase = mTextViewTitleOnTheAppBar.getText().toString();
//            backgroundState.putString(FORE_BACK_STATE_KEY, mCurrentTitleLowerCase.toLowerCase());
//        }
//    }

    private void setUpRecyclerViewWithAdapter() {
        mRecyclerView = findViewById(R.id.left_drawer);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        if (mSectionAdapter == null) {
            mSectionAdapter = new SectionAdapter(this);
        }
        mRecyclerView.setAdapter(mSectionAdapter);
    }

    private void loadEverythingFromDataBase() {
        getLoaderManager().initLoader(VIAPLAY_LOADER, null, MainActivity.this);
    }

    private void loadNavigationBarItemsFromInternet(List<ViaplaySection> viaplaySections) {
        for (int i = 0; i < viaplaySections.size(); i++) {
            if (!mSectionTitlesString.contains(viaplaySections.get(i).getTitle())) {
                mSectionTitlesString.add(viaplaySections.get(i).getTitle());
            }
        }
        showContentView();
        mSectionAdapter.setUpTitleStringArray(mSectionTitlesString);
    }

    private void putSectionTitleDataIntoDatabase() {
        cleanSectionTableFromDatabase();
        int count = mSectionTitlesString.size();
        Vector<ContentValues> cVVector = new Vector<>(count);

        for (int i = 0; i < count; i++) {
            ContentValues values = new ContentValues();
            // Temporarily store section title's information to every column because they couldn't be null
            values.put(SectionEntry.COLUMN_SECTION_TITLE, mSectionTitlesString.get(i).toLowerCase());
            values.put(SectionEntry.COLUMN_SECTION_LONG_TITLE, mSectionTitlesString.get(i));
            values.put(SectionEntry.COLUMN_SECTION_DESCRIPTION, mSectionTitlesString.get(i));
            cVVector.add(values);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int bulkInsertRows = getContentResolver().bulkInsert(
                    SectionEntry.CONTENT_URI,
                    cvArray);

            if (bulkInsertRows == cVVector.size()) {
                Log.i(LOG_TAG, "DB bulkInsert into SectionEntry successful.");
            } else {
                Log.e(LOG_TAG, "DB bulkInsert into SectionEntry unsuccessful. The number of bulkInsertRows is: "
                        + bulkInsertRows + " and the number of data size is: " + cVVector.size());
            }
        }
    }

    private void cleanSectionTableFromDatabase() {
        int rowsDeleted = getContentResolver().delete(SectionEntry.CONTENT_URI, null, null);
        if (rowsDeleted != -1) {
            Log.i(LOG_TAG, rowsDeleted + " rows in section table is been cleaned.");
        } else {
            Log.e(LOG_TAG, "delete section table in database failed.");
        }
    }

    private void getSectionsInformationFromInternet() {
        mFirstSectionName = mSectionTitlesString.get(0).toLowerCase();
        for (int i = 0; i < mSectionTitlesString.size(); i++) {
            String sectionName = mSectionTitlesString.get(i).toLowerCase();
            mSectionInformationViewModel = ViewModelProviders.of(this)
                    .get(ViaplaySectionInformationViewModel.class);
            mSectionInformationViewModel.init(sectionName);
            mSectionInformationViewModel.getSingleJSONResponseLiveData().observe(this, singleJSONResponse -> {
                putSectionInformationIntoDatabase(sectionName, singleJSONResponse);
            });
        }
    }

    private void putSectionInformationIntoDatabase(String sectionName, SingleJSONResponse singleJSONResponse) {
        String currentLongTitle = singleJSONResponse.getTitle();
        String currentDescription = singleJSONResponse.getDescription();

        if (null != currentLongTitle && !currentLongTitle.isEmpty() && null != currentDescription
                && !currentDescription.isEmpty()) {

            // Set up the first state of the app
            if (sectionName.equalsIgnoreCase(mFirstSectionName)) {
                populateContentViews(currentLongTitle, currentDescription);
                mTextViewTitleOnTheAppBar.setText(mFirstSectionName);
            }

            // Set up clicked section information
            if (mClickedSectionName != null && !mClickedSectionName.isEmpty()) {
                populateContentViews(currentLongTitle, currentDescription);
            }

            // Update database with complete information for one specific ViaplaySection
            ContentValues values = new ContentValues();
            values.put(SectionEntry.COLUMN_SECTION_LONG_TITLE, currentLongTitle);
            values.put(SectionEntry.COLUMN_SECTION_DESCRIPTION, currentDescription);
            String selection = SectionEntry.COLUMN_SECTION_TITLE;
            String[] selectionArgs = {sectionName};

            int rowsUpdated = getContentResolver().update(SectionEntry.CONTENT_URI, values,
                    selection, selectionArgs);

            if (rowsUpdated > 0) {
                Log.i(LOG_TAG, "DB Update long title and description information for "
                        + sectionName + " section is successful.");
            }
        }
    }

    private void populateContentViews(String currentLongTitle, String currentDescription) {
        if (mToast != null) {
            mToast.cancel();
        }
        showContentView();
        mTextViewTitle.setText(currentLongTitle);
        mTextViewDescription.setText(currentDescription);

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onClick(String sectionTitle) {

        showContentView();
        mDrawerLayout.closeDrawer(mRecyclerView);
        mTextViewTitleOnTheAppBar.setText(sectionTitle);
        mClickedSectionName = sectionTitle.toLowerCase();

        if (hasInternet()) {
            refreshOneScreenWithInternet();
        } else {
            loadEverythingFromDataBase();
        }
    }

    private void refreshOneScreenWithInternet() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, getString(R.string.toast_message_data_is_loading), Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.BOTTOM, 0, 0);
        mToast.show();

        mSectionInformationViewModel = ViewModelProviders.of(this)
                .get(ViaplaySectionInformationViewModel.class);
        mSectionInformationViewModel.init(mClickedSectionName);
        mSectionInformationViewModel.getSingleJSONResponseLiveData().observe(this, singleJSONResponse -> {
            putSectionInformationIntoDatabase(mClickedSectionName, singleJSONResponse);
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(this, SectionEntry.CONTENT_URI, null, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null && cursor.getCount() > 0) {

            // Opens the app, show first section's information.
            if (mClickedSectionName == null || mClickedSectionName.isEmpty()) {
                cursor.moveToFirst();
                mFirstSectionName = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_TITLE));
                mTextViewTitleOnTheAppBar.setText(mFirstSectionName);

                String firstSectionTitle = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_LONG_TITLE));
                String firstSectionDescription = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_DESCRIPTION));
                populateContentViews(firstSectionTitle, firstSectionDescription);
            }

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String currentTitle = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_TITLE));

                // Click a section, show specific section's information.
                if (mClickedSectionName != null && !mClickedSectionName.isEmpty()) {
                    if (currentTitle.equalsIgnoreCase(mClickedSectionName)) {
                        cursor.moveToPosition(i);
                        String sectionTitle = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_LONG_TITLE));
                        String sectionDescription = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_DESCRIPTION));
                        if (sectionTitle.equalsIgnoreCase(mClickedSectionName)
                                || sectionDescription.equalsIgnoreCase(mClickedSectionName)) {
                            showEmptyView();
                        } else {
                            populateContentViews(sectionTitle, sectionDescription);
                        }
                    }
                } else {
                    // Opens the app, show drawer navigation items list
                    if (!mSectionTitlesString.contains(currentTitle)) {
                        mSectionTitlesString.add(currentTitle);
                    }
                    Log.i(LOG_TAG, "There are " + mSectionTitlesString.size() + " different section titles available.");

                    showContentView();
                    mSectionAdapter.setUpTitleStringArray(mSectionTitlesString);
                }
            }

        } else {
            showEmptyView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSectionAdapter.setUpTitleStringArray(null);
    }

    private void showEmptyView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mContentView.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showContentView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mContentView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
    }

    private boolean hasInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
