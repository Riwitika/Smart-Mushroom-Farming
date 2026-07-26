import os
import sys

# Ensure backend folder is in sys.path
backend_dir = os.path.dirname(os.path.abspath(__file__))
if backend_dir not in sys.path:
    sys.path.insert(0, backend_dir)

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from config import settings
from routers import prediction
from schemas import HealthResponse
from services.prediction_service import prediction_service

app = FastAPI(
    title=settings.PROJECT_NAME,
    version=settings.VERSION,
    description="REST API for predicting mushroom crop disease risks using ML models."
)

# CORS configuration
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"], # Allow all origins (including Android Emulator/devices)
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers
app.include_router(prediction.router, prefix=settings.API_V1_STR)

@app.get("/")
def read_root():
    return {
        "message": "Smart Mushroom Farming Prediction API",
        "version": settings.VERSION
    }

@app.get("/health", response_model=HealthResponse)
def health_check():
    model_loaded = prediction_service.check_model_loaded()
    return {
        "status": "healthy",
        "model_loaded": model_loaded,
        "service": settings.PROJECT_NAME
    }
