# Sber Jukebox
This is a project that was presented on [Sberbank Javathon](https://sbergraduate.ru/javathon/) and took 3rd place. The service let's the facility's visitors to add there custom music tracks from VK to the facility's playlist for a small amount of money. Facility's administrators make their place more comfortable and cozy in return.

### How-to launch
Just execute run.sh script (you need to have Java 8 and Maven to be installed).

## Functionality
A customer comes to facility (restaurant or barber's, whatever) and starts chatting with VK bot. After specifying ID of facility and attaching a few tracks, the service adds these tracks to playlist queue of chosen facility. Facility's administrator has his browser opened on service's web-page which has audio player for playing the music in queue order.

## Features
* Adding tracks to playlists
* Mock payment handling
* Music autoplay

## Architecture
![alt text](https://github.com/ilyamogilin/sberjukebox/raw/master/Architecture.png "Architecture schema")

## Notes
* Though the payment processing classes are presented, this functionality is a mock for testing, but this is a matter of a couple of code lines to add real payment functionality.
