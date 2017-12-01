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

//        // Current content survive while rotates the phone
//        if (savedInstanceState != null && savedInstanceState.containsKey(DETAIL_FRAGMENT_CURRENT_SECTION_NAME)) {
//            mClickedSectionName = savedInstanceState.getString(DETAIL_FRAGMENT_CURRENT_SECTION_NAME);
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

    private void initialScreenWithInternet() {
        ListFragment navigationDrawerFragment = new ListFragment();
        fragmentManager.beginTransaction()
                .add(R.id.navigation_drawer_container, navigationDrawerFragment)
                .commit();
    }

    private String getCurrentSectionNameOnTheAppBarText() {
        return titleOnTheBar.getText().toString();
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
        DetailFragment sectionDetailContentFragment = new DetailFragment();
        sectionDetailContentFragment.setmSectionNamesList(sectionNamesList);
        fragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, sectionDetailContentFragment)
                .commit();
    }

    private void populateSectionNameOnTheAppBar(String sectionName) {
        titleOnTheBar.setText(sectionName);
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
//            if (getCurrentSectionNameOnTheAppBarText() == null || getCurrentSectionNameOnTheAppBarText().toString().isEmpty()) {
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
//        if (getCurrentSectionNameOnTheAppBarText() != null) {
//            backgroundState = new Bundle();
//            backgroundState.putString(FORE_BACK_STATE_KEY, getCurrentSectionNameOnTheAppBarText().toString().toLowerCase());
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


    @Override
    public void onDetailDataBack() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
