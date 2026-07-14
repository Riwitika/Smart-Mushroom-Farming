import os
import pickle
import pandas as pd
import numpy as np

# Load preprocessor and model
MODELS_DIR = "models"
PREPROCESSOR_PATH = os.path.join(MODELS_DIR, "preprocessor.pkl")
MODEL_PATH = os.path.join(MODELS_DIR, "best_model.pkl")

# Cache models to avoid reloading
_preprocessor = None
_model = None

def load_resources():
    global _preprocessor, _model
    if _preprocessor is None:
        if not os.path.exists(PREPROCESSOR_PATH):
            raise FileNotFoundError(f"Preprocessor not found at {PREPROCESSOR_PATH}. Run train.py first.")
        with open(PREPROCESSOR_PATH, "rb") as f:
            _preprocessor = pickle.load(f)
            
    if _model is None:
        if not os.path.exists(MODEL_PATH):
            raise FileNotFoundError(f"Model not found at {MODEL_PATH}. Run train.py first.")
        with open(MODEL_PATH, "rb") as f:
            _model = pickle.load(f)
            
    return _preprocessor, _model

def generate_recommendation(status, temp, hum, vent, light, ph):
    """
    Generates tailored advice based on predicted status and input values.
    """
    if status == 'Good':
        advices = []
        if temp < 14:
            advices.append("Temperature is cold (below 14°C) for optimal mushroom growth. Consider warming up to 18°C–24°C.")
        if hum < 70:
            advices.append("Humidity is dry (below 70%). Increase humidity to 80%–90% for mushroom pinning and fruiting.")
        if ph > 8.0:
            advices.append("pH is highly alkaline (above 8.0). Keep casing soil neutralized to 6.5–7.5 for best nutrient absorption.")
        if vent == 'low':
            advices.append("Ensure ventilation is increased occasionally to clear carbon dioxide build-up.")
        if light == 'high':
            advices.append("Dim light intensity to prevent pinheads and substrate from drying out.")
            
        if not advices:
            return "Optimal environment! Temperature, humidity, ventilation, and pH are perfectly balanced. Disease risk is extremely low."
        return "Disease risk is Low (Good), but growth conditions can be optimized: " + " ".join(advices)
        
    elif status == 'Moderate':
        advices = []
        if hum < 75:
            advices.append("Humidity is slightly low (below 75%). Run misting system to boost relative humidity.")
        if temp > 25:
            advices.append("Temperature is slightly warm (above 25°C). Turn on fans or cooling to reach 18°C–24°C.")
        if temp < 15:
            advices.append("Temperature is slightly cool (below 15°C). Increase room temperature.")
        if ph < 6.0:
            advices.append("Substrate pH is slightly acidic (below 6.0). Monitor closely to prevent green mold (Trichoderma).")
        if ph > 8.0:
            advices.append("Substrate pH is slightly alkaline (above 8.0). Check water source acidity.")
        if vent == 'low':
            advices.append("Ventilation is Low. Switch to Medium or High to control CO2 accumulation.")
            
        if not advices:
            return "Environmental parameters are stable with moderate disease risk. Maintain current monitoring."
        return "Moderate disease risk. Action required: " + " ".join(advices)
        
    else:  # Bad
        recs = []
        if temp > 30:
            recs.append("CRITICAL: Temperature exceeds 30°C! Turn on cooling systems immediately to avoid green mold and heat stress.")
        if temp < 10:
            recs.append("CRITICAL: Temperature is freezing (<10°C)! Turn on auxiliary heaters immediately.")
        if hum < 60:
            recs.append("CRITICAL: Humidity is extremely dry (<60%)! Increase humidifier output immediately to prevent crop failure.")
        if ph < 5.5:
            recs.append("CRITICAL: Substrate is highly acidic (pH < 5.5). Apply lime/gypsum water to boost pH and inhibit competing fungi.")
        if ph > 8.5:
            recs.append("CRITICAL: Substrate is highly alkaline (pH > 8.5). Mushroom mycelium cannot thrive. Correct substrate mixture.")
        if vent == 'low':
            recs.append("CRITICAL: CO2 accumulation. Increase ventilation immediately to prevent long, leggy stems and small caps.")
            
        if not recs:
            return "CRITICAL: Multiple factors are highly unfavorable. Inspect IoT sensor connections and manual ventilation settings immediately!"
        return "High Disease Risk! Action required: " + " ".join(recs)


def predict_mushroom_health(temperature, humidity, ventilation, light_intensity, ph):
    """
    Accepts raw features, preprocesses them, runs the ML model, and returns:
    (health_status, disease_risk_level, recommendation)
    """
    preprocessor, model = load_resources()
    
    # 1. Create a single-row DataFrame
    input_data = pd.DataFrame([{
        'temperature': float(temperature),
        'humidity': float(humidity),
        'ventilation': str(ventilation).lower(),
        'light_intensity': str(light_intensity).lower(),
        'ph': float(ph)
    }])
    
    # 2. Preprocess input
    preprocessed_data = preprocessor.transform(input_data)
    
    # 3. Extract the feature columns used in the ML model
    feature_cols = ['temperature_scaled', 'humidity_scaled', 'ventilation', 'light_intensity', 'ph_scaled']
    X_inference = preprocessed_data[feature_cols]
    
    # 4. Predict
    prediction_idx = int(model.predict(X_inference)[0])
    
    # 5. Map back to class name
    health_status = preprocessor.reverse_target_map[prediction_idx]
    
    # 6. Map to Risk
    risk_mapping = {'Good': 'Low Risk', 'Moderate': 'Medium Risk', 'Bad': 'High Risk'}
    disease_risk_level = risk_mapping[health_status]
    
    # 7. Generate recommendation
    recommendation = generate_recommendation(health_status, float(temperature), float(humidity), 
                                             str(ventilation).lower(), str(light_intensity).lower(), float(ph))
    
    return {
        "health_status": health_status,
        "disease_risk_level": disease_risk_level,
        "recommendation": recommendation
    }

if __name__ == "__main__":
    import argparse
    parser = argparse.ArgumentParser(description="Mushroom Health Inference CLI")
    parser.add_argument("--temp", type=float, default=20.0, help="Temperature in °C")
    parser.add_argument("--hum", type=float, default=85.0, help="Humidity in %")
    parser.add_argument("--vent", type=str, default="medium", choices=["low", "medium", "high"], help="Ventilation level")
    parser.add_argument("--light", type=str, default="medium", choices=["low", "medium", "high"], help="Light intensity level")
    parser.add_argument("--ph", type=float, default=6.8, help="pH level")
    
    args = parser.parse_args()
    
    res = predict_mushroom_health(args.temp, args.hum, args.vent, args.light, args.ph)
    print("\n--- INFERENCE RESULTS ---")
    print(f"Health Status      : {res['health_status']}")
    print(f"Disease Risk Level : {res['disease_risk_level']}")
    print(f"Recommendation     : {res['recommendation']}")
