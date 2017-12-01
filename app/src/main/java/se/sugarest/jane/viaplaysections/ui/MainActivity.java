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
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ProgressBar;
import android.widget.FrameLayout;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.R;
import se.sugarest.jane.viaplaysections.idling_resource.SimpleIdlingResource;
import se.sugarest.jane.viaplaysections.ui.detail.DetailFragment;
import se.sugarest.jane.viaplaysections.ui.detail.DetailFragmentViewModel;
import se.sugarest.jane.viaplaysections.ui.list.ListFragment;

import static se.sugarest.jane.viaplaysections.utilities.Constants.FORE_BACK_STATE_KEY;

/**
 * This is the main controller of the whole app.
 * It initiates the app.
 */
public class MainActivity extends AppCompatActivity implements ListFragment.OnDataBackListener, DetailFragment.OnDetailDataBackListener {

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
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView titleOnTheBar;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;

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
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        titleOnTheBar = findViewById(R.id.title_on_the_app_bar);
        progressBar = findViewById(R.id.progress_bar);
        frameLayout = findViewById(R.id.detail_fragment_container);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            initialScreenWithInternet();
        }

        // Set up left drawer navigation
        menuImage.setOnClickListener((View view) -> {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.navigation_drawer_container))) {
                drawerLayout.closeDrawer(findViewById(R.id.navigation_drawer_container));
            } else if (!drawerLayout.isDrawerOpen(findViewById(R.id.navigation_drawer_container))) {
                drawerLayout.openDrawer(findViewById(R.id.navigation_drawer_container));
            }
        });


        // Set up swipe refresh function
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    if (!hasInternet()) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast = Toast.makeText(MainActivity.this, getString(R.string.toast_message_offline_cannot_refresh), Toast.LENGTH_SHORT);
                        mToast.show();
                    } else {
                        if (getCurrentSectionNameOnTheAppBarText().isEmpty()) {
                            initialScreenWithInternet();
                        } else {
                            ArrayList<String> currentList = new ArrayList<>();
                            currentList.add(getCurrentSectionNameOnTheAppBarText());
                            refreshOneSectionWithInternet(currentList);
                        }
                    }
                }
        );

        // Get the IdlingResource instance
        getIdlingResource();
    }

    private void initialScreenWithInternet() {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        if (hasInternet()) {
            showProgressBar();
        }
        ListFragment navigationDrawerFragment = new ListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.navigation_drawer_container, navigationDrawerFragment)
                .commit();
    }

    private String getCurrentSectionNameOnTheAppBarText() {
        return titleOnTheBar.getText().toString().toLowerCase();
    }

    @Override
    public void onDataBack(ArrayList<String> sectionNamesList) {
        if (sectionNamesList != null && sectionNamesList.size() > 0) {
            drawerLayout.closeDrawer(findViewById(R.id.navigation_drawer_container));
            populateSectionNameOnTheAppBar(sectionNamesList.get(0));

        }
        refreshOneSectionWithInternet(sectionNamesList);
    }

    private void refreshOneSectionWithInternet(ArrayList<String> sectionNamesList) {
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        if (hasInternet()) {
            showProgressBar();
        }
        DetailFragment sectionDetailContentFragment = new DetailFragment();
        sectionDetailContentFragment.setmSectionNamesList(sectionNamesList);
        fragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, sectionDetailContentFragment)
                .commit();
    }

    private void populateSectionNameOnTheAppBar(String sectionName) {
        titleOnTheBar.setText(sectionName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backgroundState != null
                && backgroundState.getString(FORE_BACK_STATE_KEY) != null
                && !backgroundState.getString(FORE_BACK_STATE_KEY).isEmpty()) {

            ArrayList<String> currentList = new ArrayList<>();
            currentList.add(backgroundState.getString(FORE_BACK_STATE_KEY));

            populateSectionNameOnTheAppBar(currentList.get(0));
            refreshOneSectionWithInternet(currentList);
        } else {
            if (getCurrentSectionNameOnTheAppBarText().isEmpty()) {
                initialScreenWithInternet();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!getCurrentSectionNameOnTheAppBarText().isEmpty()) {
            backgroundState = new Bundle();
            backgroundState.putString(FORE_BACK_STATE_KEY, getCurrentSectionNameOnTheAppBarText());
        }
    }

    private boolean hasInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected();
    }


    @Override
    public void onDetailDataBack() {
        swipeRefreshLayout.setRefreshing(false);
        showDetailFragment();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.INVISIBLE);
    }

    private void showDetailFragment() {
        progressBar.setVisibility(View.INVISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
    }
}
