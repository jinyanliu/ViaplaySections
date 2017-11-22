# ViaplaySections
A small app navigates between different sections from Viaplay API.

README



Functions



Presents the first section’s title and description when the user opens the app. I choose not to show the list of all sections on the main screen, because I prefer the drawer navigation, i.e., the left side bar.   

The user is able to navigate to all sections from the left side drawer navigation, which is implemented with RecyclerView.

The title and description are displayed for each section, which the user chooses from the drawer navigation. The implementation uses a SectionAdapterOnClickHandler. It listens to the clicking event and populates corresponding title and description for the selected section. 

The app supports both online and offline modes. It stores necessary data to database when opened for the first time, i.e., right after new installation. If there is no internet connection for the first time use after new installation, then a text message will be displayed, e.g., Please enable internet at first use! When working at online mode, the app always downloads the latest data from Viaplay API and replace the data in the database. When working at offline mode, it reads the data from the database. 



Performance



Current viewing page information survives configuration change, e.g., from portrait view to landscape view. 

Previous viewing page information survives activity lifecycle state transition, e.g., from background to foreground.

If it’s offline mode when used for the first time after new installation, then the app will retrieve & populate data when internet is enabled and the app is resumed from background. Or if the app is at foreground, then user can swipe the screen to load data.

A Toast message will pop up while waiting for the data comes back from Viaplay API, saying “Data is loading…”. While there is internet, I try to get newest data from Viaplay API, even though this app has old data saved in the database. 

Put all the strings into string.xml file with the attribute: translatable=“true” or “false”.  Each of them has comments, e.g. explaining “ What is this string for? ”, “ When is it presented to the user? ” and “Where is this in the layout? ” So if I need this app to be localized, the translator can easily translate the strings without changing Java code. 

Created and reused custom styles to UI widgets, e.g., reusing TextViews for label  and content on main screen. 

Used Retrofit http client to perform API requests: e.g., getting sections list and a single section’s details.



Tests



Most of the tests use Espresso.

Test all UI widgets on the main screen. Because my app has a dynamic main screen, it is important to check that every widget exists. 

Test static views, e.g., Viaplay logo ImageView and title label TextView, have the correct images or texts. 

Created 2 customized Matcher classes. One is DrawableMatcher, which checks if an ImageView has a correct image. The other is IgnoreCaseTextMatcher class, which checks if a TextView has the correct text, case-insensitive.

Test the menu image on the left side of the app bar. Performs clicking on the menu image, which opens up drawer navigation RecyclerView. Because this is a widget that the user can interact with.

Test getItemCount() method in SectionAdapter class. It is important because it indicates how many different section items this app gets back from Viaplay API or database. This is the number of section items displayed on the drawer navigation RecyclerView.

Test performing clicking on one of the section items. The app should populate the right page on the main screen. Because this is a widget that the user can interact with.

Implemented IdlingResource to control Espresso while the app is doing long-running operations, e.g., retrieving data from REST API during testing.




Design



Choose drawer navigation instead of others, e.g., tab navigation. It is because I visited Viaplay’s Android app on Google Play Store and wanted to stay consistent with the design. Besides drawer navigation saves up activities and the space on one activity.

Shows 5 different sections on the drawer navigation RecyclerView, even though the returned sections’ number from Viaplay API is 7. That is because there are 3 sport sections with the same title and description. Besides, I looked up at: https://viaplay.se/serier, and found there was only 1 sport tab on the website. So I decided to show 5 sections. 

Set colorPrimary to “#212D33” and colorPrimaryDark to “#141B1F” on purpose. I don't know if they are exactly the same colors that Viaplay used on android app. I tried to get them from Viaplay’s Android app screenshots on Google Play Store.  
