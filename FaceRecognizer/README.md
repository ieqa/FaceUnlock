The method below takes two arguments: The path to the directory containing the training faces and the path to the image you want to classify. Note that all images has to be of the same size except for LBPH and that the faces already has to be cropped out of their original images.

For the simplicity of this post, the class also requires that the training images have filename format:

**\<label>-rest_of_filename.png**.

lable must be number. You must collect face images under different illuminations as much as possible for a better recognition rate.
