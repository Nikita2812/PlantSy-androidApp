# PlantSy

PlantSy is an Android application for plant disease detection and community interaction, built with Jetpack Compose.

## Features

1. **Disease Detection Scan**: 
   - On-device scanning using CNN model
   - TensorFlow Kotlin SDK integration
   - Image capture with CameraX library

2. **Community Interaction**:
   - User-to-user communication focused on plant-related topics
   - Sharing of plant-related information and experiences
   - LSTM-based text classification to filter out non-plant-related conversations

3. **Plant Care Chatbot**:
   - AI-powered chatbot for plant-related queries
   - Instant answers to common plant care questions

## Tech Stack

- **Frontend**: 
  - Android (Kotlin)
  - Jetpack Compose for UI

- **Backend**:
  - FastAPI for API deployment
  - Firebase for database and authentication

- **Machine Learning**:
  - CNN model for disease detection
  - TensorFlow Kotlin SDK for on-device inference
  - LSTM model for text classification in community interactions

## Setup and Installation

PlantSy follows the standard setup for Android applications using Jetpack Compose. Here's a general guide:

1. Ensure you have Android Studio Arctic Fox (2020.3.1) or newer installed.
2. Clone this repository: `git clone https://github.com/yourusername/plantsy.git`
3. Open the project in Android Studio.
4. Sync the project with Gradle files.
5. Ensure you have the latest Android SDK, Build Tools, and Jetpack Compose dependencies installed via the SDK Manager.
6. Set up a Firebase project and add the `google-services.json` file to the app module.
7. Build and run the application on an emulator or physical device running Android 5.0 (API level 21) or higher.

For detailed instructions on setting up Jetpack Compose, refer to the [official documentation](https://developer.android.com/jetpack/compose/setup).

## Usage

1. **Disease Detection**: 
   - Tap the scan button on the home screen.
   - Point your camera at the plant leaf you want to analyze.
   - The app will process the image and display the detected disease, if any.

2. **Community Interaction**:
   - Navigate to the Community tab.
   - Browse existing posts or create a new one.
   - Engage in plant-related discussions with other users.

3. **Chatbot**:
   - Go to the Chatbot section.
   - Type your plant-related query.
   - Receive instant answers and advice.

## API Documentation

For detailed API documentation, please refer to our [FastAPI documentation](https://api.plantsy.com/docs).

## Contributing

We welcome contributions to PlantSy! Here are some ways you can contribute:

1. Report bugs or suggest features by opening an issue.
2. Improve documentation.
3. Submit pull requests with bug fixes or new features.

Please read our [Contributing Guidelines](CONTRIBUTING.md) for more details.

## License

PlantSy is released under the [GNU General Public License v2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html).

## Contact

For any queries regarding PlantSy, please open an issue on our GitHub repository or contact us at support@plantsy.com.
