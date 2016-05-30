################################################
# References
# https://www.cs.auckland.ac.nz/courses/compsci708s1c/lectures/Glect-html/topic4c708FSC.htm#tamura
# http://people.kmi.open.ac.uk/stefan/www-pub/howarth-rueger-2004-civr-texture.pdf 

import numpy as np
from scipy.stats import kurtosis
from scipy.signal import convolve2d
from PIL import Image

import matplotlib.pyplot as plt
from sklearn.ensemble._gradient_boosting import np_zeros

def tamura_neighbourhood_average(x, y, k, img):    

    img_w, img_h = img.size

    sum = 0
    
    for i in range (-2 ** (k - 1), 2 ** (k - 1)):
        for j in range (-2 ** (k - 1), 2 ** (k - 1)):
            if (x + i) >= 0 and (x + i) < img_w:
                if (y + j) >= 0 and (y + j) < img_h:
                    #print (x + i, y + j)
                    sum = sum + img.getpixel((x + i, y + j))

    return sum / (2 ** (2 * k))

def tamura_coarseness(img, k_max = 7, crop_image = False):

    img_size = img.size

    print img_size

    coarseness_m = np.zeros(img_size)
    coarseness_x_y_h = np.zeros(k_max)
    coarseness_x_y_v = np.zeros(k_max)
                        
    for x in range (img_size[0]) :
        for y in range (img_size[1]) :
            
            #print 'Pixel (%d, %d)' % (x,y)
            
            for k in range(1, k_max) :
                
                coarseness_x_y_h[k -1] = abs(tamura_neighbourhood_average(x + 2 ** (k - 1), y, k, img) - tamura_neighbourhood_average(x - 2 ** (k - 1), y, k, img))
                coarseness_x_y_v[k -1] = abs(tamura_neighbourhood_average(x, y + 2 ** (k - 1), k, img) - tamura_neighbourhood_average(x, y - 2 ** (k - 1), k, img))          
            
            k_max_h = np.where(coarseness_x_y_h == coarseness_x_y_h.max())
            k_max_v = np.where(coarseness_x_y_v == coarseness_x_y_v.max())
            
            if (k_max_h[0][0] == k_max_v[0][0]):
                k_max_i = k_max_h[0][0]
            else : 
                k_max_i = k_max_v[0][0]
            
            coarseness_m[x,y] = 2 ** k_max_i
    
    return np.average(coarseness_m)

def tamura_contrast(hist):
        
    return np.sqrt(np.var(hist)) / (kurtosis(hist, fisher = False) ** 0.25)

def tamura_directionality(img, edge_threshold = 350, n_bins = 32):
    
    filter_h = np.array([[-1,0,1], [-1,0, 1], [-1,0, 1]])
    filter_v = np.array([[1, 1, 1], [0, 0, 0], [-1, -1, -1]])

    edges_h = convolve2d(img, filter_h, mode='same', boundary='wrap')
    edges_v = convolve2d(img, filter_v, mode='same', boundary='wrap')
          
    edges_x_y = 0.5 * (np.abs(edges_h) + np.abs(edges_v))
    angles_x_y = np.arctan (edges_v / (edges_h  + 1e-10))
    
    hist_angles, bins_angles = np.histogram(angles_x_y, bins = n_bins)
        
    H_e = np_zeros(n_bins)
    
    for i in range (n_bins):
        if i == 31:
            indices = np.where(np.logical_and(angles_x_y >= bins_angles[i], angles_x_y <= bins_angles[i + 1]))
            
        else:  indices = np.where(np.logical_and(angles_x_y >= bins_angles[i], angles_x_y < bins_angles[i + 1]))
        
        H_e[i] = np.count_nonzero(edges_x_y[indices] >= edge_threshold)
        
    sum = 0
    
    (x_indices, y_indices) = np.where(edges_x_y >= edge_threshold) 
    
    for i in range(len(x_indices)):
        angle = angles_x_y[x_indices[i], y_indices[i]]
        
        i_bin = 1
        while (angle > bins_angles[i_bin]) and (i_bin < n_bins):
            i_bin = i_bin + 1
        
        sum = sum + (angle - bins_angles[i_bin - 1]) ** 2 * H_e[i_bin - 1]
        
    return sum
        
        
     