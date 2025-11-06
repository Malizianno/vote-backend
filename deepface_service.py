from flask import Flask, request, jsonify
from deepface import DeepFace
import base64
import cv2
import numpy as np

app = Flask(__name__)

@app.route('/verify', methods=['POST'])
def verify():
    data = request.json
    img_base64 = data['imageBase64']
    reference_list = data['referenceBase64List']

    img_data = base64.b64decode(img_base64)
    np_arr = np.frombuffer(img_data, np.uint8)
    input_img = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)

    best_match = None
    lowest_distance = float('inf')

    for ref_base64 in reference_list:
        try:
            ref_data = base64.b64decode(ref_base64)
            ref_arr = np.frombuffer(ref_data, np.uint8)
            ref_img = cv2.imdecode(ref_arr, cv2.IMREAD_COLOR)

            result = DeepFace.verify(img1_path=input_img, img2_path=ref_img, enforce_detection=False)
            if result["verified"] and result["distance"] < lowest_distance:
                best_match = {
                    "distance": result["distance"],
                    "model": result["model"],
                    "similarity_metric": result["similarity_metric"]
                }
                lowest_distance = result["distance"]
        except Exception:
            continue

    if best_match:
        return jsonify({ "match": True, **best_match })
    else:
        return jsonify({ "match": False })

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5001)