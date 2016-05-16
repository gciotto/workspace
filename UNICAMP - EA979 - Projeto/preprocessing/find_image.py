import numpy as np
from PIL import Image
import scipy.cluster as spy
import matplotlib.pyplot as plt
import matplotlib.image as mpimg


images_path = "/home/gciotto/Mes Travails/UNICAMP/EA979/mirflickr"

#image = '/home/gciotto/IMG_4532.JPG'
image = '/home/gciotto/Mes Travails/Images/teste.png'
#image = '/home/gciotto/Mes Travails/UNICAMP/EA979/mirflickr/im3.jpg'

## NORMALIZAR COLUNAS
img = Image.open(image)

hist_RGB = img.histogram() 

centroids_codebook = np.load('%s/centroids_codebook.npy' % images_path)
vq_codes_obs = np.load('%s/vq_codes_obs.npy' % images_path)
standard_deviations = np.load('%s/standard_deviations.npy' % images_path)

for i in range (768):
    hist_RGB[i] = hist_RGB[i] / standard_deviations[0, i]

(index, dist) = spy.vq.vq([hist_RGB], centroids_codebook)

print (index)

same_centroid_images = np.where(vq_codes_obs == index )

print (same_centroid_images)

number_img = 8

f, axarr = plt.subplots(number_img,  number_img)
axarr[0, 0].imshow(img)
axarr[0, 0].set_title('Imagem original')
axarr[0, 0].axis('off')

col = 1
row = 0

for i in range(number_img ** 2 - 1):
    
    img_index = same_centroid_images[0][i]
    
    img = Image.open('%s/im%d.jpg' % (images_path, img_index +1))
    
    axarr[row, col].imshow(img)
    axarr[row, col].set_title('Image #%d' %  (img_index +1))
    axarr[row, col].axis('off')
    
    col = col + 1
    
    if col >= number_img:
        col = 0
        row = row + 1
        
plt.show()
