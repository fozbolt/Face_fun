import numpy as np
import tensorflow as tf
from tensorflow import keras
import cv2
from face_cropper import crop
from os.path import dirname, join
from keras.applications.vgg16 import VGG16

def main(slika):

    loaded_model=None
    cropped_image=None

    #print('tff:', tf.version.VERSION) ##2.1.0
    #print(keras.__version__) ##2.2.3-ff
    try:
        #1.tryout
        dir1 = join(dirname(__file__), "age_and_gender_3_after_v2.h5")
        loaded_model = tf.keras.models.load_model(dir1)

        #2.tryout
        #model = VGG16()
        #print(loaded_model.summary())

        #print(list(loaded.signatures.keys()))  # ["serving_default"]
        #loaded_model.summary()
        print('Model loaded successfully: ', loaded_model)

    except:
        print('Error with loading model')


    img_path = join(dirname(__file__), "marin.jpg")

    #paket face-cropper
    cropped_image = crop(
        image_path = img_path,
    )


    if cropped_image==None:
        raise Exception('Image format unknown')

    else:
        #PIL format u cv2
        imcv = cv2.cvtColor(np.asarray(cropped_image), cv2.COLOR_RGB2BGR)

        #podesavanje u prikladan format za fittanje na model
        img_resized = cv2.resize(imcv,(48,48))
        image = cv2.cvtColor(img_resized, cv2.COLOR_BGR2RGB)
        image_test=image/255

        prediction = loaded_model.predict(np.array([image_test]))

        gender_f=['Male','Female']
        age=int(np.round(prediction[1][0]))
        gender=int(np.round(prediction[0][0]))

        predictedAge = "Predicted Age: "+ str(age)
        predictedGender = "Predicted Gender: "+ gender_f[gender]

        print(f"{predictedGender}\n {predictedAge}")

        result = [age,gender]
        result = "Age: " + age + "  Gender: " + gender


    return result
