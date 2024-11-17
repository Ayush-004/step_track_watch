# Step Tracker


## Introduction

**Step Tracker** is a comprehensive fitness application designed for Android Wear devices. It monitors and records your daily step count using the device's built-in step counter sensor. The app provides real-time step tracking, personalized notes, and timely notifications to help you stay active and achieve your fitness goals. Additionally, it maintains detailed logs of events and user interactions for performance analysis and progress tracking.

## Features

- **Real-Time Step Counting:** Continuously tracks and displays your step count on the main interface.
- **Foreground Service:** Ensures uninterrupted step tracking and timely notifications.
- **Personal Notes:** Add, view, and manage personal notes related to your activity levels or fitness goals.
- **Scheduled Notifications:** Receive motivational or rest reminders based on your step count thresholds.
- **Event Logging:** Maintains a detailed CSV log of events and user interactions for analysis.
- **Persistent Operation:** Automatically restarts essential services upon device boot to ensure continuous tracking.


## Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/yourusername/StepTracker.git
   ```
2. **Open in Android Studio:**
   - Launch Android Studio.
   - Click on `File` > `Open` and navigate to the cloned repository folder.
3. **Build the Project:**
   - Let Android Studio download all necessary dependencies.
   - Click on `Build` > `Make Project` to compile the app.
4. **Run the App:**
   - Connect your Android Wear device or set up an emulator.
   - Click on `Run` > `Run 'app'` to install the app on your device.

## Usage

1. **Launch the App:**
   - Open the **Step Tracker** app on your Android Wear device.
2. **Grant Permissions:**
   - Upon first launch, the app will request necessary permissions such as Activity Recognition and Post Notifications.
   - The main screen displays your current step count in real-time.
4. **Manage Notes:**
   - Use the `Open Notes` button to add personal notes.
   - Use the `View All Notes` button to view and manage your saved notes.
5. **View Logs:**
   - Access detailed event logs by clicking the `Open Logs` button.
6. **Receive Notifications:**
   - Based on your activity, the app sends motivational or rest reminders to keep you on track.

## Permissions

The app requires the following permissions to function correctly:

- **Activity Recognition (`ACTIVITY_RECOGNITION`):** To monitor and count your steps.
- **Post Notifications (`POST_NOTIFICATIONS`):** To send timely notifications.
- **Vibrate (`VIBRATE`):** To provide haptic feedback with notifications.
- **Receive Boot Completed (`RECEIVE_BOOT_COMPLETED`):** To restart services after device reboot.
