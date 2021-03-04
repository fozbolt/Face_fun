import numpy as np
import cv2
from face_cropper import crop
from os.path import dirname, join
from keras_vggface.vggface import VGGFace
from keras_vggface.utils import preprocess_input
from keras_vggface.utils import decode_predictions

def main(ImageFilePath):
    #filepathImage = join(dirname(__file__), "johnny_depp.jpg")
    cropped_image=None

    cropped_image = crop(
        image_path = ImageFilePath,
    )

    if cropped_image==None:
        return 'Image format unknown'

    else:
        img = cv2.cvtColor(np.array(cropped_image, dtype='uint8'), cv2.COLOR_RGB2BGR)


        slika_resized = cv2.resize(img, (224,224), interpolation= cv2.INTER_AREA)
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