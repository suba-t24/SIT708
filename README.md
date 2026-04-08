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

## How to Run the Projects

1. Clone the repository: git clone https://github.com/your-username/SIT708.git

2. Open the project in Android Studio  

3. Build the project  

4. Run the app using:
- Android Emulator OR  
- Physical Android device  

## Testing Highlights

- Input validation for user entries  
- Correct/incorrect answer feedback  
- Progress tracking accuracy  
- Theme toggle persistence across screens  
- Navigation between activities  

## Notes

- Both applications are developed for academic purposes  
- Currency values in the Travel Companion App are approximate  
- Quiz questions are static and stored locally  

## Author

Subathira Thinakaran  