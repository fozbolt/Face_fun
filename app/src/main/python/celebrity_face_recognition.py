import numpy as np
import tensorflow as tf
from tensorflow import keras
import cv2
from os.path import dirname, join

def main(slika):
    model_Celebritys =["Adriana Lima","Alex Lawther","Alexandra Daddario","Alvaro Morte","Ben Affleck","Bill Gates","Brian J. Smith","Chris Evans","Chris Hemsworth","Chris Pratt","Cristiano Ronaldo","Emilia Clarke","Emma Watson","Henry Cavil","Jason Momoa","Jennifer Lawrence","Johnny Depp","Josh Radnor","Leonardo DiCaprio","Madelaine Petsch","Megan Fox","Miley Cyrus","Morgan Freeman","Rihanna","Rober De Niro","Selena Gomez","Tom Cruise","Zac Efron","Barack Obama","Barbara Palvin","Camila Mendes","Ellen Page","Elon Musk","Kiernen Shipka","Scarlett Johansson"]
    filepathHaars = join(dirname(__file__), "haarcascade_frontalface_default.xml")
    #filepathImage = join(dirname(__file__), "leonardo_dicaprio.jpg")
    filepathModel = join(dirname(__file__), "Celeb_FR.h5")

    loaded_model = tf.keras.models.load_model(filepathModel)
    #picture = cv2.imread(filepathImage)
    img = np.asarray(slika, dtype='uint8')

    face_cascade = cv2.CascadeClassifier(filepathHaars)
    face = face_cascade.detectMultiScale(img, scaleFactor=1.3)
    if len(face)  == 0:
        return "Molim vas unesite bolju sliku."
    else:
        for (x, y, w, h) in face:
            slika_resized = cv2.resize(img[y:y+h, x:x+w], (160,160), interpolation= cv2.INTER_AREA)
            predikcija = (np.argmax(loaded_model.predict(slika_resized[tf.newaxis, ...])))
            return model_Celebritys[predikcija]

