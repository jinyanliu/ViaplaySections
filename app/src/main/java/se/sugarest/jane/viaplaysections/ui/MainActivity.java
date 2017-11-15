package se.sugarest.jane.viaplaysections.ui;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.api.ViaplayClient;
import se.sugarest.jane.viaplaysections.data.SectionAdapter;
import se.sugarest.jane.viaplaysections.data.database.SectionContract.SectionEntry;
import se.sugarest.jane.viaplaysections.data.type.JSONResponse;
import se.sugarest.jane.viaplaysections.data.type.SingleJSONResponse;
import se.sugarest.jane.viaplaysections.data.type.ViaplaySection;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_BASE_URL;
import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_LOADER;

public class MainActivity extends AppCompatActivity implements SectionAdapter.SectionAdapterOnClickHandler,
        android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private ImageButton mImageButtonDrawerMenu;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;
    private TextView mTextViewTitleOnTheAppBar;

    private RecyclerView mRecyclerView;
    private SectionAdapter mSectionAdapter;

    private Toast mToast;
    private ArrayList<String> mSectionTitles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mTextViewTitle = findViewById(R.id.section_title);
        mTextViewDescription = findViewById(R.id.section_description);
        mTextViewTitleOnTheAppBar = findViewById(R.id.title_on_the_app_bar);
        mImageButtonDrawerMenu = findViewById(R.id.button_navigation);

        setUpRecyclerViewWithAdapter();

        mImageButtonDrawerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                    mDrawerLayout.closeDrawer(mRecyclerView);
                } else if (!mDrawerLayout.isDrawerOpen(mRecyclerView)) {
                    mDrawerLayout.openDrawer(mRecyclerView);
                }
            }
        });

        sendNetworkRequestGet();

    }

    private void initLoader() {
        getLoaderManager().initLoader(VIAPLAY_LOADER, null, MainActivity.this);
    }

    private void sendNetworkRequestGet() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(VIAPLAY_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();
        ViaplayClient client = retrofit.create(ViaplayClient.class);
        Call<JSONResponse> call = client.getSections();

        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                Log.i(LOG_TAG, "GET response success: Complete url to request is: "
                        + response.raw().request().url().toString()
                        + "\nresponse.body().toString == " + response.body().toString());

                List<ViaplaySection> viaplaySections = response.body().getLinks().getViaplaySections();

                setUpFirstSectionState(viaplaySections);

                if (viaplaySections != null && !viaplaySections.isEmpty()) {
                    Log.i(LOG_TAG, "The list of ViaplaySections are: " + viaplaySections.toString());
                    putSectionDataIntoDatabase(viaplaySections);
                } else {
                    initLoader();
                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Failed to get photos list back.", t);
                initLoader();
            }
        });
    }

    private void sendNetworkRequestGetOneSection(final String currentTitle) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(VIAPLAY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        ViaplayClient client = retrofit.create(ViaplayClient.class);
        Call<SingleJSONResponse> call = client.getOneSectionByTitle(currentTitle);
        call.enqueue(new Callback<SingleJSONResponse>() {
            @Override
            public void onResponse(Call<SingleJSONResponse> call, Response<SingleJSONResponse> response) {
                Log.i(LOG_TAG, "GET ONE response success: Complete url to request is: "
                        + response.raw().request().url().toString()
                        + "\nresponse.code() == " + response.code());
                String currentLongTitle = response.body().getTitle();
                String currentDescription = response.body().getDescription();

                mTextViewTitle.setText(currentLongTitle);
                mTextViewDescription.setText(currentDescription);

                ContentValues values = new ContentValues();
                values.put(SectionEntry.COLUMN_SECTION_LONG_TITLE, currentLongTitle);
                values.put(SectionEntry.COLUMN_SECTION_DESCRIPTION, currentDescription);
                String selection = SectionEntry.COLUMN_SECTION_TITLE;
                String[] selectionArgs = {currentTitle};
                int rowsUpdated = getContentResolver().update(SectionEntry.CONTENT_URI, values, selection, selectionArgs);
                if (rowsUpdated > 0) {
                    Log.i(LOG_TAG, "Update long title and description information for " + currentTitle + " section is successful.");
                }
            }

            @Override
            public void onFailure(Call<SingleJSONResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Failed to get section data back with title: " + currentTitle, t);
            }
        });
    }

    private void setUpFirstSectionState(List<ViaplaySection> viaplaySections) {
        ViaplaySection currentViaplaySection = viaplaySections.get(0);
        String currentViaplaySectionTitle = currentViaplaySection.getTitle();
        mTextViewTitleOnTheAppBar.setText(currentViaplaySectionTitle);
        sendNetworkRequestGetOneSection(currentViaplaySectionTitle.toLowerCase());
    }

    private void putSectionDataIntoDatabase(List<ViaplaySection> viaplaySections) {
        cleanSectionTableFromDatabase();
        int count = viaplaySections.size();
        Vector<ContentValues> cVVector = new Vector<>(count);
        for (int i = 0; i < count; i++) {
            ContentValues values = new ContentValues();
            values.put(SectionEntry.COLUMN_SECTION_TITLE, viaplaySections.get(i).getTitle().toLowerCase());
            values.put(SectionEntry.COLUMN_HREF_URL, viaplaySections.get(i).getHref());
            // Temporarily store section short title's information to long title
            // Temporarily store section href url to description
            values.put(SectionEntry.COLUMN_SECTION_LONG_TITLE, viaplaySections.get(i).getTitle());
            values.put(SectionEntry.COLUMN_SECTION_DESCRIPTION, viaplaySections.get(i).getHref());

            cVVector.add(values);
        }

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int bulkInsertRows = getContentResolver().bulkInsert(
                    SectionEntry.CONTENT_URI,
                    cvArray);
            if (bulkInsertRows == cVVector.size()) {
                Log.i(LOG_TAG, "bulkInsert into SectionEntry successful.");
            } else {
                Log.e(LOG_TAG, "bulkInsert into SectionEntry unsuccessful. The number of bulkInsertRows is: "
                        + bulkInsertRows + " and the number of data size is: " + cVVector.size());
            }
        }
        initLoader();
    }

    private void cleanSectionTableFromDatabase() {
        getContentResolver().delete(SectionEntry.CONTENT_URI,
                null,
                null);
        Log.i(LOG_TAG, "section table in section database is been cleaned.");
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

    @Override
    public void onClick(String sectionTitle) {
        mDrawerLayout.closeDrawer(mRecyclerView);
        mTextViewTitleOnTheAppBar.setText(sectionTitle);
        sendNetworkRequestGetOneSection(sectionTitle);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                SectionEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            mSectionAdapter.swapCursor(cursor);
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(MainActivity.this, getString(R.string.toast_message_no_data_from_database), Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.BOTTOM, 0, 0);
            mToast.show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mSectionAdapter.swapCursor(null);
    }
}
