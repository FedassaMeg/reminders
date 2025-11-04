# Task Reminder - Android App

A modern Android task management application built with idiomatic technologies and best practices.

## Features

- **Task Management**: Create, edit, and delete tasks with rich metadata
- **Subject Organization**: Group tasks by subjects/projects for better organization
- **Priority Levels**: Set task priorities (Low, Medium, High, Urgent)
- **Task Lifecycle**: Track tasks through common lifecycle states (TODO, In Progress, Completed, Cancelled)
- **Deadlines**: Set and track task deadlines with visual indicators
- **Notes**: Add detailed notes to each task
- **Filtering**: Filter tasks by subject or view all tasks together
- **Material Design 3**: Modern UI following Material Design 3 guidelines

## Technology Stack

### Modern Android Development
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (declarative UI)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Navigation Compose

### Jetpack Libraries
- **Room**: Local database with type-safe SQL queries
- **ViewModel**: UI-related data holder with lifecycle awareness
- **StateFlow**: Reactive state management
- **Kotlin Coroutines**: Asynchronous programming

### Design
- **Material Design 3**: Latest Material Design components and theming
- **Dynamic Color**: Supports Android 12+ dynamic theming

## Project Structure

```
app/src/main/java/com/example/taskreminder/
├── data/
│   ├── entity/          # Room entities (Task, Subject)
│   ├── dao/             # Data Access Objects
│   ├── model/           # Data models (TaskStatus, TaskPriority)
│   ├── repository/      # Repository pattern for data access
│   └── TaskReminderDatabase.kt
├── ui/
│   ├── components/      # Reusable UI components
│   ├── screens/         # Screen composables
│   ├── theme/           # Material Design theme configuration
│   └── viewmodel/       # ViewModels for UI logic
├── navigation/          # Navigation setup
├── MainActivity.kt
└── TaskReminderApplication.kt
```

## Key Components

### Data Layer
- **Task Entity**: Stores task information with support for priorities, statuses, deadlines, and notes
- **Subject Entity**: Organizes tasks into categories/projects with custom colors
- **Room Database**: Provides efficient local storage with foreign key relationships

### UI Layer
- **Task List Screen**: Displays all tasks with filtering options
- **Task Detail Screen**: Create/edit tasks with all attributes
- **Subject List Screen**: Manage subjects/categories
- **Subject Detail Screen**: Create/edit subjects with color customization

### Features Implementation
- **Priority System**: Four priority levels with color-coded badges
- **Status Tracking**: Complete task lifecycle management
- **Subject Filtering**: View tasks by subject or view all together
- **Deadline Management**: Visual deadline indicators with overdue highlighting

## Building the App

### Requirements
- Android Studio Hedgehog | 2023.1.1 or newer
- Android SDK 34
- Minimum SDK 26 (Android 8.0)
- JDK 17

### Build Steps
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

### Gradle Commands
```bash
# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Install on connected device
./gradlew installDebug
```

## Architecture Highlights

### MVVM Pattern
- **Model**: Room entities and database
- **View**: Jetpack Compose UI
- **ViewModel**: Business logic and state management

### Repository Pattern
- Abstracts data sources from ViewModels
- Provides clean API for data operations
- Enables easy testing and maintainability

### Reactive Programming
- StateFlow for reactive UI updates
- Coroutines for asynchronous operations
- Flow-based database queries

## Future Enhancements

Potential features for future versions:
- Notifications/Reminders
- Task search functionality
- Task sorting options
- Data export/import
- Dark theme toggle
- Widget support
- Task statistics and analytics
- Recurring tasks
- Task attachments

## License

This project is for educational and demonstration purposes.
