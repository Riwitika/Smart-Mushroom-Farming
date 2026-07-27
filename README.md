Smart Mushroom Farming

An IoT inspired, machine learning powered dashboard that predicts mushroom crop health and disease risk based on environmental parameters like temperature, humidity, substrate pH, ventilation, and light exposure.

Built as a final year college project, this system simulates how sensor data from a mushroom cultivation room can be fed into a machine learning model to give real time health diagnostics and actionable recommendations to farmers.

Overview

Mushroom farming is highly sensitive to environmental conditions. Even small deviations in temperature, humidity, or pH can lead to poor yield or fungal disease outbreaks. This project combines the following components.

1. IoT style sensor simulation for temperature, humidity, ventilation, light, and pH
2. A trained machine learning model that classifies crop health as Good, Moderate, or Bad
3. A Streamlit dashboard for real time monitoring and predictions
4. An Android app for on the go access
5. A backend service to support data handling and predictions

Features

1. Real time sensor parameter input including temperature, humidity, pH, ventilation, and light
2. One click health prediction using a trained machine learning model
3. Visual alignment charts showing how current readings compare to optimal ranges
4. Color coded health status with disease risk level
5. Actionable recommendations for farmers based on predictions
6. Companion Android app
7. Reset controls to quickly test different scenarios

Tech Stack

Frontend Dashboard: Streamlit
Machine Learning Model: Python, scikit learn, pickle based
Data Handling: Pandas, NumPy
Visualization: Matplotlib, Seaborn
Mobile App: Android, Kotlin or Java or Flutter
Backend: Python

Project Structure

Smart Mushroom Farming
backend folder containing backend service logic
data folder containing dataset used for training
models folder containing saved and trained machine learning models
smart mushroom farming android folder containing Android app source
app.py, the Streamlit dashboard and main user interface
predict.py, the prediction logic
preprocessing.py, the data preprocessing pipeline
train.py, the model training script
gitignore file

Installation and Setup

Step one, clone the repository.
git clone https://github.com/Riwitika/Smart Mushroom Farming.git
cd Smart Mushroom Farming

Step two, create a virtual environment. This is recommended but optional.
python -m venv venv
source venv/bin/activate on Mac or Linux
venv\Scripts\activate on Windows

Step three, install dependencies.
pip install streamlit pandas numpy matplotlib seaborn scikit learn

If a requirements file is added later, this step can be replaced with pip install -r requirements.txt

Step four, train the model.
python train.py

Step five, run the dashboard.
streamlit run app.py

The app will open in your browser at localhost port 8501.

How It Works

1. Adjust the environmental parameters such as temperature, humidity, pH, ventilation, and light using the sliders and dropdowns on the dashboard.
2. Click Predict Health.
3. The model analyzes the inputs and returns the overall health status as Good, Moderate, or Bad, along with the disease risk level and a recommended action plan for the farmer.
4. Visual charts show how the current readings align with optimal growing ranges.

Optimal Growing Parameters Reference

Temperature between 15 and 24 degrees Celsius
Humidity between 75 and 90 percent
Substrate pH between 6.0 and 7.5

Android App

The smart mushroom farming android folder contains a companion mobile app for monitoring crop health on the go. Refer to that folder for setup instructions specific to the app.

Future Scope

1. Integration with real ESP32 based IoT sensors for live data collection
2. Cloud based storage such as Firebase for historical tracking
3. Push notifications and alerts for abnormal readings
4. Expanded machine learning model with more disease classes
5. Multi language support for regional farmers

Author

Riwitika Gupta

License

This project is currently unlicensed. Feel free to reach out if you would like to use or contribute to it.
