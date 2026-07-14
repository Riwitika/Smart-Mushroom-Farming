import os
import pickle
import pandas as pd
import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from xgboost import XGBClassifier
from sklearn.metrics import accuracy_score, precision_recall_fscore_support, confusion_matrix, classification_report
from preprocessing import prepare_data

def train_and_evaluate():
    # 1. Load and prepare data
    csv_path = "data/disease_growth_level.csv"
    models_dir = "models"
    X_train, X_test, y_train, y_test, preprocessor = prepare_data(csv_path, models_dir)
    
    # 2. Define classifiers
    models = {
        "Logistic Regression": LogisticRegression(max_iter=1000, random_state=42),
        "Decision Tree": DecisionTreeClassifier(random_state=42),
        "Random Forest": RandomForestClassifier(random_state=42),
        "SVM": SVC(probability=True, random_state=42),
        "XGBoost": XGBClassifier(random_state=42, use_label_encoder=False, eval_metric='mlogloss')
    }
    
    results = {}
    best_f1 = 0
    best_model_name = None
    best_model_obj = None
    
    # 3. Train and evaluate each model
    print("\n" + "="*50)
    print("TRAINING AND COMPARING CLASSIFIERS")
    print("="*50)
    
    for name, model in models.items():
        print(f"\nTraining model: {name} ...")
        model.fit(X_train, y_train)
        
        # Predict on test data
        y_pred = model.predict(X_test)
        
        # Calculate metrics
        acc = accuracy_score(y_test, y_pred)
        precision, recall, f1, _ = precision_recall_fscore_support(y_test, y_pred, average='weighted')
        cm = confusion_matrix(y_test, y_pred)
        
        results[name] = {
            "Accuracy": acc,
            "Precision": precision,
            "Recall": recall,
            "F1 Score": f1,
            "Confusion Matrix": cm
        }
        
        print(f"Metrics for {name}:")
        print(f" - Accuracy  : {acc:.4f}")
        print(f" - Precision : {precision:.4f}")
        print(f" - Recall    : {recall:.4f}")
        print(f" - F1 Score  : {f1:.4f}")
        print("Confusion Matrix:")
        print(cm)
        
        # Classification report for detail
        print("Classification Report:")
        print(classification_report(y_test, y_pred, target_names=['Good', 'Moderate', 'Bad']))
        
        # Keep track of best model based on F1 Score
        if f1 > best_f1:
            best_f1 = f1
            best_model_name = name
            best_model_obj = model
            
    # 4. Print Comparison Summary
    print("\n" + "="*50)
    print("PERFORMANCE COMPARISON SUMMARY")
    print("="*50)
    summary_df = pd.DataFrame({
        name: {
            "Accuracy": results[name]["Accuracy"],
            "Precision": results[name]["Precision"],
            "Recall": results[name]["Recall"],
            "F1 Score": results[name]["F1 Score"]
        } for name in results
    }).T
    print(summary_df.to_string())
    
    # 5. Save the best model
    print("\n" + "="*50)
    print(f"BEST MODEL SELECTED: {best_model_name} (F1 Score: {best_f1:.4f})")
    print("="*50)
    
    best_model_path = os.path.join(models_dir, "best_model.pkl")
    with open(best_model_path, "wb") as f:
        pickle.dump(best_model_obj, f)
    print(f"Saved best model object '{best_model_name}' to: {best_model_path}")
    
    # Let's save a summary dictionary too, containing model metadata
    model_metadata = {
        "best_model_name": best_model_name,
        "features": X_train.columns.tolist(),
        "classes": ['Good', 'Moderate', 'Bad'],
        "metrics": {
            "Accuracy": results[best_model_name]["Accuracy"],
            "Precision": results[best_model_name]["Precision"],
            "Recall": results[best_model_name]["Recall"],
            "F1 Score": results[best_model_name]["F1 Score"]
        }
    }
    metadata_path = os.path.join(models_dir, "model_metadata.pkl")
    with open(metadata_path, "wb") as f:
        pickle.dump(model_metadata, f)
    print(f"Saved metadata to: {metadata_path}")
    
    return results, best_model_name

if __name__ == "__main__":
    train_and_evaluate()
