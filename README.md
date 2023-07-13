# Imgur-App

## GOAL
Goal for this assignment is to create an app which allows users to search imgur for content.
This should be a single page application and you do not need to implement pagination.
By default the application should show the contents of the first page of Get Gallery. When
the user searches, show the first page of results for the search query using the following
API call Search Gallery. Make sure to also include a loading indicator as well as an empty
state in the case there are no search results. Your application should also gracefully handle
error states such as “no internet connection” .

### The app has following packages:
1. **data**: It contains all the data accessing and manipulating components.
2. **di**: Dependency providing classes using Dagger2.
3. **ui**: View classes along with their corresponding ViewModel.
4. **utils**: Utility classes.


### Libraries
- [Jetpack]
    - [Viewmodel]
    - [View Binding]
    - [Room]
    - [Navigation Components]
- [Retrofit]
- [Gson]
- [okhttp-logging-interceptor]
- [Coroutines]
- [Flow]
- [Truth]
- [Material Design]
- [Espresso]
- [mockK]
- [JUnit Rules]
- [Glide]
- [Hilt-Dagger]
- [Hilt-ViewModel]