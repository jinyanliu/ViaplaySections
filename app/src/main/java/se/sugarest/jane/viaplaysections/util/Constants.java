package se.sugarest.jane.viaplaysections.util;

/**
 * Created by jane on 17-11-15.
 */
public class Constants {
    // Ending slash in base url is required by Retrofit
    public static final String VIAPLAY_BASE_URL = "https://content.viaplay.se/";
    public static final String VIAPLAY_API_END_POINT = "/androiddash-se";
    public static final String VIAPLAY_API_SLASH = "/";

    // Use with overriding onSaveInstanceState method in MainActivity to save current section view
    // when configuration change happens, e.g., from Portrait to Landscape.
    public static final String CONFIGURATION_KEY = "current_section";

    // Use with overriding onPause & onResume method in MainActivity to save current section view
    // when activity lifecycle state transition happens, e.g., from background to foreground.
    public static final String FORE_BACK_STATE_KEY = "current_title";

    // Use to recognize cursorLoader in MainActivity
    public static final int VIAPLAY_LOADER = 0;

    // Use with UriMatcher in SectionProvider to recognize data types, e.g., item type or directory
    // type.
    public static final int SECTIONS = 100;
    public static final int SECTIONS_ID = 101;
}
