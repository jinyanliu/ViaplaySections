package se.sugarest.jane.viaplaysections.ui;

import android.arch.lifecycle.ViewModelProviders;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.data.SectionAdapter;
import se.sugarest.jane.viaplaysections.data.type.ViaplaySectionNameViewModel;
import se.sugarest.jane.viaplaysections.idlingResource.SimpleIdlingResource;

/**
 * This is the main controller of the whole app.
 * It initiates the app.
 */
public class MainActivity extends AppCompatActivity implements SectionAdapter.SectionAdapterOnClickHandler {
        //, android.app.LoaderManager.LoaderCallbacks<Cursor> {
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
    private String mCurrentTitle;
    private String mFirstTitle;
    private Toast mToast;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViaplaySectionNameViewModel mViewModel;

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

//        // Set up swipe refresh function
//        mSwipeRefreshLayout.setOnRefreshListener(
//                new SwipeRefreshLayout.OnRefreshListener() {
//                    @Override
//                    public void onRefresh() {
//                        if (mTextViewTitleOnTheAppBar.getText() == null || mTextViewTitleOnTheAppBar.getText().toString() == null ||
//                                mTextViewTitleOnTheAppBar.getText().toString().isEmpty()) {
//                            refreshScreen();
//                        } else {
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                }
//        );

        // Create a ViewModel the first time the system calls an activity's onCreate() method.
        // Re-created activities receive the same MyViewModel instance created by the first activity.
        mViewModel = ViewModelProviders.of(this).get(ViaplaySectionNameViewModel.class);
        mViewModel.getSectionNames().observe(this, sectionNames -> {
            // Update Navigation Bar items
            mSectionAdapter.setUpTitleStringArray((ArrayList<String>) sectionNames);
        });

//        // Current content survive while configuration change happens
//        if (savedInstanceState != null && savedInstanceState.containsKey(CONFIGURATION_KEY)) {
//            mCurrentTitle = savedInstanceState.getString(CONFIGURATION_KEY);
//            setPageContent(mCurrentTitle);
//        } else {
//            refreshScreen();
//        }

        // Get the IdlingResource instance
        getIdlingResource();
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
//            mCurrentTitle = backgroundState.getString(FORE_BACK_STATE_KEY);
//            setPageContent(mCurrentTitle);
//        } else {
//            String currentStringInTitleContentView = mTextViewTitle.getText().toString();
//            if (currentStringInTitleContentView == null || currentStringInTitleContentView.isEmpty()) {
//                refreshScreen();
//            }
//        }
//    }
//
//    private void setPageContent(String currentTitle) {
//        mCurrentTitle = currentTitle.toLowerCase();
//        mTextViewTitleOnTheAppBar.setText(mCurrentTitle);
//        loadNavigationBarItemsFromDataBase();
//        loadContentFromDatabase(mCurrentTitle);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mTextViewTitleOnTheAppBar.getText() != null && mTextViewTitleOnTheAppBar.getText().toString() != null) {
//            backgroundState = new Bundle();
//            mCurrentTitle = mTextViewTitleOnTheAppBar.getText().toString();
//            backgroundState.putString(FORE_BACK_STATE_KEY, mCurrentTitle.toLowerCase());
//        }
//    }
//
//    private void refreshScreen() {
//        if (!hasInternet()) {
//            loadNavigationBarItemsFromDataBase();
//            loadFirstContentStateFromDatabase();
//        } else {
//
//            if (mIdlingResource != null) {
//                mIdlingResource.setIdleState(false);
//            }
//
//            showContentView();
//            sendNetworkRequestGet();
//            if (mToast != null) {
//                mToast.cancel();
//            }
//            mToast = Toast.makeText(this, getString(R.string.toast_message_data_is_loading), Toast.LENGTH_SHORT);
//            mToast.setGravity(Gravity.BOTTOM, 0, 0);
//            mToast.show();
//        }
//    }
//
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
//
//    private void loadNavigationBarItemsFromDataBase() {
//        getLoaderManager().initLoader(VIAPLAY_LOADER, null, MainActivity.this);
//    }
//
//    // Use External Library Retrofit to GET ViaplaySection object list.
//    // Reference: https://github.com/square/retrofit
//    private void sendNetworkRequestGet() {
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(VIAPLAY_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.client(httpClient.build()).build();
//        ViaplayClient client = retrofit.create(ViaplayClient.class);
//        Call<JSONResponse> call = client.getSections();
//
//        call.enqueue(new Callback<JSONResponse>() {
//            @Override
//            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
//
//                Log.i(LOG_TAG, "GET response success: Complete url to request is: "
//                        + response.raw().request().url().toString()
//                        + "\nresponse.body().toString == " + response.body().toString());
//
//                List<ViaplaySection> viaplaySections = response.body().getLinks().getViaplaySections();
//
//                if (viaplaySections != null && !viaplaySections.isEmpty()) {
//
//                    Log.i(LOG_TAG, "The list of ViaplaySections are: " + viaplaySections.toString());
//
//                    putSectionTitleDataIntoDatabase(viaplaySections);
//
//                    loadNavigationBarItemsFromInternet(viaplaySections);
//
//                    mFirstTitle = viaplaySections.get(0).getTitle();
//                    showContentView();
//                    mTextViewTitleOnTheAppBar.setText(mFirstTitle);
//                    mCurrentTitle = mFirstTitle;
//
//                    for (int i = 0; i < viaplaySections.size(); i++) {
//                        sendNetworkRequestGetOneSection(viaplaySections.get(i).getTitle().toLowerCase());
//                        Log.i(LOG_TAG, "Send network request to get one section's long title and description: "
//                                + viaplaySections.get(i).getTitle());
//                    }
//                } else {
//                    loadNavigationBarItemsFromDataBase();
//                    loadFirstContentStateFromDatabase();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JSONResponse> call, Throwable t) {
//                Log.e(LOG_TAG, "Failed to get titles list back.", t);
//                loadNavigationBarItemsFromDataBase();
//                loadFirstContentStateFromDatabase();
//            }
//        });
//    }
//
//    private void loadNavigationBarItemsFromInternet(List<ViaplaySection> viaplaySections) {
//        ArrayList<String> sectionTitlesString = new ArrayList<>();
//        for (int i = 0; i < viaplaySections.size(); i++) {
//            String currentTitle = viaplaySections.get(i).getTitle();
//            if (!sectionTitlesString.contains(currentTitle)) {
//                sectionTitlesString.add(currentTitle);
//            }
//            Log.i(LOG_TAG, "There are " + sectionTitlesString.size() + " different section titles available.");
//        }
//        showContentView();
//        mSectionAdapter.setUpTitleStringArray(sectionTitlesString);
//    }
//
//    private void loadFirstContentStateFromDatabase() {
//        Cursor cursor = getContentResolver().query(SectionEntry.CONTENT_URI, null,
//                null, null, null);
//        if (cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            String title = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_TITLE));
//            mTextViewTitleOnTheAppBar.setText(title);
//            loadContentFromDatabase(title);
//        } else {
//            showEmptyView();
//        }
//    }
//
//    // Use External Library Retrofit to GET one specific ViaplaySection's detail title and description.
//    // Reference: https://github.com/square/retrofit
//    private void sendNetworkRequestGetOneSection(final String currentTitle) {
//
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(VIAPLAY_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//        ViaplayClient client = retrofit.create(ViaplayClient.class);
//        Call<SingleJSONResponse> call = client.getOneSectionByTitle(currentTitle);
//
//        call.enqueue(new Callback<SingleJSONResponse>() {
//            @Override
//            public void onResponse(Call<SingleJSONResponse> call, Response<SingleJSONResponse> response) {
//
//                Log.i(LOG_TAG, "GET ONE response success: Complete url to request is: "
//                        + response.raw().request().url().toString()
//                        + "\nresponse.code() == " + response.code());
//
//                String currentLongTitle = response.body().getTitle();
//                String currentDescription = response.body().getDescription();
//
//                if (null != currentLongTitle && !currentLongTitle.isEmpty() && null != currentDescription
//                        && !currentDescription.isEmpty()) {
//
//                    // Set up the first state of the app
//                    if (currentTitle.equalsIgnoreCase(mFirstTitle)) {
//                        populateContentViews(currentLongTitle, currentDescription);
//                    }
//
//                    // Update database with complete information for one specific ViaplaySection
//                    ContentValues values = new ContentValues();
//                    values.put(SectionEntry.COLUMN_SECTION_LONG_TITLE, currentLongTitle);
//                    values.put(SectionEntry.COLUMN_SECTION_DESCRIPTION, currentDescription);
//                    String selection = SectionEntry.COLUMN_SECTION_TITLE;
//                    String[] selectionArgs = {currentTitle};
//
//                    int rowsUpdated = getContentResolver().update(SectionEntry.CONTENT_URI, values,
//                            selection, selectionArgs);
//
//                    if (rowsUpdated > 0) {
//                        Log.i(LOG_TAG, "DB Update long title and description information for "
//                                + currentTitle + " section is successful.");
//                    }
//                } else {
//                    loadContentFromDatabase(currentTitle);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<SingleJSONResponse> call, Throwable t) {
//                Log.e(LOG_TAG, "Failed to get section data back with title: " + currentTitle, t);
//                loadContentFromDatabase(currentTitle);
//            }
//        });
//    }
//
//    private void loadContentFromDatabase(String currentTitle) {
//        String selection = SectionEntry.COLUMN_SECTION_TITLE + " =?";
//        String[] selectionArgs = {currentTitle.toLowerCase()};
//        Cursor cursor = getContentResolver().query(SectionEntry.CONTENT_URI, null,
//                selection, selectionArgs, null);
//
//        if (cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            String currentLongTitle = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_LONG_TITLE));
//            String currentDescription = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_DESCRIPTION));
//
//            if (!currentLongTitle.equalsIgnoreCase(currentTitle)) {
//                populateContentViews(currentLongTitle, currentDescription);
//            }
//        } else {
//            showEmptyView();
//        }
//    }
//
//    private void populateContentViews(String currentLongTitle, String currentDescription) {
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        showContentView();
//        mTextViewTitle.setText(currentLongTitle);
//        mTextViewDescription.setText(currentDescription);
//
//        if (mIdlingResource != null) {
//            mIdlingResource.setIdleState(true);
//        }
//    }
//
//    private void putSectionTitleDataIntoDatabase(List<ViaplaySection> viaplaySections) {
//        cleanSectionTableFromDatabase();
//        int count = viaplaySections.size();
//        Vector<ContentValues> cVVector = new Vector<>(count);
//
//        for (int i = 0; i < count; i++) {
//            ContentValues values = new ContentValues();
//            values.put(SectionEntry.COLUMN_SECTION_TITLE, viaplaySections.get(i).getTitle().toLowerCase());
//            // Temporarily store section title's information to detail title column because they couldn't be null
//            // Temporarily store section href url to description column because they couldn't be null
//            values.put(SectionEntry.COLUMN_SECTION_LONG_TITLE, viaplaySections.get(i).getTitle().toLowerCase());
//            values.put(SectionEntry.COLUMN_SECTION_DESCRIPTION, viaplaySections.get(i).getHref());
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
//
    @Override
    public void onClick(String sectionTitle) {
        showContentView();
        mDrawerLayout.closeDrawer(mRecyclerView);
        mTextViewTitleOnTheAppBar.setText(sectionTitle);
        mCurrentTitle = sectionTitle;
        // loadContentFromDatabase(sectionTitle);
    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//
//        return new CursorLoader(this, SectionEntry.CONTENT_URI, null, null,
//                null, null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        if (cursor != null && cursor.getCount() > 0) {
//
//            ArrayList<String> sectionTitlesString = new ArrayList<>();
//
//            for (int i = 0; i < cursor.getCount(); i++) {
//                cursor.moveToPosition(i);
//                String currentTitle = cursor.getString(cursor.getColumnIndex(SectionEntry.COLUMN_SECTION_TITLE));
//
//                if (!sectionTitlesString.contains(currentTitle)) {
//                    sectionTitlesString.add(currentTitle);
//                }
//                Log.i(LOG_TAG, "There are " + sectionTitlesString.size() + " different section titles available.");
//            }
//
//            showContentView();
//            mSectionAdapter.setUpTitleStringArray(sectionTitlesString);
//        } else {
//            showEmptyView();
//        }
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        mSectionAdapter.setUpTitleStringArray(null);
//    }
//
//    private void showEmptyView() {
//        mSwipeRefreshLayout.setRefreshing(false);
//        mContentView.setVisibility(View.INVISIBLE);
//        mEmptyView.setVisibility(View.VISIBLE);
//    }

    private void showContentView() {
        mSwipeRefreshLayout.setRefreshing(false);
        mContentView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.INVISIBLE);
    }

//    private boolean hasInternet() {
//        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
