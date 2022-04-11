# OpenWeather

<b>Persistence:</b>
The application loading persisted JSON objects executed in OnResume and displaying data. When data do not exist, weren't created or were deleted user will see "something" every time the app is active.
The application does not crash even if data were deleted by a user while running in the background.
The class AppData in package work. beno.filesystem loading and storing data.json - it contains data as is downloaded from WEB and config.json - holding timestamp of the last update

<b>Parse:</b>
The JsonParser class is located in work. beno.filesystem package has two functions getWeather and getConfig.
Each function returns parsed data into an object.

<b>2 hours update:</b>
Every time the app is in the foreground the current timestamp is compared with the timestamp from config. If the difference is greater than 2 hours the task continues with testing for WiFi connection to the internet.
If the time window is shorter than 2 hours the persisted data is displayed.

<b>Current location:</b>
An application trying to obtain geolocation only if:
Data are older than 2 hours, the device has a valid connection to the Internet, the location must be On, and the user must accept the permission. (application prompts the user)
Once a location is found, the application proceeds to download.

<b>HTTP request:</b>
Once all conditions above are met the obtained location is filled in the HTTP request with other parameters like language and metric units.
Then obtained request is parsed and displayed in the view.

<b>UI:</b>
The view Is done either programmatically in a separate class and XML (status bar).
The WeatherView class takes a Weather object with relevant information.

<b>Task/Thread:</b>
The task is running when the application is in the foreground and conditions for the HTTP request aren't met yet. It is designed to be running for the minimum time possible to minimise draining the battery.

<b>Staus Bar (Extra):</b>
To meet Ux the app has implemented a status bar in the top left corner to inform a user.

<b>Future release:</b>
For proof of work, the main class contains Location, Network state and Download function. I will separate them into Classes.
By HCI there should be a progress bar indicating activity while the device searching for a location
The app is designed for mobile phones in a “portrait” view. Landscape and tablet support will be benefiting other users
Images expressing weather like sunny, cloudy… would be an improvement
Users may enjoy choosing a different theme
For requesting data every 2+ hours by current location. Users should have an option to select between previous (home) and current location
User settings of units metric/imperial (C/F, km/miles) in option menu
Weather API supports several languages, those can be detected by mobile and set. Alternatively, can be selected by the user in the option menu
The way is Weather data downloaded we could have our own JSON object with extras that can settings i.e. season theme, wishing very best holidays, informing users about an occasion like temperature records, advice, special offers and coming soon

For security reasons, the API code is not included in the public project! It must be entered into the code before compiling and running.
