import numpy as np
from PIL import Image
import scipy.cluster as spy
import matplotlib.pyplot as plt
import sklearn.neighbors as skn
from process_images import n_columns_histogram, images_path, useGreyScale


c_neighbors = 7

image = '/home/gciotto/IMG_4532.JPG'
#image = '/home/gciotto/Mes Travails/Images/teste.png'
#image = '/home/gciotto/Mes Travails/UNICAMP/EA979/mirflickr/im2033.jpg'

# Leitura da imagem
img_original = Image.open(image)

if useGreyScale:
    img = img_original.convert('L')

# Calculo do histograma 
hist_RGB_given_image = img.histogram() 

# Recupera vetores calculados pelo KMEANS
centroids_codebook = np.load('%s/centroids_codebook.npy' % images_path)
vq_codes_obs = np.load('%s/vq_codes_obs.npy' % images_path)
standard_deviations = np.load('%s/standard_deviations.npy' % images_path)

# Normaliza histograma de acordo com os desvios padroes calculados pela funcao 
# whitten() antes de realizar o KMEANS.  
for i in range (n_columns_histogram):
    hist_RGB_given_image[i] = hist_RGB_given_image[i] / standard_deviations[0, i]

# Calcula CENTROIDE mais proximo
(index, dist) = spy.vq.vq([hist_RGB_given_image], centroids_codebook)

print index

same_centroid_images = np.where(vq_codes_obs == index )

print same_centroid_images[0]

hist_RGB_dataset = []

w = 0
for i in same_centroid_images[0]:
    
    img = Image.open('%s/im%d.jpg' % (images_path, i + 1))
    
    if useGreyScale:
        img = img.convert('L')
    
    hist_RGB_dataset_i = img.histogram()
    
    for j in range (n_columns_histogram):
        hist_RGB_dataset_i[j] = hist_RGB_dataset_i[j] / standard_deviations[0, j]
    
    hist_RGB_dataset.append(hist_RGB_dataset_i)
    
    w = w + 1

print len(hist_RGB_dataset)

nearest_neighbors = skn.NearestNeighbors(n_neighbors = c_neighbors).fit(hist_RGB_dataset)
distances, indices = nearest_neighbors.kneighbors(hist_RGB_given_image)

print indices
print distances

# Mostrar resultados - substituir trecho abaixo antes de submeter no ADESSOWIKI 

f, axarr = plt.subplots((c_neighbors + 1) / 2,  2)
axarr[0, 0].imshow(img_original)
axarr[0, 0].set_title('Imagem original')
axarr[0, 0].axis('off')

col = 1
row = 0

for i in range(c_neighbors):
    
    img_index = same_centroid_images[0][indices[0][i]]
    
    img = Image.open('%s/im%d.jpg' % (images_path, img_index +1))
    
    axarr[row, col].imshow(img)
    axarr[row, col].set_title('Image #%d' %  (img_index +1))
    axarr[row, col].axis('off')
    
    col = col + 1
    
    if col >= 2:
        col = 0
        row = row + 1
        
plt.show()