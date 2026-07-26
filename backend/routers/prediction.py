from fastapi import APIRouter, HTTPException, Depends
from schemas import PredictionRequest, PredictionResponse
from services.prediction_service import prediction_service

router = APIRouter()

@router.post("/predict", response_model=PredictionResponse)
def run_prediction(request: PredictionRequest):
    """
    Exposes prediction inference on incoming environmental parameters.
    """
    res = prediction_service.predict_health(
        temperature=request.temperature,
        humidity=request.humidity,
        ventilation=request.ventilation,
        light_intensity=request.light_intensity,
        ph=request.ph
    )
    return res
