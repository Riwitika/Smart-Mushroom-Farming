import os
import pickle
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

class MushroomPreprocessor:
    def __init__(self):
        self.scaler = StandardScaler()
        self.imputation_values = {}
        self.iqr_bounds = {}
        self.ventilation_map = {'low': 0, 'medium': 1, 'high': 2}
        self.light_map = {'low': 0, 'medium': 1, 'high': 2}
        self.target_map = {'Good': 0, 'Moderate': 1, 'Bad': 2}
        self.reverse_target_map = {0: 'Good', 1: 'Moderate', 2: 'Bad'}
        
    def fit(self, df):
        # 1. Target Variable Handling
        if 'disease growth possibility level' in df.columns:
            df = df.rename(columns={'disease growth possibility level': 'Health_Status'})
            mapping = {'Low': 'Good', 'Moderate': 'Moderate', 'High': 'Bad'}
            df['Health_Status'] = df['Health_Status'].map(mapping)
            
        # 2. Extract imputation parameters from training data
        # For numerical columns, use median
        self.imputation_values['ph'] = df['ph'].median()
        # For categorical columns, use mode
        self.imputation_values['ventilation'] = df['ventilation'].mode()[0] if not df['ventilation'].dropna().empty else 'medium'
        self.imputation_values['light_intensity'] = df['light_intensity'].mode()[0] if not df['light_intensity'].dropna().empty else 'medium'
        
        # 3. Calculate IQR bounds for outlier clipping
        for col in ['temperature', 'humidity', 'ph']:
            q1 = df[col].quantile(0.25)
            q3 = df[col].quantile(0.75)
            iqr = q3 - q1
            self.iqr_bounds[col] = (q1 - 1.5 * iqr, q3 + 1.5 * iqr)
            
        # 4. Fit the scaler on numerical features
        # Clean numerical values on the fly to fit correctly
        temp_df = df.copy()
        temp_df['ph'] = temp_df['ph'].fillna(self.imputation_values['ph'])
        for col in ['temperature', 'humidity', 'ph']:
            lower, upper = self.iqr_bounds[col]
            temp_df[col] = np.clip(temp_df[col], lower, upper)
            
        numerical_features = temp_df[['temperature', 'humidity', 'ph']]
        self.scaler.fit(numerical_features)
        
    def transform(self, df, is_training=False):
        df_clean = df.copy()
        
        # 1. Target Variable Handling (only if present)
        if 'disease growth possibility level' in df_clean.columns:
            df_clean = df_clean.rename(columns={'disease growth possibility level': 'Health_Status'})
            mapping = {'Low': 'Good', 'Moderate': 'Moderate', 'High': 'Bad'}
            df_clean['Health_Status'] = df_clean['Health_Status'].map(mapping)
            
        # 2. Impute missing values
        df_clean['ph'] = df_clean['ph'].fillna(self.imputation_values['ph'])
        df_clean['ventilation'] = df_clean['ventilation'].fillna(self.imputation_values['ventilation'])
        df_clean['light_intensity'] = df_clean['light_intensity'].fillna(self.imputation_values['light_intensity'])
        
        # 3. Clip outliers using fitted IQR bounds
        for col in ['temperature', 'humidity', 'ph']:
            lower, upper = self.iqr_bounds[col]
            df_clean[col] = np.clip(df_clean[col], lower, upper)
            
        # 4. Encode categorical values
        df_clean['ventilation'] = df_clean['ventilation'].str.lower().map(self.ventilation_map).fillna(1).astype(int)
        df_clean['light_intensity'] = df_clean['light_intensity'].str.lower().map(self.light_map).fillna(1).astype(int)
        
        # 5. Extract Date/Time values (even though we don't train on them, we preprocess them for requirements)
        if 'date' in df_clean.columns and 'time' in df_clean.columns:
            df_clean['datetime'] = pd.to_datetime(df_clean['date'] + ' ' + df_clean['time'], format='mixed', errors='coerce')
            df_clean['month'] = df_clean['datetime'].dt.month.fillna(6).astype(int)
            df_clean['day'] = df_clean['datetime'].dt.day.fillna(15).astype(int)
            df_clean['dayofweek'] = df_clean['datetime'].dt.dayofweek.fillna(0).astype(int)
            df_clean['hour'] = df_clean['datetime'].dt.hour.fillna(12).astype(int)
            df_clean['minute'] = df_clean['datetime'].dt.minute.fillna(0).astype(int)
            # drop temporary datetime
            df_clean = df_clean.drop(columns=['datetime'])
            
        # 6. Scale numerical features
        scaled_nums = self.scaler.transform(df_clean[['temperature', 'humidity', 'ph']])
        df_clean['temperature_scaled'] = scaled_nums[:, 0]
        df_clean['humidity_scaled'] = scaled_nums[:, 1]
        df_clean['ph_scaled'] = scaled_nums[:, 2]
        
        # 7. Map target if it exists
        if 'Health_Status' in df_clean.columns:
            df_clean['Health_Status_encoded'] = df_clean['Health_Status'].map(self.target_map)
            
        return df_clean

def prepare_data(csv_path, models_dir="models"):
    print("Loading data from:", csv_path)
    df = pd.read_csv(csv_path)
    
    # Drop duplicates
    initial_len = len(df)
    df = df.drop_duplicates()
    print(f"Dropped {initial_len - len(df)} duplicate records. Remaining records: {len(df)}")
    
    # Split training and testing sets before preprocessing to avoid leakages
    if 'disease growth possibility level' in df.columns:
        train_df, test_df = train_test_split(df, test_size=0.2, random_state=42, stratify=df['disease growth possibility level'])
    else:
        train_df, test_df = train_test_split(df, test_size=0.2, random_state=42)
        
    print(f"Train set size: {len(train_df)}, Test set size: {len(test_df)}")
    
    # Fit preprocessor
    preprocessor = MushroomPreprocessor()
    preprocessor.fit(train_df)
    
    # Transform train and test
    train_preprocessed = preprocessor.transform(train_df, is_training=True)
    test_preprocessed = preprocessor.transform(test_df)
    
    # Save the preprocessor
    os.makedirs(models_dir, exist_ok=True)
    preprocessor_path = os.path.join(models_dir, "preprocessor.pkl")
    with open(preprocessor_path, "wb") as f:
        pickle.dump(preprocessor, f)
    print("Saved preprocessor to:", preprocessor_path)
    
    # List of features to use in ML model
    # We only use the 5 parameters inputted in the dashboard
    feature_cols = ['temperature_scaled', 'humidity_scaled', 'ventilation', 'light_intensity', 'ph_scaled']
    
    X_train = train_preprocessed[feature_cols]
    y_train = train_preprocessed['Health_Status_encoded']
    X_test = test_preprocessed[feature_cols]
    y_test = test_preprocessed['Health_Status_encoded']
    
    return X_train, X_test, y_train, y_test, preprocessor

if __name__ == "__main__":
    csv_path = "data/disease_growth_level.csv"
    X_train, X_test, y_train, y_test, prep = prepare_data(csv_path)
    print("Preprocessing completed successfully!")
    print("Features trained on:", X_train.columns.tolist())
    print("Train X shape:", X_train.shape, "Test X shape:", X_test.shape)
