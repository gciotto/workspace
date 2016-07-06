import numpy as np
from PIL import Image
import scipy.cluster as spy
from scipy import misc
import matplotlib.pyplot as plt
import sklearn.neighbors as skn
import os
from tamura import coarseness, contrast, degreeDirect, threshold, neigh, rgb2gray
from garbor import garbor_features, kernels
from process_images import *
c_neighbors = 3

image = '/home/gciotto/Downloads/rainbow.jpg'
#image = '/home/gciotto/Downloads/lnls.jpg'
#image = '/home/gciotto/Downloads/eiffel.jpeg'
#image = '/home/gciotto/Mes Travails/Images/teste.png'
#image = '%s/im24013.jpg' % images_path

# Leitura da imagem
img = misc.imread(image)
                
if useGreyScale:
    img = rgb2gray(img)
    

# Calculo do histograma 
hist_RGB_given_image, bins = np.histogram(img, n_columns_histogram)

path_1 = "%s/arrays/level1_%d_%d" % (images_path, kCentroids_features, cIter_features)

# Recupera vetores calculados pelo KMEANS
centroids_codebook_features = np.load('%s/centroids_codebook_features_%d_%d_%s_%s_%s_%s.npy' % (path_1, kCentroids_features, cIter_features, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor))
vq_codes_obs_features = np.load('%s/vq_codes_obs_features_%d_%d_%s_%s_%s_%s.npy' % (path_1, kCentroids_features, cIter_features, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor))
standard_deviations_features = np.load('%s/standard_deviations_features_%d_%d_%s_%s_%s_%s.npy' % (path_1, kCentroids_features, cIter_features, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor))

learning_set_features_image = np.zeros(n_colums_features)
j = 0

if useTamuraCoarseness:
    learning_set_features_image[j] = coarseness(img)
    j = j + 1

if useTamuraContrast:
    learning_set_features_image[j] = contrast(img)
    j = j + 1

if useTamuraDirectionality:
    learning_set_features_image[j] = degreeDirect(img, threshold, neigh)
    j = j + 1

if useGarbor: 
    
    start_i = j
    stop_i = start_i + n_kernels * 2
    
    learning_set_features_image[start_i : stop_i] = np.resize(garbor_features(img, kernels) , (1, n_kernels * 2)) 

for i in range (n_colums_features):
    learning_set_features_image[i] = learning_set_features_image[i] / standard_deviations_features[i]


# Images from 1o level Kmeans
(index, dist) = spy.vq.vq(np.array([learning_set_features_image]), centroids_codebook_features)

path_2 = "%s/arrays/level2_%d_%d" % (images_path, kCentroids, cIter)

images_index = np.load('%s/centroids_codebook_images_index_%d_%d_%d_%s_%s_%s_%s.npy' % (path_2, index[0] , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor))
centroids_codebook_histogram = np.load('%s/centroids_codebook_histogram_%d_%d_%d_%s_%s_%s_%s.npy' % (path_2, index[0] , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor))
vq_codes_obs_histogram = np.load('%s/vq_codes_obs_histogram_%d_%d_%d_%s_%s_%s_%s.npy' % (path_2, index[0] , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor))
standard_deviations_histogram = np.load('%s/standard_deviations_histogram_%d_%d_%d_%s_%s_%s_%s.npy' % (path_2, index[0] , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor))

# Normaliza histograma de acordo com os desvios padroes calculados pela funcao 
# whitten() antes de realizar o KMEANS.  
for i in range (n_columns_histogram):
    hist_RGB_given_image[i] = hist_RGB_given_image[i] / standard_deviations_histogram[i]

# Calcula CENTROIDE mais proximo
(index, dist) = spy.vq.vq(np.array([hist_RGB_given_image]), centroids_codebook_histogram)

print index

same_centroid_images = np.where(vq_codes_obs_histogram == index )

print same_centroid_images[0]

hist_RGB_dataset = []

w = 0
for i in same_centroid_images[0]:
    
    if not os.path.isfile(os.path.join(images_path, 'histograms.npy')):
    
        img = misc.imread('%s/im%d.jpg' % (images_path, images_index[i] + 1))
    
        if useGreyScale:
            img = rgb2gray(img)

        hist_RGB_dataset_i, bins = np.histogram(img, n_columns_histogram)
            
    else:
        hist_RGB_dataset_i = np.load(os.path.join(images_path, 'hist_%d.npy' % (images_index[i] + 1)))
        
    for j in range (n_columns_histogram):
        hist_RGB_dataset_i[j] = hist_RGB_dataset_i[j] / standard_deviations_histogram[j]
    
    hist_RGB_dataset.append(hist_RGB_dataset_i)
    
    w = w + 1

print len(hist_RGB_dataset)

nearest_neighbors = skn.NearestNeighbors(n_neighbors = c_neighbors).fit(hist_RGB_dataset)
distances, indices = nearest_neighbors.kneighbors(hist_RGB_given_image)

print indices
print distances

# Mostrar resultados - substituir trecho abaixo antes de submeter no ADESSOWIKI 

img_original = Image.open(image)

f, axarr = plt.subplots((c_neighbors + 1) / 2,  2)
axarr[0, 0].imshow(img_original)
axarr[0, 0].set_title('Imagem original')
axarr[0, 0].axis('off')

col = 1
row = 0

for i in range(c_neighbors):
    
    img_index = images_index[same_centroid_images[0][indices[0][i]]]
    
    img = Image.open('%s/im%d.jpg' % (images_path, img_index +1))
    
    axarr[row, col].imshow(img)
    axarr[row, col].set_title('Image #%d' %  (img_index +1))
    axarr[row, col].axis('off')
    
    col = col + 1
    
    if col >= 2:
        col = 0
        row = row + 1
        
plt.show()