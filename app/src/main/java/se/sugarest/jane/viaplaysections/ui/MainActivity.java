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
import android.support.v4.app.FragmentManager;
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
import se.sugarest.jane.viaplaysections.architecture_components.viewModels.SectionProfileViewModel;
import se.sugarest.jane.viaplaysections.architecture_components.viewModels.SectionNameViewModel;
import se.sugarest.jane.viaplaysections.data.SectionAdapter;
import se.sugarest.jane.viaplaysections.data.database.SectionContract.SectionEntry;
import se.sugarest.jane.viaplaysections.data.datatype.SingleJSONResponse;
import se.sugarest.jane.viaplaysections.data.datatype.ViaplaySection;
import se.sugarest.jane.viaplaysections.idling_resource.SimpleIdlingResource;

import static se.sugarest.jane.viaplaysections.util.Constants.CONFIGURATION_KEY;
import static se.sugarest.jane.viaplaysections.util.Constants.FORE_BACK_STATE_KEY;
import static se.sugarest.jane.viaplaysections.util.Constants.SECTION_INFORMATION_CONTENT_TEXT_STIZE;
import static se.sugarest.jane.viaplaysections.util.Constants.SECTION_INFORMATION_LABEL_TEXT_SIZE;
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
    private TextView mTextViewTitleOnTheAppBar;
    private TextView mEmptyView;
    private LinearLayout mContentView;
    private RecyclerView mRecyclerView;
    private SectionAdapter mSectionAdapter;
    private String mClickedSectionName;
    private String mFirstSectionName;
    private Toast mToast;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SectionNameViewModel mSectionNameViewModel;
    private SectionProfileViewModel mSectionProfileViewModel;
    private FragmentManager mFragmentManager;

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

        // Set up ToolBar
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFragmentManager = getSupportFragmentManager();

        SectionTextViewFragment titleLabelFragment = new SectionTextViewFragment();
        titleLabelFragment.setmContentText(getString(R.string.section_title_label));
        titleLabelFragment.setmTextSize(SECTION_INFORMATION_LABEL_TEXT_SIZE);
        mFragmentManager.beginTransaction()
                .add(R.id.section_title_label_container, titleLabelFragment)
                .commit();

        SectionTextViewFragment descriptionLabelFragment = new SectionTextViewFragment();
        descriptionLabelFragment.setmContentText(getString(R.string.section_description_label));
        descriptionLabelFragment.setmTextSize(SECTION_INFORMATION_LABEL_TEXT_SIZE);
        mFragmentManager.beginTransaction()
                .add(R.id.section_description_label_container, descriptionLabelFragment)
                .commit();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mTextViewTitleOnTheAppBar = findViewById(R.id.title_on_the_app_bar);
        mImageNavigationMenu = findViewById(R.id.navigation_menu);
        mEmptyView = findViewById(R.id.empty_view);
        mContentView = findViewById(R.id.content_activity_main);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        setUpRecyclerViewWithAdapter();

        // Set up left drawer navigation
        mImageNavigationMenu.setOnClickListener(view -> {
            if (mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                mDrawerLayout.closeDrawer(mRecyclerView);
            } else if (!mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                mDrawerLayout.openDrawer(mRecyclerView);
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
                        if (mTextViewTitleOnTheAppBar.getText() == null ||
                                mTextViewTitleOnTheAppBar.getText().toString().isEmpty()) {
                            initialScreenWithInternet();
                        } else {
                            mClickedSectionName = mTextViewTitleOnTheAppBar.getText().toString().toLowerCase();
                            refreshOneScreenWithInternet();
                        }
                    }
                }
        );

        // Current content survive while rotates the phone
        if (savedInstanceState != null && savedInstanceState.containsKey(CONFIGURATION_KEY)) {
            mClickedSectionName = savedInstanceState.getString(CONFIGURATION_KEY);
            mTextViewTitleOnTheAppBar.setText(mClickedSectionName);
            loadEverythingFromDataBase();
        } else {
            if (hasInternet()) {
                initialScreenWithInternet();
            } else {
                loadEverythingFromDataBase();
            }
        }

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
        // Re-created activities receive the same SectionNameViewModel instance created by the first activity.
        mSectionNameViewModel = ViewModelProviders.of(this).get(SectionNameViewModel.class);
        mSectionNameViewModel.init();
        mSectionNameViewModel.getSectionNames().observe(this, sectionNames -> {
            // Update Navigation Bar items
            loadNavigationBarItemsFromInternet(sectionNames);
            putSectionTitleDataIntoDatabase();
            getSectionsInformationFromInternet();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String currentTitle = mTextViewTitleOnTheAppBar.getText().toString().toLowerCase();
        if (!currentTitle.isEmpty()) {
            outState.putString(CONFIGURATION_KEY, currentTitle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backgroundState != null
                && backgroundState.getString(FORE_BACK_STATE_KEY) != null && backgroundState.getString(FORE_BACK_STATE_KEY) != null
                && !backgroundState.getString(FORE_BACK_STATE_KEY).isEmpty()) {
            mClickedSectionName = backgroundState.getString(FORE_BACK_STATE_KEY);
            mTextViewTitleOnTheAppBar.setText(mClickedSectionName);
            if (hasInternet()) {
                refreshOneScreenWithInternet();
            } else {
                loadEverythingFromDataBase();
            }
        } else {
            if (mTextViewTitleOnTheAppBar.getText() == null || mTextViewTitleOnTheAppBar.getText().toString().isEmpty()) {
                if (hasInternet()) {
                    initialScreenWithInternet();
                } else {
                    loadEverythingFromDataBase();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTextViewTitleOnTheAppBar.getText() != null) {
            backgroundState = new Bundle();
            backgroundState.putString(FORE_BACK_STATE_KEY, mTextViewTitleOnTheAppBar.getText().toString().toLowerCase());
        }
    }

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
            filterOutDifferentSectionNames(viaplaySections.get(i).getTitle());
        }
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
            mSectionProfileViewModel = ViewModelProviders.of(this)
                    .get(SectionProfileViewModel.class);
            mSectionProfileViewModel.init(sectionName);
            mSectionProfileViewModel.getSingleJSONResponseLiveData().observe(this, singleJSONResponse -> {
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

        SectionTextViewFragment titleContentFragment = new SectionTextViewFragment();
        titleContentFragment.setmContentText(currentLongTitle);
        titleContentFragment.setmTextSize(SECTION_INFORMATION_CONTENT_TEXT_STIZE);
        mFragmentManager.beginTransaction()
                .replace(R.id.section_title_container, titleContentFragment)
                .commit();

        SectionTextViewFragment descriptionContentFragment = new SectionTextViewFragment();
        descriptionContentFragment.setmContentText(currentDescription);
        descriptionContentFragment.setmTextSize(SECTION_INFORMATION_CONTENT_TEXT_STIZE);
        mFragmentManager.beginTransaction()
                .replace(R.id.section_description_container, descriptionContentFragment)
                .commit();

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

        mSectionProfileViewModel = ViewModelProviders.of(this)
                .get(SectionProfileViewModel.class);
        mSectionProfileViewModel.init(mClickedSectionName);
        mSectionProfileViewModel.getSingleJSONResponseLiveData().observe(this, singleJSONResponse -> {
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

            if (!hasInternet()) {
                // Opens the app, show first section's information.
                if (mClickedSectionName == null || mClickedSectionName.isEmpty()) {
                    cursor.moveToFirst();
                    mFirstSectionName = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_TITLE));
                    mTextViewTitleOnTheAppBar.setText(mFirstSectionName);

                    String firstSectionTitle = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_LONG_TITLE));
                    String firstSectionDescription = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_DESCRIPTION));
                    populateContentViews(firstSectionTitle, firstSectionDescription);
                }
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
                }

                filterOutDifferentSectionNames(currentTitle);
                mSectionAdapter.setUpTitleStringArray(mSectionTitlesString);
            }

        } else {
            showEmptyView();
        }
    }

    public void filterOutDifferentSectionNames(String sectionName) {
        if (!mSectionTitlesString.contains(sectionName.toLowerCase())) {
            mSectionTitlesString.add(sectionName.toLowerCase());
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
        return connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();
    }
}
