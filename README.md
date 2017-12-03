A small app to navigate Viaplay’s content categories retrieved via API. 

# Functions

1. Presents the first section’s name, title and description when opening the app.
2. Navigates all categories from a navigation bar in the left side.
3. The name, title and description are displayed for each category, when clicking the category name from the navigation bar.
4. Supports both online and offline use. The data is stored locally after used in online mode. 
5. Swipe down to load latest data from API. 

# Implementation Highlights

1. Implemented with Android Architecture Components framework, e.g., using ViewModel, LiveData and Room. 
2. Used a single activity and two fragment classes for implementation. This increases modularity and re-usability.
3. Used dataBinding.
4. Used Retrofit HTTP client to perform API requests: e.g., getting sections list and a single section’s details.
5. Displays a user friendly image with message if no data is available, e.g., first time use under offline mode.
6. Current data on the screen survives configuration change, e.g., screen orientation. 
7. Ongoing background task (e.g., fetching data via network) survives configuration change.
8. Recently viewed category information survives activity lifecycle state transition, e.g., from background to foreground.
9. Retrieves and populates data when resuming from background under online mode. 
10. A loading indicator could be displayed if the data is not loaded immediately.
11. Put all the strings into strings.xml to support easy localization.
12. Created and reused custom styles to UI widgets, e.g., reusing TextView styles for labels  and contents.

# Tests

Used Espresso for instrumented tests.

1. Tests the corresponding UI widgets are displayed based on availability of data.
2. Tests that clicking on the menu image will open drawer navigation bar. 
3. Tests that clicking on single category name from navigation bar will populate the right content.
4. Tests the getItemCount() method in SectionAdapter class.
5. Added IdlingResources in MainActivity for Espresso.
6. Created two customized Matcher classes, e.g., a DrawableMatcher to check if an ImageView has the correct image.

# Design

1. Used drawer navigation to stay consistent with Viaplay’s Android app.
2. Showed one Sport category instead of three returned from the API. Because they have the same  content. 
3. Set colorPrimary to “#212D33” and colorPrimaryDark to “#141B1F” on purpose, with reference to Viaplay’s Android app. 