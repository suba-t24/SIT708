# SIT708

This repository contains Android applications developed as part of the **SIT708 coursework**. The projects demonstrate practical implementation of mobile application development concepts, including UI design, user interaction, data handling, and state management.

## Project Structure
SIT708/
|__ TravelCompanionApp/
|__ QuizApp/
|__ README.md

# 1. Travel Companion App

## Overview
The Travel Companion App is designed to assist users with common travel-related conversions. It provides a simple and efficient interface for converting currency, fuel/distance, and temperature values.

## Features
- Currency conversion (USD, AUD, EUR, JPY, GBP)  
- Fuel and distance conversion  
- Temperature conversion (Celsius, Fahrenheit, Kelvin)  
- Input validation and error handling  
- Clean and user-friendly UI  

## Key Concepts Implemented
- Activity-based navigation  
- Form handling and validation  
- Modular conversion logic  
- UI styling using drawable resources  

# 2. Quiz App

## Overview
The Quiz App is an interactive Android application that allows users to attempt a multiple-choice quiz and receive real-time feedback on their performance.

## Features
- Multiple-choice questions with four options  
- Visual feedback:  
  - Correct answers--> Green  
  - Incorrect answers --> Red  
- Progress tracking using ProgressBar  
- Final score display on completion  
- User name persistence across screens  
- Single dynamic button (Submit --> Next) for improved UX  
- Dark mode and light mode toggle  
- Theme persistence using SharedPreferences  

## Key Concepts Implemented
- Multi-activity architecture (Main, Quiz, Result)  
- Intent-based data passing between screens  
- State management (user input, score tracking)  
- Dynamic UI updates and event handling  
- Theme switching using AppCompatDelegate  
- Reusable UI styling with drawable resources  

## UI Design Approach

Both applications follow a clean and minimal UI design:

- Card-based layouts for better readability  
- Consistent spacing and alignment  
- Styled buttons using custom drawable XML  
- Visual feedback for user interactions  
- Support for both light and dark themes  

## Technologies Used

- Java  
- Android Studio  
- XML (UI Design)  
- SharedPreferences (data persistence)  
- Material Design principles  

## Testing Highlights

- Input validation for user entries  
- Correct/incorrect answer feedback  
- Progress tracking accuracy  
- Theme toggle persistence across screens  
- Navigation between activities  

# 3. Personal Event Planner App

## Overview
The Personal Event Planner App allows users to organise upcoming events, appointments, and reminders. It provides a structured interface using fragments, Room database persistence, and a bottom navigation layout.

## Features
- Add new events with Title, Category, Location, Date, and Time  
- View all upcoming events sorted automatically by date  
- Edit and update existing events  
- Delete events from the list  
- Input validation:
  - Title and Date cannot be empty  
  - Past dates are not accepted  
- User feedback using Snackbars/Toasts  
- Modern navigation using Jetpack Navigation Component  
- Local data persistence using Room  

## Key Concepts Implemented
- Single-Activity architecture with multiple Fragments  
- Bottom Navigation Bar linked to Navigation Component  
- RecyclerView with custom adapter for listing events  
- Room Database (Entity, DAO, ViewModel optional)  
- Live UI updates using LiveData (if implemented)  
- Input validation and error handling  

## Technologies Used
- Java  
- Android Studio  
- Room Persistence Library  
- Jetpack Navigation Component  
- XML for UI layout  

## Testing Highlights
- Events display correctly in date-sorted order  
- Validation prevents empty Title/Date fields and disallows past dates  
- Smooth navigation between fragments via Bottom Navigation and item selection  
- Room Database correctly persists added, edited, and deleted events  
- RecyclerView updates immediately on event changes  
- Snackbars/Toasts provide feedback for save, update, and delete actions  
- Events successfully reload after app restart 
 

## How to Run the Projects

1. Clone the repository: git clone https://github.com/your-username/SIT708.git

2. Open the project in Android Studio  

3. Build the project  

4. Run the app using:
- Android Emulator OR  
- Physical Android device  

## Notes

- All the applications are developed for academic purposes  
- Currency values in the Travel Companion App are approximate  
- Quiz questions are static and stored locally  
- Event list in Personal Event Planner App are stored locally and the event categories are static

## Author

Subathira Thinakaran  