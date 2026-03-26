# Travel Companion App

## Overview
The Travel Companion App is an Android application developed for Pass Task 2.1. It helps international travellers perform essential conversions in three categories:
- Currency
- Fuel/Distance
- Temperature

## Features
- User-friendly interface with spinners for selecting conversion category and units
- Input field for conversion value
- Convert button to perform calculations
- Output display for converted result
- Validation for empty and non-numeric inputs
- Identity conversion handling
- Negative value restriction for fuel/distance conversions

## Fixed Conversion Rates Used
### Currency
- 1 USD = 1.55 AUD
- 1 USD = 0.92 EUR
- 1 USD = 148.50 JPY
- 1 USD = 0.78 GBP

### Fuel/Distance
- 1 mpg = 0.425 km/L
- 1 Gallon (US) = 3.785 Liters
- 1 Nautical Mile = 1.852 Kilometers

### Temperature
- Celsius to Fahrenheit: F = (C × 1.8) + 32
- Fahrenheit to Celsius: C = (F − 32) / 1.8
- Celsius to Kelvin: K = C + 273.15

## Technologies Used
- Java
- Android Studio
- XML layouts

## Validation and Error Handling
- Prevents empty input
- Prevents non-numeric input
- Handles same-unit conversion
- Prevents negative values for fuel/distance conversions

## How to run
- Open the project in Android Studio
- Build and run the app
- Use an emulator or a physical Android device

## Author
Subathira Thinakaran
