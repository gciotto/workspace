'''
Created on Apr 10, 2016

@author: gciotto
'''
import scipy.signal
import numpy as np

f = np.array([[1,0,0, 1], [1, 0,0,1], [1,1,0, 1], [1,1,1,0]])
h = np.array([[0,0,0],[1,1,0],[0,1,0]])

print ('f = \n', f)
print ('h = \n', h)

print('f*h =\n', scipy.signal.convolve(f, h))

f = np.array([[1,0,0], [1, 0,0], [1,1,0]])
h = np.array([[-1,0,0],[1,1,0],[0,1,-2]])
 
print ('f = \n', f)
print ('h = \n', h)

print('f*h =\n', scipy.signal.convolve(f, h))