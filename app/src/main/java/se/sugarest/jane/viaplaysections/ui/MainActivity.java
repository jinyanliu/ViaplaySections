package se.sugarest.jane.viaplaysections.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.api.ViaplayClient;
import se.sugarest.jane.viaplaysections.data.SectionAdapter;
import se.sugarest.jane.viaplaysections.data.type.JSONResponse;
import se.sugarest.jane.viaplaysections.data.type.ViaplaySection;

import static se.sugarest.jane.viaplaysections.util.Constants.VIAPLAY_BASE_URL;

public class MainActivity extends AppCompatActivity implements SectionAdapter.SectionAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageButton mImageButtonDrawerMenu;
    private TextView mTextViewTitle;
    private TextView mTextViewDescription;

    private RecyclerView mRecyclerView;
    private SectionAdapter mSectionAdapter;

    private ArrayList<String> mFakeTitleData = new ArrayList<>();
    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);


        setUpRecyclerViewWithAdapter();
        setSectionTitleDataToRecyclerView();


        mTextViewTitle = findViewById(R.id.section_title);
        mTextViewDescription = findViewById(R.id.section_description);

        mImageButtonDrawerMenu = findViewById(R.id.button_navigation);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

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
                if (viaplaySections != null && !viaplaySections.isEmpty()) {
                    Log.i(LOG_TAG, "The list of ViaplaySections are: " + viaplaySections.toString());
                } else {

                }
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Failed to get photos list back.", t);
                fetchSectionDataFromDatabase();
            }
        });
    }

    private void fetchSectionDataFromDatabase() {
    }

    private void setSectionTitleDataToRecyclerView() {
        getSectionInformation();
        mSectionAdapter.setSectionData(mFakeTitleData);
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

    private void getSectionInformation() {
        mFakeTitleData.add("SERIER");
        mFakeTitleData.add("FILM");
        mFakeTitleData.add("SPORT");
        mFakeTitleData.add("BARN");
        mFakeTitleData.add("STORE");
    }


    @Override
    public void onClick(int position) {
        Log.i(LOG_TAG, "User clicked: " + mFakeTitleData.get(position));
        mDrawerLayout.closeDrawer(mRecyclerView);
    }
}
