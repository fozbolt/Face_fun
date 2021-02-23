import numpy as np
import cv2
from os.path import dirname, join

def main(slika):
    filepathHaars = join(dirname(__file__), "haarcascade_frontalface_default.xml")
    img = np.asarray(slika, dtype='uint8')
    face_cascade = cv2.CascadeClassifier(filepathHaars)
    face = face_cascade.detectMultiScale(img, scaleFactor=1.3)
    if len(face)  == 0:
        print("Molim vas unesite bolju sliku.")
    else:
        for (x, y, w, h) in face:
            slika_resized = cv2.resize(slika[y:y+h, x:x+w], (160,160), interpolation= cv2.INTER_AREA)
            print(slika_resized)

    return "bravosssss"
