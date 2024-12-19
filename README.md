# Food Delivery App

This Android app allows users to browse restaurants, view menus, order food, and track their delivery. The app features user authentication, restaurant listings, food ordering with quantity tracking, and order management with notifications. It also includes a map view for delivery tracking and a calendar feature for order scheduling.

## Functionality

The following **required** functionality is completed:

* [x] **Sign in Activity** – Users can sign in to the app using Firebase Authentication.
* [x] **Session Saving with Shared Preferences** – User session is saved using SharedPreferences, allowing the app to remember the user's login status.
* [x] **Implementation of Navigation Drawer** – A navigation drawer is implemented with links to different app sections (Home, Recent Orders, Profile, etc.).
* [x] **Recent Restaurants in Horizontal Scrollable manner in Home Activity** – Recent restaurants are displayed horizontally for quick access.
* [x] **All Restaurants in Vertical Scrollable manner in Home Activity** – A vertical list of all available restaurants.
* [x] **Food items with Quantity Tracker in Fragment 2 of Restaurant Activity** – Users can view and track food item quantities before placing an order.
* [x] **Swipe-Left Delete Feature in Checkout Activity** – Users can swipe to delete items from their cart in the checkout screen.
* [x] **Background Service for Checkout Activity** – A background service calculates the distance for delivery and notifies users accordingly.
* [x] **Calculate Distance for Checkout Activity** – Distance between user and restaurant is calculated to estimate delivery time.
* [x] **Generate Notifications** – Notifications are generated for order updates.
* [x] **MapView – Displaying the Marker** – A map shows the restaurant's location with a marker.
* [x] **Display Distance Route in the MapView** – The app displays the route between the user's location and the restaurant on the map.
* [x] **Saving Order Details to the Database** – Order details are saved to Firestore.
* [x] **Fetching Order Details from the Database in All Orders Activity** – A list of all previous orders can be viewed.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src="https://github.com/user-attachments/assets/e6017a6e-cad1-446a-a592-d36c041810c3" width=250/>

## Notes

### Challenges encountered while building the app:

* Integrating Firebase Authentication for login and sign-up.
* Managing background services and notifications for delivery updates.
* Working with the MapView to display accurate routes and locations.
* Organizing Firebase database in an efficient way.

## License

Copyright [2024] [Lucas Herbst]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and
limitations under the License.
