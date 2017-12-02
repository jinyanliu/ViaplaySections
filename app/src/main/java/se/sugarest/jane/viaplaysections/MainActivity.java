package se.sugarest.jane.viaplaysections;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import se.sugarest.jane.viaplaysections.databinding.ActivityMainBinding;
import se.sugarest.jane.viaplaysections.idling_resource.SimpleIdlingResource;
import se.sugarest.jane.viaplaysections.ui.detail.DetailFragment;
import se.sugarest.jane.viaplaysections.ui.list.ListFragment;

import static se.sugarest.jane.viaplaysections.utilities.Constants.FORE_BACK_STATE_KEY;

/**
 * This is the main controller of the whole app.
 * It initiates the app.
 */
public class MainActivity extends AppCompatActivity implements ListFragment.OnDataBackListener, DetailFragment.OnDetailDataBackListener {

    private Bundle backgroundState;
    private Toast mToast;
    private FragmentManager fragmentManager;
    private ActivityMainBinding mBinding;

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

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Set up ToolBar
        setSupportActionBar(mBinding.appBar.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            initialScreenWithInternet();
        }

        setUpNavigationBar();

        setUpSwipeRefreshListener();

        // For Espresso Testing purpose
        getIdlingResource();
    }

    private void setUpSwipeRefreshListener() {
        mBinding.swipeRefresh.setOnRefreshListener(
                () -> {
                    if (!hasInternet()) {
                        mBinding.swipeRefresh.setRefreshing(false);
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
    }

    private void setUpNavigationBar() {
        mBinding.appBar.navigationMenu.setOnClickListener((View view) -> {
            if (mBinding.drawerLayout.isDrawerOpen(mBinding.navigationDrawerContainer)) {
                mBinding.drawerLayout.closeDrawer(mBinding.navigationDrawerContainer);
            } else if (!mBinding.drawerLayout.isDrawerOpen(mBinding.navigationDrawerContainer)) {
                mBinding.drawerLayout.openDrawer(mBinding.navigationDrawerContainer);
            }
        });
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
        return mBinding.appBar.titleOnTheAppBar.getText().toString().toLowerCase();
    }

    @Override
    public void onDataBack(ArrayList<String> sectionNamesList) {
        if (sectionNamesList != null && sectionNamesList.size() > 0) {
            mBinding.drawerLayout.closeDrawer(mBinding.navigationDrawerContainer);
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
        mBinding.appBar.titleOnTheAppBar.setText(sectionName);
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
        mBinding.swipeRefresh.setRefreshing(false);
        showDetailFragment();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    private void showProgressBar() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.detailFragmentContainer.setVisibility(View.INVISIBLE);
    }

    private void showDetailFragment() {
        mBinding.progressBar.setVisibility(View.INVISIBLE);
        mBinding.detailFragmentContainer.setVisibility(View.VISIBLE);
    }
}
