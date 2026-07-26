import os
import sys
from datetime import datetime
from fastapi import HTTPException, status

# Resolve parent directory to import predict.py
parent_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "../.."))
sys.path.append(parent_dir)

try:
    import predict
    # Overwrite relative model paths in predict.py with correct absolute parent paths
    predict.MODELS_DIR = os.path.join(parent_dir, "models")
    predict.PREPROCESSOR_PATH = os.path.join(predict.MODELS_DIR, "preprocessor.pkl")
    predict.MODEL_PATH = os.path.join(predict.MODELS_DIR, "best_model.pkl")
except Exception as e:
    # Failures will be handled gracefully during method invocations
    pass

class PredictionService:
    def check_model_loaded(self) -> bool:
        """
        Check if the machine learning model files are successfully loaded.
        """
        try:
            preprocessor, model = predict.load_resources()
            return preprocessor is not None and model is not None
        except Exception:
            return False

    def predict_health(
        self,
        temperature: float,
        humidity: float,
        ventilation: str,
        light_intensity: str,
        ph: float
    ) -> dict:
        """
        Runs inference on the existing machine learning model wrapper.
        """
        try:
            # Check model files exist first and raise proper HTTP exceptions
            if not os.path.exists(predict.PREPROCESSOR_PATH):
                raise HTTPException(
                    status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                    detail=f"Preprocessor file not found at {predict.PREPROCESSOR_PATH}"
                )
            if not os.path.exists(predict.MODEL_PATH):
                raise HTTPException(
                    status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                    detail=f"Model file not found at {predict.MODEL_PATH}"
                )

            # Invoke prediction logic from predict.py exactly
            result = predict.predict_mushroom_health(
                temperature=temperature,
                humidity=humidity,
                ventilation=ventilation,
                light_intensity=light_intensity,
                ph=ph
            )

            # Map health_status back to target possibility level
            # Good -> Low, Moderate -> Moderate, Bad -> High
            mapping = {
                "Good": "Low",
                "Moderate": "Moderate",
                "Bad": "High"
            }
            health = result["health_status"]
            possibility = mapping.get(health, "Low")

            return {
                "success": True,
                "health_status": health,
                "disease_growth_possibility_level": possibility,
                "disease_risk_level": result["disease_risk_level"],
                "recommendation": result["recommendation"],
                "timestamp": datetime.utcnow().isoformat()
            }
        except FileNotFoundError as e:
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=str(e)
            )
        except HTTPException:
            raise
        except Exception as e:
            raise HTTPException(
                status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
                detail=f"Prediction failure: {str(e)}"
            )

prediction_service = PredictionService()
