# Test whether an Emergency signal is present in an audio sample
from em_detection import *
from time import sleep

# Read the test file

def run_detection(test_file):
    y, sr = librosa.load(test_file, sr=8000)

    # Load the scaler obtained from the train data
    scaler_filename = "scaler.save"
    scaler = joblib.load(scaler_filename)

    classes = predict_probability(y, scaler)
    return classes

if __name__ == "__main__":
    print(run_detection("../audio/sirena-ambulanza.wav"))
#     while True: 
#         if 
#             run_detection()
#         else:
#             sleep(5)