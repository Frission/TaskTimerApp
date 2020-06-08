# Task Timer Android App

This is an Android application I have written in Kotlin while following my Android course. It lets users create tasks with simple descriptions, and time them like a stopwatch. The timing information also gets saved, which can be viewed later. In this project, I've implemented Android Room, LiveData, Repository pattern and Android ViewModel as an exercise.

<img src="https://i.imgur.com/e0TAZrN.png" width="360">

# How It Works

You can create new tasks by pressing the add button above, and entering the details. Tasks in the main page will be sorted by their sort order. Timing of a task can be started by long tapping a task. After you are done, you can long tap the same task again to stop timing.
Tasks can also be deleted from the main page with the delete icon. This will cause all related timing data to be deleted as well through an SQL TRIGGER.

<img src="https://i.imgur.com/5W8Tbbn.png" width="360">

Creating a task in landscape orientation is a bit different. Since there's a lot of room to the right side of our RecyclerView, the AddEditFragment will be placed here with a transaction instead of navigating away from the main page. When the fragment is open, the home button on the top right will show up, but it will close the AddEditFragment instead of navigating back. If the user rotates the phone back to portrait mode in this situation, the AddEditFragment will take up the entire screen again.

<img src="https://i.imgur.com/LobBhCd.png" width="480">

The same fragment is also used when editing the details of these tasks.

<img src="https://i.imgur.com/EhNgeg4.png" width="360"> 
<img src="https://i.imgur.com/3dIUrc3.png" width="480">

The timed tasks can be viewed on a separate acitivity. The first durations list is not filtered, but the "1" button on the top left lets the user filter the timings by day, or by week. This filtering is done using the current date and current week as a starting point, but this starting point can be changed from the options menu on the top left. It will spawn a date picker dialogue for you to choose. 

<img src="https://i.imgur.com/fKjTO7n.png" width="360">

Lastly, the timing data can be deleted via a date picker dialogue again. To delete old timing data, select "Delete old timings" from the options menu on the top right, and select the date you owuld like to delete the timings up to. All the timings that were recorded before this date will be deleted.

<img src="https://i.imgur.com/oEaZRh5.png" width="360">
