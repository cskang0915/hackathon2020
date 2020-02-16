# Test whether an Emergency signal is present in an audio sample
from em_detection import *

# Read the test file
test_file = '../audio/sirena_ambulanza.wav'
y, sr = librosa.load(test_file, sr=8000)

# Load the scaler obtained from the train data
scaler_filename = "scaler.save"
scaler = joblib.load(scaler_filename)

classes = predict_probability(y, scaler)


