# Test whether an Emergency signal is present in an audio sample
from em_detection import *
from time import sleep
import os

# Read the test file

def run_detection(test_file):
    y, sr = librosa.load(test_file, sr=8000)

    # Load the scaler obtained from the train data
    scaler_filename = "scaler.save"
    scaler = joblib.load(scaler_filename)

    classes = predict_probability(y, scaler)
    return classes

def iterate_delete(directory):
    while True: 
        classes = []
        if not os.listdir(directory):
           sleep(10)
        else:
            for filename in os.listdir(directory):
                classes = run_detection(filename)
                os.remove(filename)

if __name__ == "__main__":
    iterate_delete('./audio')