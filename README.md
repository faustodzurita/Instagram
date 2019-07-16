# Project 3 - Instagram

**Instagram** is a photo sharing app using Parse as its backend.

Time spent: **30** hours spent in total

## User Stories

The following **required** functionality is completed:

- [X] User sees app icon in home screen.
- [X] User can sign up to create a new account using Parse authentication
- [X] User can log in and log out of his or her account
- [X] The current signed in user is persisted across app restarts
- [X] User can take a photo, add a caption, and post it to "Instagram"
- [X] User can view the last 20 posts submitted to "Instagram"
- [X] User can pull to refresh the last 20 posts submitted to "Instagram"
- [X] User can tap a post to view post details, including timestamp and caption.

The following **stretch** features are implemented:

- [X] Style the login page to look like the real Instagram login page.
- [X] Style the feed to look like the real Instagram feed.
- [X] User should switch between different tabs - viewing all posts (feed view), capture (camera and photo gallery view) and profile tabs (posts made) using a Bottom Navigation View.
- [X] User can load more posts once he or she reaches the bottom of the feed using infinite scrolling.
- [X] Show the username and creation time for each post
- [X] After the user submits a new post, show an indeterminate progress bar while the post is being uploaded to Parse
- User Profiles:
  - [X] Allow the logged in user to add a profile photo
  - [X] Display the profile photo with each post
  - [X] Tapping on a post's username or profile photo goes to that user's profile page
- [X] User can comment on a post and see all comments for each post in the post details screen.
- [X] User can like a post and see number of likes for each post in the post details screen.
- [X] Create a custom Camera View on your phone.
- [X] Run your app on your phone and use the camera to take the photo

The following **additional** features are implemented:

- [X] Profile images are circular
- [X] Like and comment data persist locally

Please list two areas of the assignment you'd like to **discuss further with your peers** during the next class (examples include better ways to implement something, how to extend your app in certain ways, etc):

1. How to extend app to include chat.
2. How to extend app to better master "cardview".

## Video Walkthrough

Here's a walkthrough of implemented user stories:

![Walkthrough](gif.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Credits

List an 3rd party libraries, icons, graphics, or other assets you used in your app.

- [Android Async Http Client](http://loopj.com/android-async-http/) - networking library


## Notes

- HomeActivity.java: implemented bottom navigation buttons with fragment views and a switch statement.

- LoginActivity.java: implemented a check to see if a session already exists or a login is required, used
listeners to validate login or launch the signup activity with an intent.

- PostDetailActivity.java: implemented a display of all the details of a post given its ID, used parcels
to obtain the post ID of the post that should be rendered, used Glide to render images, **implemented logic
to like and unlike an image storing that data both locally and on the remote database**, implemented logic
to comment on an image using textfield and button with listener that updates an array.

- SignupActivity.java: implemented a sign-up activity that stores data remotely using Parse, experimented with
textfield types such as password and phone number.

- UserDetailActivtiy.java: implemented a grid layout that displays all of the posts of a user using a
grid layout manager and a recycler view, used ParseQuery to query posts with specific constraints such as
only 20 posts and specifically from the user, logged errors.

- Post.java: created a Post model that interacts with the Parse API to get data, wrote method to convert
date formats from one form to another.

- ComposeFragment.java: implemented buttons with listeners to capture and post an image, used intents and activity
methods to launch the camera and preview the photo before posting in a box.

- ProfileFragment.java: implemented a grid layout that displays all of the posts of a user using a
grid layout manager and a recycler view, used ParseQuery to query posts with specific constraints such as
only 20 posts and specifically from the user, logged errors, implemented button to log out out of a session,
implemented button to change profile photo using built-in camera.

- TimelineFragment.java: implemented a timeline of posts with refresh swipe gesture and scroll listeners,
implemented logic to fetch new data from Parse upon reaching end of a set of posts using loadNextData method.

- EndlessRecyclerViewScrollListener.java: This was copied from online.

- ParseApplication.java: configured Parse to work sending a key.

- PostAdapter.java: implemented a Post adapter to bind post data to post views, used parcels and intents
to make each post clickable.

## License

    Copyright 2019 Fausto Zurita

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
