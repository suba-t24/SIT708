# SIT708

This repository contains Android applications developed as part of the **SIT708 coursework**. The projects demonstrate practical implementation of mobile application development concepts, including UI design, user interaction, data handling, and state management.

## Project Structure
SIT708/
|__ TravelCompanionApp/
|__ QuizApp/
|__ PersonalEventPlannerApp/
|__ SportsNewsFeedApp/
|__ iStreamApp/
|__ LLMLearningAssistantApp/
|__ LostAndFoundApp/
|__ LLMChatBotApp/
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
 
# 4. Sports News Feed App

## Overview
The Sports News Feed App is a fragment-based Android application that displays sports news in a structured and interactive format using a single activity architecture.

## Features
- Horizontal RecyclerView for Featured Matches  
- Vertical RecyclerView for Latest Sports News  
- Search functionality to filter news by sport category or title  
- Detailed story view with image and description  
- Related stories based on sport category  
- Bookmark functionality using local storage  
- Separate Bookmarks screen to view saved stories  

## Key Concepts Implemented
- Single Activity architecture with multiple Fragments  
- Navigation Component for fragment transitions  
- RecyclerView with multiple adapters  
- Data passing using Bundles  
- Dynamic filtering logic  
- Local storage using SharedPreferences  

# 5. iStream App

## Overview
The iStream App is a multimedia Android application that allows users to log in, play YouTube videos, and manage a personalised playlist using local database storage.

## Features
- User Signup and Login system  
- Room database for storing user credentials  
- YouTube video playback using iFrame-based player integration  
- URL validation and error handling  
- Add video to playlist  
- User-specific playlist management  
- Clickable playlist items for playback  
- Logout functionality  

## Key Concepts Implemented
- Multi-activity architecture  
- Room Database (Entity, DAO, Database classes)  
- Background processing using Executors  
- YouTube video integration  
- Intent-based data passing between activities  
- Input validation and user authentication  

## UI Design Approach

All applications follow a clean and minimal UI design:

- Card-based layouts for better readability  
- Consistent spacing and alignment  
- Styled buttons using custom drawable XML  
- Visual feedback for user interactions  
- Support for modern UI practices  

## Technologies Used

- Java  
- Android Studio  
- XML (UI Design)  
- SharedPreferences (local storage)  
- Room Database  
- Material Design principles  

## Testing Highlights

- Input validation across all applications  
- Navigation between activities and fragments  
- RecyclerView data consistency  
- Database persistence (Room and SharedPreferences)  
- User authentication and session handling  
- Video playback validation and URL handling 

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
- Sports news data is static (dummy data)  
- YouTube playback may vary depending on video embedding permissions

# 6. LLM-Enhanced Learning Assistant App

## Overview
The LLM-Enhanced Learning Assistant App is an Android application that provides personalised learning support using AI-style features. It generates tasks based on user interests and includes learning utilities such as hints, explanations, summaries, flashcards, and study plans.
The app uses a Flask backend with optional Ollama integration. If the AI model is unavailable, fallback responses ensure smooth and reliable performance.

## Features
- User login and signup  
- Interest-based task generation  
- Quiz with multiple-choice questions  
- LLM-based features:
  - Generate hints  
  - Explain answers  
  - Summarise lessons  
  - Create flashcards  
  - Suggest a 7-day study plan  
- Prompt and response display  
- Loading and error handling  
- Fallback responses for reliability  

## Key Concepts Implemented
- Multi-activity Android architecture  
- Retrofit for API communication  
- Flask backend integration  
- Dynamic prompt generation  
- Asynchronous API handling  
- Fallback mechanism for LLM responses  

## Technologies Used
- Java  
- Android Studio  
- XML (UI Design)  
- Retrofit  
- Flask (Backend)  
- Ollama (Optional AI)  

## Running the Backend Locally

1. Navigate to backend folder: cd llm_learning_backend
2. Create virtual environment: python3 -m venv venv
3. Activate environment - macOS/Linux: source venv/bin/activate
4. Install dependencies: pip install -r requirements.txt
5. Run backend: python app.py
Backend will run at: http://127.0.0.1:5001

## Notes
- The app supports both AI-based and fallback responses  
- Ollama integration is optional and may be resource-intensive  
- Fallback ensures consistent performance during demo  

# 7. Lost and Found App

## Overview

The Lost and Found App is an Android application that helps users report lost or found items and reconnect them with their owners. It uses a local SQLite database to store and manage item listings.

## Features
- Create Lost or Found adverts
- Upload image for each advert (mandatory)
- Add item details (name, phone, description, date, location, category)
- View all adverts
- Filter adverts by category (Electronics, Pets, Wallets, etc.)
- View detailed item information
- Automatic date and time stamp for each post
- Remove advert once item is returned

## Key Concepts Implemented
- SQLite database (CRUD operations)
- Multi-activity architecture
- Intent-based navigation
- Image selection using Storage Access Framework
- Input validation
- Custom UI styling (blue theme, card layout)

## Technologies Used
- Java
- Android Studio
- XML (UI Design)
- SQLite

## Testing Highlights
- Form validation and error handling
- Image upload functionality
- Category filtering accuracy
- Database persistence and deletion
- Smooth navigation between screens

# 8. LLM ChatBot App

## Overview
The LLM ChatBot App is an Android chatbot application developed using Java in Android Studio. The app allows users to log in with a username, interact with an AI chatbot, and store chat history locally using SQLite.

The chatbot connects to a Flask backend integrated with the Ollama `phi3:mini` language model for generating responses.

## Features
- Username-based login
- Chatbot messaging interface
- User and bot message bubbles
- Message timestamps
- SQLite chat history persistence
- Retrofit API communication
- Flask backend integration
- Ollama `phi3:mini` support
- Custom blue and teal UI theme

## Technologies Used
- Java
- Android Studio
- XML
- SQLite
- Retrofit
- Flask
- Ollama
- Python

## Running the Backend Locally

1. Navigate to backend folder: cd llm_chatpot_backend
2. Create virtual environment: python3 -m venv venv
3. Activate environment - macOS/Linux: source venv/bin/activate
4. Install dependencies: pip install -r requirements.txt
5. Run backend: python app.py
Backend will run at: http://127.0.0.1:5001

## Notes
- The app supports both AI-based and fallback responses  
- Ollama integration is optional and may be resource-intensive  
- Fallback ensures consistent performance during demo 

## Author

Subathira Thinakaran  