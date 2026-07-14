import streamlit as st
import pandas as pd
import numpy as np
import os
import pickle
import matplotlib.pyplot as plt
import seaborn as sns
from predict import predict_mushroom_health

# Page configuration
st.set_page_config(
    page_title="Smart Mushroom Farming Health Prediction",
    page_icon="🍄",
    layout="wide",
    initial_sidebar_state="expanded"
)

# Custom CSS for Premium Design & Theme
st.markdown("""
<style>
    /* Theme Font and Backgrounds */
    @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700&display=swap');
    
    html, body, [class*="css"] {
        font-family: 'Outfit', sans-serif;
    }
    
    /* Main Background and Earthy Theme Colors */
    .stApp {
        background-color: #f7f9f6;
    }
    
    /* Header styling */
    .header-container {
        background: linear-gradient(135deg, #1e3f20 0%, #2e5a36 100%);
        padding: 2.5rem;
        border-radius: 16px;
        color: white;
        text-align: center;
        margin-bottom: 2rem;
        box-shadow: 0 8px 32px 0 rgba(31, 63, 32, 0.15);
        border: 1px solid rgba(255, 255, 255, 0.1);
    }
    .header-title {
        font-size: 2.5rem;
        font-weight: 700;
        margin: 0;
        letter-spacing: -0.5px;
    }
    .header-subtitle {
        font-size: 1.1rem;
        font-weight: 300;
        margin-top: 0.5rem;
        opacity: 0.9;
    }
    
    /* Custom Card Design */
    .metric-card {
        background-color: white;
        border-radius: 12px;
        padding: 1.2rem;
        box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.03);
        border: 1px solid #e2ebd5;
        text-align: center;
        transition: transform 0.2s ease, box-shadow 0.2s ease;
    }
    .metric-card:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 24px 0 rgba(0, 0, 0, 0.08);
    }
    .metric-label {
        font-size: 0.9rem;
        font-weight: 600;
        color: #7d8c6d;
        text-transform: uppercase;
        margin-bottom: 0.4rem;
    }
    .metric-value {
        font-size: 1.8rem;
        font-weight: 700;
        color: #2e5a36;
    }
    
    /* Results cards color-coding */
    .result-card {
        border-radius: 16px;
        padding: 2rem;
        color: white;
        box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.1);
        margin-top: 1rem;
        border: 1px solid rgba(255, 255, 255, 0.1);
    }
    .result-good {
        background: linear-gradient(135deg, #1b5e20 0%, #2e7d32 100%);
    }
    .result-moderate {
        background: linear-gradient(135deg, #f57f17 0%, #f9a825 100%);
    }
    .result-bad {
        background: linear-gradient(135deg, #b71c1c 0%, #c62828 100%);
    }
    
    /* Prediction highlights */
    .status-text {
        font-size: 2.2rem;
        font-weight: 700;
        margin-bottom: 0.2rem;
    }
    .risk-text {
        font-size: 1.1rem;
        text-transform: uppercase;
        font-weight: 600;
        letter-spacing: 1px;
        opacity: 0.9;
        margin-bottom: 1.5rem;
    }
    .rec-box {
        background-color: rgba(255, 255, 255, 0.15);
        padding: 1.2rem;
        border-radius: 8px;
        font-size: 1rem;
        line-height: 1.5;
        border-left: 5px solid rgba(255, 255, 255, 0.5);
    }
    
    /* Sidebar styling adjustment */
    .css-12w0qpk {
        background-color: #f1f5eb;
    }
    
    /* Button styles */
    .stButton>button {
        border-radius: 8px;
        padding: 0.6rem 2rem;
        font-weight: 600;
        transition: all 0.2s;
    }
    
    /* Predict Button (Earthy Forest Green) */
    .predict-btn button {
        background-color: #2e5a36 !important;
        color: white !important;
        border: none !important;
        width: 100%;
    }
    .predict-btn button:hover {
        background-color: #1e3f20 !important;
        transform: scale(1.02);
    }
    
    /* Reset Button (Slate/Neutral) */
    .reset-btn button {
        background-color: #d7e0cd !important;
        color: #4b583f !important;
        border: none !important;
        width: 100%;
    }
    .reset-btn button:hover {
        background-color: #c4ceb7 !important;
    }
</style>
""", unsafe_allow_html=True)

# ----------------------------------------------------
# Session State Initialization (for Reset functionality)
# ----------------------------------------------------
if "temp_val" not in st.session_state:
    st.session_state.temp_val = 20.0
if "hum_val" not in st.session_state:
    st.session_state.hum_val = 85.0
if "ph_val" not in st.session_state:
    st.session_state.ph_val = 6.8
if "vent_val" not in st.session_state:
    st.session_state.vent_val = "Medium"
if "light_val" not in st.session_state:
    st.session_state.light_val = "Medium"
if "predict_clicked" not in st.session_state:
    st.session_state.predict_clicked = False

def reset_parameters():
    st.session_state.temp_val = 20.0
    st.session_state.hum_val = 85.0
    st.session_state.ph_val = 6.8
    st.session_state.vent_val = "Medium"
    st.session_state.light_val = "Medium"
    st.session_state.predict_clicked = False

# ----------------------------------------------------
# Main Layout - Header
# ----------------------------------------------------
st.markdown("""
    <div class="header-container">
        <h1 class="header-title">🍄 Smart Mushroom Farming</h1>
        <div class="header-subtitle">Crop Health Diagnostics & Eco-System Prediction System</div>
    </div>
""", unsafe_allow_html=True)

# ----------------------------------------------------
# Layout Structure: Two main columns (Inputs and Results)
# ----------------------------------------------------
col_input, col_display = st.columns([1, 2], gap="large")

with col_input:
    st.subheader("🛠️ Sensor & Parameter Controls")
    st.markdown("Use the controls below to configure or simulate physical IoT sensor readings in the cultivation room:")
    
    # Input controls
    # We bind value to session state and use callbacks/key bindings
    temperature = st.slider(
        "🌡️ Temperature (°C)", 
        min_value=5.0, 
        max_value=50.0, 
        step=0.1, 
        key="temp_val"
    )
    
    humidity = st.slider(
        "💧 Relative Humidity (%)", 
        min_value=10.0, 
        max_value=100.0, 
        step=1.0, 
        key="hum_val"
    )
    
    ph = st.number_input(
        "🧪 Substrate pH Level", 
        min_value=3.0, 
        max_value=11.0, 
        step=0.1, 
        key="ph_val"
    )
    
    ventilation = st.selectbox(
        "🌀 Ventilation Exchange Speed",
        options=["Low", "Medium", "High"],
        key="vent_val"
    )
    
    light_intensity = st.selectbox(
        "☀️ Light Exposure Intensity",
        options=["Low", "Medium", "High"],
        key="light_val"
    )
    
    st.markdown("<br>", unsafe_allow_html=True)
    
    # Action Buttons side by side
    btn_col1, btn_col2 = st.columns(2)
    with btn_col1:
        st.markdown('<div class="predict-btn">', unsafe_allow_html=True)
        predict_click = st.button("🔍 Predict Health", use_container_width=True)
        st.markdown('</div>', unsafe_allow_html=True)
    with btn_col2:
        st.markdown('<div class="reset-btn">', unsafe_allow_html=True)
        reset_click = st.button("🔄 Reset Parameters", on_click=reset_parameters, use_container_width=True)
        st.markdown('</div>', unsafe_allow_html=True)

    if predict_click:
        st.session_state.predict_clicked = True

with col_display:
    st.subheader("📊 IoT Monitoring Panel")
    
    # Display selected values in stylized cards
    card_col1, card_col2, card_col3 = st.columns(3)
    with card_col1:
        st.markdown(f"""
            <div class="metric-card">
                <div class="metric-label">🌡️ Temperature</div>
                <div class="metric-value">{temperature}°C</div>
            </div>
        """, unsafe_allow_html=True)
    with card_col2:
        st.markdown(f"""
            <div class="metric-card">
                <div class="metric-label">💧 Humidity</div>
                <div class="metric-value">{humidity}%</div>
            </div>
        """, unsafe_allow_html=True)
    with card_col3:
        st.markdown(f"""
            <div class="metric-card">
                <div class="metric-label">🧪 Substrate pH</div>
                <div class="metric-value">{ph}</div>
            </div>
        """, unsafe_allow_html=True)
        
    st.markdown("<br>", unsafe_allow_html=True)
    
    # Display details of selected states
    card_col4, card_col5 = st.columns(2)
    with card_col4:
        st.markdown(f"""
            <div class="metric-card" style="padding: 0.8rem;">
                <div class="metric-label">🌀 Ventilation Speed</div>
                <div class="metric-value" style="font-size: 1.4rem;">{ventilation}</div>
            </div>
        """, unsafe_allow_html=True)
    with card_col5:
        st.markdown(f"""
            <div class="metric-card" style="padding: 0.8rem;">
                <div class="metric-label">☀️ Light Exposure</div>
                <div class="metric-value" style="font-size: 1.4rem;">{light_intensity}</div>
            </div>
        """, unsafe_allow_html=True)

    st.markdown("<hr style='border-top: 1px solid #dcdfd8;'>", unsafe_allow_html=True)

    # ----------------------------------------------------
    # Prediction Output Block
    # ----------------------------------------------------
    if st.session_state.predict_clicked:
        # Load resources and predict
        try:
            results = predict_mushroom_health(
                temperature=temperature,
                humidity=humidity,
                ventilation=ventilation,
                light_intensity=light_intensity,
                ph=ph
            )
            
            status = results['health_status']
            risk = results['disease_risk_level']
            rec = results['recommendation']
            
            # Map CSS class and display style
            if status == 'Good':
                card_class = "result-good"
                icon = "🟢"
                bg_color = "#e8f5e9"
                border_color = "#2e7d32"
                text_color = "#1b5e20"
            elif status == 'Moderate':
                card_class = "result-moderate"
                icon = "🟡"
                bg_color = "#fffde7"
                border_color = "#f9a825"
                text_color = "#f57f17"
            else: # Bad
                card_class = "result-bad"
                icon = "🔴"
                bg_color = "#ffebee"
                border_color = "#c62828"
                text_color = "#b71c1c"
                
            st.markdown(f"""
                <div class="result-card {card_class}">
                    <div style="font-size: 0.9rem; font-weight: 600; text-transform: uppercase; opacity: 0.85; margin-bottom: 0.3rem;">Diagnostic Results</div>
                    <div class="status-text">{icon} Mushroom Health: {status}</div>
                    <div class="risk-text">Pathogen Infection Threat: {risk}</div>
                    <div class="rec-box">
                        <strong>👨‍🌾 Action Plan for Farmer:</strong><br>{rec}
                    </div>
                </div>
            """, unsafe_allow_html=True)
            
            # Draw parameter ranges trend chart
            st.markdown("<br><h4>📈 Optimal Parameter Range Alignment</h4>", unsafe_allow_html=True)
            
            # Simple visualization using a horizontal bar charts
            # Optimal values reference:
            # Temperature: 15-24
            # Humidity: 75-90
            # pH: 6.0-7.5
            
            fig, ax = plt.subplots(3, 1, figsize=(10, 4.5))
            
            # Temperature plot
            ax[0].axvspan(15, 24, alpha=0.3, color='green', label='Optimal (15-24°C)')
            ax[0].axvspan(5, 15, alpha=0.1, color='orange')
            ax[0].axvspan(24, 50, alpha=0.1, color='orange')
            ax[0].plot([temperature], [0], marker='o', markersize=12, color=border_color)
            ax[0].set_xlim(5, 50)
            ax[0].set_yticks([])
            ax[0].set_title(f"Temperature Alignment (Current: {temperature}°C)", fontsize=10, loc='left')
            ax[0].grid(False)
            
            # Humidity plot
            ax[1].axvspan(75, 90, alpha=0.3, color='green', label='Optimal (75-90%)')
            ax[1].axvspan(10, 75, alpha=0.1, color='orange')
            ax[1].axvspan(90, 100, alpha=0.1, color='orange')
            ax[1].plot([humidity], [0], marker='o', markersize=12, color=border_color)
            ax[1].set_xlim(10, 100)
            ax[1].set_yticks([])
            ax[1].set_title(f"Humidity Alignment (Current: {humidity}%)", fontsize=10, loc='left')
            ax[1].grid(False)
            
            # pH plot
            ax[2].axvspan(6.0, 7.5, alpha=0.3, color='green', label='Optimal (6.0-7.5)')
            ax[2].axvspan(3.0, 6.0, alpha=0.1, color='red')
            ax[2].axvspan(7.5, 11.0, alpha=0.1, color='red')
            ax[2].plot([ph], [0], marker='o', markersize=12, color=border_color)
            ax[2].set_xlim(3, 11)
            ax[2].set_yticks([])
            ax[2].set_title(f"Substrate pH Alignment (Current: {ph})", fontsize=10, loc='left')
            ax[2].grid(False)
            
            plt.tight_layout()
            st.pyplot(fig)
            
        except Exception as e:
            st.error(f"Prediction Error: {str(e)}")
            st.info("Make sure you run train.py to generate models before loading predictions.")
    else:
        # Default panel when predict hasn't been clicked
        st.markdown(f"""
            <div style="background-color: #ebf0e4; border-radius: 12px; padding: 2.5rem; text-align: center; border: 1px dashed #7d8c6d; color: #4b583f; margin-top: 1rem;">
                <span style="font-size: 3rem;">🍄</span>
                <h3 style="margin-top: 0.5rem; color: #2e5a36;">Ready for Diagnosis</h3>
                <p>Adjust environmental sensor values in the left controls panel and click <strong>Predict Health</strong> to check mycelium and disease status.</p>
            </div>
        """, unsafe_allow_html=True)
        
# Footer
st.markdown("<br><hr style='border-top: 1px solid #dcdfd8;'>", unsafe_allow_html=True)
st.markdown("""
    <div style="text-align: center; font-size: 0.8rem; color: #7d8c6d; padding-bottom: 2rem;">
        Smart Mushroom Farming Diagnostics Dashboard &copy; 2026. Final Year College Project.
    </div>
""", unsafe_allow_html=True)
