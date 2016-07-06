'''
# References
http://people.kmi.open.ac.uk/stefan/www-pub/howarth-rueger-2004-civr-texture.pdf
'''

import numpy as np
from scipy import misc
import scipy.cluster as spy
import os
import time
from tamura import contrast, degreeDirect, coarseness, threshold, neigh, rgb2gray

images_path = "/home/gciotto/images_flicker"

n_images = 25000

# Parametros utilizados pelo KMEANS
kCentroids_features = 200
cIter_features = 100

kCentroids = 20
cIter = 100

# Escolha dos FEATURES
useGreyScale = True
useTamuraCoarseness = True
useTamuraContrast = True
useTamuraDirectionality = True
useGarbor = True

if useGreyScale:
    n_columns_histogram = 256    
else: 
    n_columns_histogram = 768

n_colums_features = 0
n_kernels = 0

failures_directionality = []
failures_contrast = []
failures_coarseness = []
failures_garbor = []

print os.path.join(images_path, '/features/histograms.npy')

histogram_hasBeenCalculated = os.path.isfile('%s/features/histograms.npy' % images_path)

if histogram_hasBeenCalculated:
    learning_set = np.load('%s/features/histograms.npy' % images_path)
else : learning_set = np.zeros((n_images, n_columns_histogram))

if useTamuraCoarseness:

    n_colums_features = n_colums_features + 1
    
    tamura_coarseness_hasBeenCalculated = os.path.isfile('%s/features/tamura_coarseness.npy' % images_path)
    
    if tamura_coarseness_hasBeenCalculated:
        tamura_coarseness_v = np.load('%s/features/tamura_coarseness.npy' % images_path)
    else : tamura_coarseness_v = np.zeros(25000)
    
if useTamuraContrast:
    
    n_colums_features = n_colums_features + 1
    
    tamura_contrast_hasBeenCalculated = os.path.isfile('%s/features/tamura_contrast.npy' % images_path)
    
    if tamura_contrast_hasBeenCalculated:
        tamura_contrast_v = np.load('%s/features/tamura_contrast.npy' % images_path)
    else : tamura_contrast_v = np.zeros(25000)

    
if useTamuraDirectionality:
    
    n_colums_features = n_colums_features + 1
    
    tamura_directionality_hasBeenCalculated = os.path.isfile('%s/features/tamura_directionality.npy' % images_path)
    
    if tamura_directionality_hasBeenCalculated:
        tamura_directionality_v = np.load('%s/features/tamura_directionality.npy' % images_path)
    else : tamura_directionality_v = np.zeros(25000)


if useGarbor:
    
    from garbor import garbor_features, kernels
    
    n_kernels = len(kernels)
    
    n_colums_features = n_colums_features + len(kernels) * 2
    
    garbor_hasBeenCalculated = os.path.isfile('%s/features/garbor.npy' % images_path)
    
    if garbor_hasBeenCalculated:
        garbor_v = np.load('%s/features/garbor.npy' % images_path)
    else : garbor_v = np.zeros( 25000 * len(kernels) * 2)
    

if __name__ == '__main__':

    
    r = 0;
    
    for r in range (25000):
            
            img = None
            
            print 'Calculating histogram for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
                        
            if not histogram_hasBeenCalculated:
                                
                if os.path.isfile(os.path.join(images_path, 'hist_%d.npy' % (r + 1))):
                    hist_RGB = np.load(os.path.join(images_path, 'hist_%d.npy' % (r + 1)))
                else:
                    
                    if img is None:
                    
                        img = misc.imread(os.path.join(images_path, 'im%d.jpg' % (r + 1) ))
                
                        if useGreyScale:
                            img = rgb2gray(img)
                    
                    
                    hist_RGB, bins = np.histogram(img, n_columns_histogram)
                    np.save(os.path.join(images_path, 'hist_%d.npy' % (r + 1)), hist_RGB)
                        
                learning_set[r, :] = hist_RGB[:]
                       
            if useTamuraCoarseness:
                
                if not tamura_coarseness_hasBeenCalculated:
                    if os.path.isfile(os.path.join(images_path, 'tamura_coarseness_%d.npy' % (r + 1))):
                        tamura_coarseness_v[r] = np.load(os.path.join(images_path, 'tamura_coarseness_%d.npy' % (r + 1)))
                    else : 
                
                        if img is None:
                    
                            img = misc.imread(os.path.join(images_path, 'im%d.jpg' % (r + 1) ))
                
                            if useGreyScale:
                                img = rgb2gray(img)
                
                        print 'Calculating Tamura COARSENESS for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
                        
                        try:
                            start_time = time.time()   
                            tamura_coarseness_v[r] = coarseness(img)
                            elapsed_time = time.time() - start_time
                            print 'It took %ds to calculate COARSENESS...' % elapsed_time
                            np.save(os.path.join(images_path, 'tamura_coarseness_%d.npy' % (r + 1)),tamura_coarseness_v[r])
                            
                        except:
                            failures_coarseness.append(r+1)
                    
                    print tamura_coarseness_v[r] 
                    
            if useTamuraContrast:
                                                
                if not tamura_contrast_hasBeenCalculated:
                    
                    if os.path.isfile(os.path.join(images_path, 'tamura_contrast_%d.npy' % (r + 1))):
                        tamura_contrast_v[r] = np.load(os.path.join(images_path, 'tamura_contrast_%d.npy' % (r + 1)))
                    else : 
                        
                        if img is None:
                    
                            img = misc.imread(os.path.join(images_path, 'im%d.jpg' % (r + 1) ))
                
                            if useGreyScale:
                                img = rgb2gray(img)
                        
                        
                        print 'Calculating Tamura CONTRAST for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
                        
                        try:
                            start_time = time.time()   
                            tamura_contrast_v[r] = contrast(img)
                            elapsed_time = time.time() - start_time
                            print 'It took %ds to calculate CONTRAST...' % elapsed_time
                            np.save(os.path.join(images_path, 'tamura_contrast_%d.npy' % (r + 1)),tamura_contrast_v[r])
                        except:
                            failures_contrast.append(r+1)
                    
                    #print tamura_contrast_v[r]
            
            if useTamuraDirectionality:
                                                
                if not tamura_directionality_hasBeenCalculated:
                    
                    if os.path.isfile(os.path.join(images_path, 'tamura_directionality_%d.npy' % (r + 1))):
                        tamura_directionality_v[r] = np.load(os.path.join(images_path, 'tamura_directionality_%d.npy' % (r + 1)))
                    else : 
                        
                        if img is None:
                    
                            img = misc.imread(os.path.join(images_path, 'im%d.jpg' % (r + 1) ))
                
                            if useGreyScale:
                                img = rgb2gray(img)
                        
                        print 'Calculating Tamura DIRECTIONALITY for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
                        
                        
                        try:                        
                            start_time = time.time()   
                            tamura_directionality_v[r] = degreeDirect(img, threshold, neigh)
                            elapsed_time = time.time() - start_time
                            np.save(os.path.join(images_path, 'tamura_directionality_%d.npy' % (r + 1)),tamura_directionality_v[r])
                            
                        except:
                            failures_directionality.append(r+1)
                            
                        
                        print 'It took %ds to calculate DIRECTIONALITY...' % elapsed_time
                        
                        
                    
                    #print tamura_directionality_v[r]
            
            if useGarbor:
                
                start_i = len(kernels)*r * 2
                stop_i = start_i + len(kernels) * 2
                
                if not garbor_hasBeenCalculated:
                    
                    if os.path.isfile(os.path.join(images_path, 'garbor_%d.npy' % (r + 1))):
                        garbor_v[ start_i : stop_i ] = np.load(os.path.join(images_path, 'garbor_%d.npy' % (r + 1)))
                    
                    else:
                        
                        if img is None:
                    
                            img = misc.imread(os.path.join(images_path, 'im%d.jpg' % (r + 1) ))
                
                            if useGreyScale:
                                img = rgb2gray(img)
                        
                        print 'Calculating GARBOR for file ', os.path.join(images_path, 'im%d.jpg' % (r + 1))
                        
                        try:
                            start_time = time.time()   
                            garbor_v[ start_i : stop_i ] = np.resize(garbor_features(img, kernels) , (1, len(kernels) * 2))
                            elapsed_time = time.time() - start_time
                            
                            print 'It took %ds to calculate GARBOR...' % elapsed_time
                            
                            np.save(os.path.join(images_path, 'garbor_%d.npy' % (r + 1)), garbor_v[ start_i : stop_i ])
                            
                        except:
                            failures_garbor.append(r+1) 
    
    print 'Failures COARSENESS ', failures_coarseness
    print 'Failures CONTRAST ', failures_contrast
    print 'Failures DIRECTIONALITY ', failures_directionality
    print 'Failures GARBOR ', failures_garbor   
    
    if not histogram_hasBeenCalculated:
        np.save('%s/features/histograms.npy' % images_path, learning_set)
        histogram_hasBeenCalculated = True
    
    if useTamuraCoarseness: 
        if not tamura_coarseness_hasBeenCalculated and not failures_coarseness:
            np.save('%s/features/tamura_coarseness.npy' % images_path, tamura_coarseness_v)
            tamura_coarseness_hasBeenCalculated = True
    
    if useTamuraContrast: 
        if not tamura_contrast_hasBeenCalculated and not failures_contrast:
            np.save('%s/features/tamura_contrast.npy' % images_path, tamura_contrast_v)
            tamura_contrast_hasBeenCalculated = True
    
    if useTamuraDirectionality: 
        if not tamura_directionality_hasBeenCalculated and not failures_directionality:
            np.save('%s/features/tamura_directionality.npy' % images_path, tamura_directionality_v)
            tamura_directionality_hasBeenCalculated = True
    
    if useGarbor: 
        if not garbor_hasBeenCalculated and not failures_garbor:
            np.save('%s/features/garbor.npy' % images_path, garbor_v)
            garbor_hasBeenCalculated = True
            
    
            
    print '%d images successfully preprocessed...' % (r+1)
    
    ##################################################################################################
    # Calculating 1o LEVEL K-means - Only features considered
    ##################################################################################################
    
    for cIter_features in [50, 70, 90, 100] :
    
        path_1 = "%s/arrays/level1_%d_%d" % (images_path, kCentroids_features, cIter_features)
    
        if not os.path.isdir(path_1):
            os.mkdir(path_1)
    
        print '1o level ',  kCentroids_features, cIter_features
    
        learning_set_features = np.zeros((n_images, n_colums_features))
        
        for i in range(n_images):
            
            j = 0
            
            if useTamuraCoarseness:
                learning_set_features[i, j] = tamura_coarseness_v[i]
                j = j + 1
        
            if useTamuraContrast:
                learning_set_features[i, j] = tamura_contrast_v[i]
                j = j + 1
                
            if useTamuraDirectionality:
                learning_set_features[i, j] = tamura_directionality_v[i]
                j = j + 1
                
            if useGarbor:
                
                start_i = i * len(kernels) * 2
                stop_i = start_i + len(kernels) * 2 
                
                learning_set_features[i, j : j + len(kernels) * 2] = garbor_v[ start_i : stop_i ]
                j = j + len(kernels) * 2
        
        whitenned_learning_set_features = spy.vq.whiten(learning_set_features)
        
        standard_deviations_features = np.zeros (n_colums_features)
        
        for i in range(n_colums_features):
            standard_deviations_features[i] = learning_set_features[0, i] / whitenned_learning_set_features [0, i]
            
        (centroids_codebook_features, distortion_features) = spy.vq.kmeans(whitenned_learning_set_features, kCentroids_features, cIter_features)
        
        (codes_features, dist_features) = spy.vq.vq(whitenned_learning_set_features, centroids_codebook_features)
        
        np.save('%s/centroids_codebook_features_%d_%d_%s_%s_%s_%s' % (path_1, kCentroids_features, cIter_features, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), centroids_codebook_features)
        np.save('%s/standard_deviations_features_%d_%d_%s_%s_%s_%s' % (path_1, kCentroids_features, cIter_features, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), standard_deviations_features)
        np.save('%s/vq_codes_obs_features_%d_%d_%s_%s_%s_%s' % (path_1, kCentroids_features, cIter_features, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), codes_features)
        np.save('%s/vq_dist_features_%d_%d_%s_%s_%s_%s' % (path_1, kCentroids_features, cIter_features, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), dist_features)
        
        ##################################################################################################
        # Calculating 2o LEVEL K-means - Histograms considered
        ##################################################################################################
        
        for cIter in [50, 70, 90, 100] :
              
            print '2o level ',  kCentroids, cIter
              
            for i in range(kCentroids_features):
                
                path_2 = "%s/arrays/level2_%d_%d" % (images_path, kCentroids, cIter)
    
                if not os.path.isdir(path_2):
                    os.mkdir(path_2)
                
                print 'Calculating 2o-level k-means for %d i 1o-level centroid' % i
                
                images_i = np.where(codes_features == i)[0]
                
                learning_set_histograms_i = learning_set[images_i, :]
                
                whitenned_learning_set_histograms_i = spy.vq.whiten(learning_set_histograms_i)
            
                standard_deviations_histograms_i = np.zeros (n_columns_histogram)
                
                for j in range(n_columns_histogram):
                    standard_deviations_histograms_i[j] = learning_set_histograms_i[0, j] / whitenned_learning_set_histograms_i [0, j]
                    
                (centroids_codebook_histograms_i, distortion_histograms_i) = spy.vq.kmeans(whitenned_learning_set_histograms_i, kCentroids, cIter)
            
                (codes_histograms_i, dist_histograms_i) = spy.vq.vq(whitenned_learning_set_histograms_i, centroids_codebook_histograms_i)
                
                np.save('%s/centroids_codebook_images_index_%d_%d_%d_%s_%s_%s_%s' % (path_2, i , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), images_i)
                np.save('%s/centroids_codebook_histogram_%d_%d_%d_%s_%s_%s_%s' % (path_2, i , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), centroids_codebook_histograms_i)
                np.save('%s/standard_deviations_histogram_%d_%d_%d_%s_%s_%s_%s' % (path_2, i , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), standard_deviations_histograms_i)
                np.save('%s/vq_codes_obs_histogram_%d_%d_%d_%s_%s_%s_%s' % (path_2, i , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), codes_histograms_i)
                np.save('%s/vq_dist_histogram_%d_%d_%d_%s_%s_%s_%s' % (path_2, i , kCentroids, cIter, useTamuraCoarseness, useTamuraContrast, useTamuraDirectionality, useGarbor), dist_histograms_i)
