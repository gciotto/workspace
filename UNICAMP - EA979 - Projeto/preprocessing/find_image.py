import numpy as np
from PIL import Image
import scipy.cluster as spy
import matplotlib.pyplot as plt
import sklearn.neighbors as skn
from tamura import *
from garbor import * 
from process_images import n_columns_histogram, n_colums_features, n_kernels, images_path, useGreyScale, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor


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
centroids_codebook_features = np.load('%s/centroids_codebook_features.npy' % images_path)
vq_codes_obs_features = np.load('%s/vq_codes_obs_features.npy' % images_path)
standard_deviations_features = np.load('%s/standard_deviations_features.npy' % images_path)

learning_set_features_image = np.zeros(n_colums_features)
j = 0

if useTamuraCoarseness:
    learning_set_features_image[j] = tamura_coarseness(img)
    j = j + 1

if useTamuraContrast:
    learning_set_features_image[j] = tamura_contrast(hist_RGB_given_image)
    j = j + 1

if useTamuraDirectionality:
    learning_set_features_image[j] = tamura_directionality(img)
    j = j + 1

if useGarbor: 
    
    start_i = j
    stop_i = start_i + n_kernels * 2
    
    learning_set_features_image[start_i : stop_i] = np.resize(garbor_features(img, kernels) , (1, n_kernels * 2)) 

for i in range (n_colums_features):
    learning_set_features_image[i] = learning_set_features_image[i] / standard_deviations_features[0, i]


(index, dist) = spy.vq.vq([learning_set_features_image], centroids_codebook_features)

centroids_codebook_histogram = np.load('%s/centroids_codebook_histogram_%d.npy' % (images_path , index[0]))
vq_codes_obs_histogram = np.load('%s/vq_codes_obs_histogram_%d.npy' % (images_path , index[0]))
standard_deviations_histogram = np.load('%s/standard_deviations_histogram_%d.npy' % (images_path , index[0]))

# Normaliza histograma de acordo com os desvios padroes calculados pela funcao 
# whitten() antes de realizar o KMEANS.  
for i in range (n_columns_histogram):
    hist_RGB_given_image[i] = hist_RGB_given_image[i] / standard_deviations_histogram[0, i]

# Calcula CENTROIDE mais proximo
(index, dist) = spy.vq.vq([hist_RGB_given_image], centroids_codebook_histogram)

print index

same_centroid_images = np.where(vq_codes_obs_histogram == index )

print same_centroid_images[0]

hist_RGB_dataset = []

w = 0
for i in same_centroid_images[0]:
    
    img = Image.open('%s/im%d.jpg' % (images_path, i + 1))
    
    if useGreyScale:
        img = img.convert('L')
    
    hist_RGB_dataset_i = img.histogram()
    
    for j in range (n_columns_histogram):
        hist_RGB_dataset_i[j] = hist_RGB_dataset_i[j] / standard_deviations_histogram[0, j]
    
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