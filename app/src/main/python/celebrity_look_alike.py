import numpy as np
import cv2
from os.path import dirname, join
from keras_vggface.vggface import VGGFace
from keras_vggface.utils import preprocess_input
from keras_vggface.utils import decode_predictions

def main(slika):
    filepathHaars = join(dirname(__file__), "haarcascade_frontalface_default.xml")
    filepathImage = join(dirname(__file__), "johnny_depp.jpg")
    picture = cv2.imread(filepathImage)
    img = np.asarray(picture, dtype='uint8')
    face_cascade = cv2.CascadeClassifier(filepathHaars)
    face = face_cascade.detectMultiScale(img, scaleFactor=1.3)
    if len(face)  == 0:
        print("Molim vas unesite bolju sliku.")
    else:
        for (x, y, w, h) in face:
            slika_resized = cv2.resize(img[y:y+h, x:x+w], (224,224), interpolation= cv2.INTER_AREA)

    slika_resized = slika_resized.astype('float32')
    slika_resized = np.expand_dims(slika_resized, axis = 0)
    slika_resized = preprocess_input(slika_resized, version = 2)

    model = VGGFace(model = 'resnet50')

    prediction = model.predict(slika_resized)
    results = decode_predictions(prediction)

    final_result = []
    for result in results[0]:
        final_result.append('%s: %.3f%%' % (result[0], result[1]*100))

    return final_result
