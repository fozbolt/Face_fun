import numpy as np
import tensorflow as tf
import cv2
from face_cropper import crop
from os.path import dirname, join
from PIL import Image

def main(slika):

    #print('tff:', tf.version.VERSION) ##2.1.0
    #print(keras.__version__) ##2.2.3-ff

    loaded_model=None
    cropped_image=None

    try:

        dir = join(dirname(__file__), "age_and_gender_3_after_v2.h5")
        loaded_model = tf.keras.models.load_model(dir)

        #print(loaded_model.summary())

        print('Model loaded successfully')

    except:
        print('Error with loading model')


    img_path = join(dirname(__file__), "marin.jpg")
    #print(slika)
    #img = cv2.imread(slika)
    img = np.array(slika, dtype='uint8')
    print("dobro",img)

    #paket face-cropper

    cropped_image = crop(
        image_path = img_path,
    )


    if cropped_image==None:
        raise Exception('Image format unknown')

    else:

        #PIL format u cv2
        imcv = cv2.cvtColor(img, cv2.COLOR_RGB2BGR)

        #podesavanje u prikladan format za fittanje na model
        img_resized = cv2.resize(imcv,(48,48))
        image = cv2.cvtColor(img_resized, cv2.COLOR_BGR2RGB)
        image_test=image/255

        prediction = loaded_model.predict(np.array([image_test]))

        gender_f=['Male','Female']
        age=int(np.round(prediction[1][0]))
        gender=int(np.round(prediction[0][0]))

        print("Predicted Age: "+ str(age))
        print("Predicted Gender: "+ gender_f[gender])


        #result = [age,gender]
        result = "Age: " + str(age) + "  Gender: " + str(gender_f[gender])

    return result
