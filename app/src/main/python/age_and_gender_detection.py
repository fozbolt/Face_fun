import numpy as np
import tensorflow as tf
import cv2
from face_cropper import crop
from face_cropper.exceptions import NoFaceException, AboveThresholdException
from os.path import dirname, join


#https://pysource.com/2019/04/04/face-swapping-opencv-with-python-part-1/

def main(ImageFilePath, byteArr):

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




    try:
        ##photo from path (gallery or camera)
        cropped_image = crop(image_path = ImageFilePath)
        imcv = cv2.cvtColor(np.array(cropped_image, dtype='uint8'), cv2.COLOR_RGB2BGR)

        ##photo from byteArray (gallery or camera)
        #imcv = cv2.cvtColor(np.array(byteArr, dtype='uint8'), cv2.COLOR_RGB2BGR)

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


    except(NoFaceException,AboveThresholdException):
        return "Please upload better picture"