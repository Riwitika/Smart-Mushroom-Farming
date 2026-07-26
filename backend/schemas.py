from pydantic import BaseModel, Field, validator
from typing import Optional

class PredictionRequest(BaseModel):
    temperature: float = Field(..., description="Temperature in °C (Allowed: 5 to 50)")
    humidity: float = Field(..., description="Humidity percentage (Allowed: 10 to 100)")
    ventilation: str = Field(..., description="Ventilation level (Allowed: low, medium, high)")
    light_intensity: str = Field(..., description="Light intensity level (Allowed: low, medium, high)")
    ph: float = Field(..., description="pH level of substrate (Allowed: 3 to 11)")

    @validator('temperature')
    def validate_temp(cls, v):
        if not (5.0 <= v <= 50.0):
            raise ValueError('Temperature must be between 5.0 and 50.0 °C')
        return v

    @validator('humidity')
    def validate_humidity(cls, v):
        if not (10.0 <= v <= 100.0):
            raise ValueError('Humidity must be between 10.0 and 100.0%')
        return v

    @validator('ph')
    def validate_ph(cls, v):
        if not (3.0 <= v <= 11.0):
            raise ValueError('pH must be between 3.0 and 11.0')
        return v

    @validator('ventilation')
    def validate_vent(cls, v):
        val = v.lower()
        if val not in ['low', 'medium', 'high']:
            raise ValueError('Ventilation must be low, medium, or high')
        return val

    @validator('light_intensity')
    def validate_light(cls, v):
        val = v.lower()
        if val not in ['low', 'medium', 'high']:
            raise ValueError('Light Intensity must be low, medium, or high')
        return val


class PredictionResponse(BaseModel):
    success: bool
    health_status: str
    disease_growth_possibility_level: str
    disease_risk_level: str
    recommendation: str
    timestamp: str


class HealthResponse(BaseModel):
    status: str
    model_loaded: bool
    service: str
