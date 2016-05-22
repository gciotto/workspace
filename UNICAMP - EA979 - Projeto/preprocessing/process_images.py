'''
# References
http://people.kmi.open.ac.uk/stefan/www-pub/howarth-rueger-2004-civr-texture.pdf
'''

import numpy as np
from PIL import Image
import scipy.cluster as spy
import os
import time
from tamura import tamura_coarseness, tamura_contrast, tamura_directionality

images_path = "/home/gciotto/Mes Travails/UNICAMP/EA979/mirflickr"

n_images = 25000

# Parametros utilizados pelo KMEANS
kCentroids = 200
cIter = 50

# Escolha dos FEATURES
useGreyScale = True
useTamuraCoarseness = False
useTamuraContrast = True
useTamuraDirectionality = True

if useGreyScale:
    n_columns_histogram = 256
else: 
    n_columns_histogram = 768

if useTamuraCoarseness:
    
    tamura_coarseness_hasBeenCalculated = os.path.isfile(os.path.join(images_path, 'tamura_coarseness.npy'))
    
    if tamura_coarseness_hasBeenCalculated:
        tamura_coarseness_v = np.load('%s/tamura_coarseness.npy' % images_path)
    else : tamura_coarseness_v = np.zeros(25000)
    
if useTamuraContrast:
    
    tamura_contrast_hasBeenCalculated = os.path.isfile(os.path.join(images_path, 'tamura_contrast.npy'))
    
    if tamura_contrast_hasBeenCalculated:
        tamura_contrast_v = np.load('%s/tamura_contrast.npy' % images_path)
    else : tamura_contrast_v = np.zeros(25000)

    
if useTamuraDirectionality:
    
    tamura_directionality_hasBeenCalculated = os.path.isfile(os.path.join(images_path, 'tamura_directionality.npy'))
    
    if tamura_directionality_hasBeenCalculated:
        tamura_directionality_v = np.load('%s/tamura_directionality.npy' % images_path)
    else : tamura_directionality_v = np.zeros(25000)

    

if __name__ == '__main__':

    learning_set = np.zeros((n_images, n_columns_histogram))
    r = 0;
    
    for r in range (25000):
            
            print 'Calculating histogram for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
            
            img = Image.open(os.path.join(images_path, 'im%d.jpg' % (r + 1) ))
            
            if useGreyScale:
                img = img.convert('L')
            
            tamura_directionality(img)
            
            hist_RGB = img.histogram()
                    
            learning_set[r, :] = hist_RGB[:]
                       
            if useTamuraCoarseness:
                           
                if not tamura_coarseness_hasBeenCalculated:
                    if os.path.isfile(os.path.join(images_path, 'tamura_coarseness_%d.npy' % (r + 1))):
                        tamura_coarseness_v[r] = np.load(os.path.join(images_path, 'tamura_coarseness_%d.npy' % (r + 1)))
                    else : 
                
                        print 'Calculating Tamura COARSENESS for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
                        
                        start_time = time.time()   
                        tamura_coarseness_v[r] = tamura_coarseness(img)
                        elapsed_time = time.time() - start_time
                        
                        print 'It took %ds to calculate COARSENESS...' % elapsed_time
                        
                        np.save(os.path.join(images_path, 'tamura_coarseness_%d.npy' % (r + 1)),tamura_coarseness_v[r])
                    
                    print tamura_coarseness_v[r] 
                    
            if useTamuraContrast:
                                                
                if not tamura_contrast_hasBeenCalculated:
                    
                    if os.path.isfile(os.path.join(images_path, 'tamura_contrast_%d.npy' % (r + 1))):
                        tamura_contrast_v[r] = np.load(os.path.join(images_path, 'tamura_contrast_%d.npy' % (r + 1)))
                    else : 
                        
                        print 'Calculating Tamura CONTRAST for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
                        
                        start_time = time.time()   
                        tamura_contrast_v[r] = tamura_contrast(hist_RGB)
                        elapsed_time = time.time() - start_time
                        
                        print 'It took %ds to calculate CONTRAST...' % elapsed_time
                        
                        np.save(os.path.join(images_path, 'tamura_contrast_%d.npy' % (r + 1)),tamura_contrast_v[r])
                    
                    print tamura_contrast_v[r]
            
            if useTamuraDirectionality:
                                                
                if not tamura_directionality_hasBeenCalculated:
                    
                    if os.path.isfile(os.path.join(images_path, 'tamura_directionality_%d.npy' % (r + 1))):
                        tamura_directionality_v[r] = np.load(os.path.join(images_path, 'tamura_directionality_%d.npy' % (r + 1)))
                    else : 
                        
                        print 'Calculating Tamura DIRECTIONALITY for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
                        
                        start_time = time.time()   
                        tamura_directionality_v[r] = tamura_directionality(img)
                        elapsed_time = time.time() - start_time
                        
                        print 'It took %ds to calculate DIRECTIONALITY...' % elapsed_time
                        
                        np.save(os.path.join(images_path, 'tamura_directionality_%d.npy' % (r + 1)),tamura_directionality_v[r])
                    
                    print tamura_directionality_v[r]
                
    
    if useTamuraCoarseness: 
        if not tamura_coarseness_hasBeenCalculated:
            np.save('%s/tamura_coarseness.npy' % images_path, tamura_coarseness_v)
    
    if useTamuraContrast: 
        if not tamura_contrast_hasBeenCalculated:
            np.save('%s/tamura_contrast.npy' % images_path, tamura_contrast_v)
    
    if useTamuraDirectionality: 
        if not tamura_directionality_hasBeenCalculated:
            np.save('%s/tamura_directionality.npy' % images_path, tamura_directionality_v)
            
    print '%d images successfully preprocessed...' % (r+1)
    
    print 'Calculating k = %d centroids...' % kCentroids
    start_time = time.time()   
    
    whitenned_learning_set = spy.vq.whiten(learning_set)
    
    standard_deviations = np.zeros ((1, n_columns_histogram))
    
    for i in range(n_columns_histogram):
        standard_deviations[0, i] = learning_set[0, i] / whitenned_learning_set [0, i]
    
    (centroids_codebook, distortion) = spy.vq.kmeans(whitenned_learning_set, kCentroids, cIter)
    
    (codes, dist) = spy.vq.vq(whitenned_learning_set, centroids_codebook)
    
    elapsed_time = time.time() - start_time
    
    np.save(os.path.join(images_path, 'centroids_codebook'), centroids_codebook)
    np.save(os.path.join(images_path, 'standard_deviations'), standard_deviations)
    np.save(os.path.join(images_path, 'vq_codes_obs'), codes)
    np.save(os.path.join(images_path, 'vq_dist'), dist)
    
    print 'It took %ds to calculate and save files' % elapsed_time
    

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
