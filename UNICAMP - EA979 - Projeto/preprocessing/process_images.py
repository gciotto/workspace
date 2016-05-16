'''
Given an images path, this module gets the RGB matrix of every image in that directory and
calculates the centroids according k-means algorithm.

@author: gciotto
'''

import numpy as np
from PIL import Image
import scipy.cluster as spy
import os
import time

images_path = "/home/gciotto/Mes Travails/UNICAMP/EA979/mirflickr/"
n_images = 25000
kCentroids = 100
cIter = 50

#### Function getRGBMatrix
#    Gets R, G and B matrixes for all images in image_path
def getRGBMatrix (image_path):
    
    img = Image.open(image_path)
    
    RGBm = img.load()
    
    (rows, cols) = img.size
    
    R = np.zeros(img.size)
    G = np.zeros(img.size)
    B = np.zeros(img.size)
    
    for i in range(rows):
        for j in range(cols):
            (r, g, b) = RGBm[i,j]
            R[i,j] = r
            G[i,j] = g
            B[i,j] = b          
    
    return (R, G, B)

learning_set = np.zeros((n_images, 768))
r = 0;

#for i in os.listdir(images_path):
#    if os.path.isfile(os.path.join(images_path, i)):

for r in range (25000):
        
        print ('Calculating histogram for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1)))
        
        img = Image.open(os.path.join(images_path, 'im%d.jpg' % (r + 1) ))
        RGBm = img.load()
        
        hist_RGB = img.histogram() 
        
        learning_set[r, :] = hist_RGB[:]
    
        #r = r + 1
        
print ('%d images successfully preprocessed...' % (r+1))

print ('Calculating k = %d centroids...' % kCentroids)
start_time = time.time()   

whitenned_learning_set = spy.vq.whiten(learning_set)

standard_deviations = np.zeros ((1, 768))

for i in range(768):
    standard_deviations[0, i] = learning_set[0, i] / whitenned_learning_set [0, i]

(centroids_codebook, distortion) = spy.vq.kmeans(whitenned_learning_set, kCentroids, cIter)

(codes, dist) = spy.vq.vq(whitenned_learning_set, centroids_codebook)

elapsed_time = time.time() - start_time

np.save(os.path.join(images_path, 'centroids_codebook'), centroids_codebook)
np.save(os.path.join(images_path, 'standard_deviations'), standard_deviations)
np.save(os.path.join(images_path, 'vq_codes_obs'), codes)
np.save(os.path.join(images_path, 'vq_dist'), dist)

print ('It took %ds to calculate and save files' % elapsed_time)


