################################################
# References
# https://www.cs.auckland.ac.nz/courses/compsci708s1c/lectures/Glect-html/topic4c708FSC.htm#tamura
# http://people.kmi.open.ac.uk/stefan/www-pub/howarth-rueger-2004-civr-texture.pdf 

import numpy as np
from scipy import signal

threshold = 25 # Threshold for Sobel filter
neigh = 20 # Neighborhood size for finding peaks in directional histogram

#Convert RGB image to Gray Scale
def rgb2gray(rgb):
    return np.dot(rgb[...,:3], [0.2989, 0.5870, 0.1140])

#Calculate the pixels mean value
def mean(hist,nPixels):
    m = 0.0
    for i in np.arange(256):
        m += i*hist[i]
    return m/nPixels

#Calculate the n-order central moment
def moment(n,hist,mean,nPixels):
    v = 0.0
    for i in np.arange(256):
        v += (i - mean)**n *hist[i]
    return v/nPixels

#Calculate contrast of the gray image
def contrast(gray):
    n = 0.25
    nPixels = gray.size
    hist, bins = np.histogram(gray,256)
    m = mean(hist,nPixels)
    sigma2 = moment(2,hist,m,nPixels)
    sigma = np.sqrt(sigma2)
    kurtosis = moment(4,hist,m,nPixels)/(sigma2**2)
    return sigma / (kurtosis**n)

#Applies Sobel filter to the gray image with the specified threshold for edges strength
def sobelFilter(gray,threshold):
    sobelX = np.array([[-1,0,1],[-1,0,1],[-1,0,1]])
    sobelY = np.array([[1,1,1],[0,0,0],[-1,-1,-1]])

    gradX = signal.convolve2d(gray,sobelX,boundary='wrap',mode='same')
    gradY = signal.convolve2d(gray,sobelY,boundary='wrap',mode='same')
    edgeStrength = 0.5*(np.abs(gradX) + np.abs(gradY))
    edgeStrength *= 255/np.max(edgeStrength)

    #Applies threshold transformation to edges strength
    T = np.arange(256)
    T[T < threshold] = 0
    edgeStrength = edgeStrength.astype(np.uint8)
    edgeStrength = T[edgeStrength]

    directAngle = np.zeros(gray.shape)
    rows, columns = gray.shape
    for i in range(rows):
        for j in range(columns):
            if(gradX[i,j] != 0):
                directAngle[i,j] = np.arctan(gradY[i,j]/gradX[i,j])
            else:
                directAngle[i,j] = np.pi/2

    directAngle[directAngle < 0.0] += np.pi
    directAngle *= 180/np.pi
    directAngle = np.mod(directAngle.astype(np.uint8),180)

    return (edgeStrength,directAngle)

#Calculate directional histogram
def histDirect(directAngle,edgeStrength):
    nBins = 180
    hist = np.zeros(nBins)
    bins = np.arange(nBins+1)
    rows,columns = directAngle.shape
    for i in range(rows):
        for j in range(columns):
            if(edgeStrength[i,j] > 0):
                hist[directAngle[i,j]] += 1
    return (hist,bins)

#Find peaks of directional histogram
def findPeaks(histD,neigh):
    nPeaks = 0
    less = np.zeros(neigh) 
    more = np.zeros(neigh)
    peaks = np.array([])
    j = 0
    while(j < 180): #Verify if j is the index of a peak
        found = True
        for i in range(neigh):
            less[i] = j-i-1 #indexes of elements to the left of j
            more[i] = j+i+1 #indexes of elements to the right of j
        less[less<0] += 180
        more[more>179] -= 180
        for i in np.arange(neigh-1):
            if(histD[less[i]] >= histD[j] or histD[j] <= histD[more[i]]):
                found = False
                break
        if(histD[less[neigh-1]] > histD[j] or histD[j] < histD[more[neigh-1]]):
            found = False
        if(found):
            peaks = np.insert(peaks,nPeaks,j)
            nPeaks += 1
            j += neigh - 1

        j += 1
    return peaks

#Calculate ranges of peaks
def findRanges(peaks, hist):
    nPeaks = peaks.size
    if(nPeaks > 1):
        rangePeaks = np.zeros(nPeaks)
    else:
        rangePeaks = np.zeros(nPeaks+1)

    #Left limit for firstpeak
    if(nPeaks > 1):
        rangeSize = peaks[0] - peaks[nPeaks-1] + 180
        angles = np.arange(rangeSize-1) + peaks[nPeaks-1] + 1
        angles[angles>=180] -= 180
        minAng = peaks[nPeaks-1]
        for j in angles:
            if(hist[minAng] > hist[j]):
                minAng = j
        rangePeaks[0] = minAng
    else:
        rangePeaks[0] = peaks[0]-90
        if(rangePeaks[0] < 0): rangePeaks[0] +=180
        rangePeaks[1] = peaks[0]+90
        if(rangePeaks[1] >=180): rangePeaks[1] -=180

    #Limits for other peaks    
    for i in np.arange(nPeaks-1)+1:
        rangeSize = peaks[i] - peaks[i-1]
        angles = np.arange(rangeSize-1) + peaks[i-1] + 1
        minAng = peaks[i-1]
        for j in angles:
            if(hist[minAng] > hist[j]):
                minAng = j
        rangePeaks[i] = minAng
        
    return rangePeaks

#Calculate degree of directionality of the gray image
def degreeDirect(gray, threshold,neigh):

    sum = 0

    edgeStrength,directAngle = sobelFilter(gray,threshold)
    histD,bins = histDirect(directAngle,edgeStrength)
    peaks = findPeaks(histD,neigh)
    rangePeaks = findRanges(peaks,histD)

    nPeaks = peaks.size
    sumHist = np.sum(histD)
    r = 10
    quant = 179 # quantizing leves
    
    # First Peak
    rangeSize = rangePeaks[1] - rangePeaks[0]
    if(rangeSize <= 0):
        rangeSize += 180
        angles = np.arange(rangeSize) + rangePeaks[0]
        if(rangePeaks[0] > peaks[0]): diff = angles - 180
        else: diff = np.copy(angles)
        angles[angles >= 180] -= 180
    else:
        angles = np.arange(rangeSize) + rangePeaks[0]
        diff = angles
    for j in np.arange(rangeSize):
        sum += ((diff[j] - peaks[0])/quant)**2 * histD[angles[j]]/sumHist
        
    #Peaks in the middle
    for i in np.arange(nPeaks-2)+1:
        rangeSize = rangePeaks[i+1] - rangePeaks[i]
        angles = np.arange(rangeSize) + rangePeaks[i]
        for j in angles:
            sum += ((j - peaks[i])/quant)**2 * histD[j]/sumHist

    #Last peak
    rangeSize = rangePeaks[0] - rangePeaks[nPeaks-1]
    if(rangeSize < 0):
        rangeSize += 180
        angles = np.arange(rangeSize) + rangePeaks[nPeaks-1]
        diff = np.copy(angles)
        angles[angles >= 180] -= 180
    else:
        angles = np.arange(rangeSize) + rangePeaks[nPeaks-1]
        diff = angles
    for j in np.arange(rangeSize):
        sum += ((diff[j] - peaks[nPeaks-1])/quant)**2 * histD[angles[j]]/sumHist

    sum *= r*nPeaks
    return 1 - sum

#### JOSEPH Codes

def coarseness(gray):
    
    (rows, cols) = gray.shape
    
    f1 = np.ones((2,2))/(2**2)
    f2 = np.ones((4,4))/(2**4)
    f3 = np.ones((8,8))/(2**6)
    f4 = np.ones((16,16))/(2**8)
    f5 = np.ones((32,32))/(2**10)
    f6 = np.ones((64,64))/(2**12)
    
    kArr = []
    kArr.append(signal.convolve2d(gray,f1,boundary='symm',mode='same'))
    kArr.append(signal.convolve2d(gray,f2,boundary='symm',mode='same'))
    kArr.append(signal.convolve2d(gray,f3,boundary='symm',mode='same'))
    kArr.append(signal.convolve2d(gray,f4,boundary='symm',mode='same'))
    kArr.append(signal.convolve2d(gray,f5,boundary='symm',mode='same'))
    kArr.append(signal.convolve2d(gray,f6,boundary='symm',mode='same'))
    
    counter = 0        
    sm = 0
    ak1 = 0
    ak2 = 0
    
    kErr = []
    for k in range (0,6):
        kErr.append(np.zeros((rows,cols)))
    for k in range (0,6):
        for i in range (0,rows):
            for j in range(0,cols):
                if (i - 1) >= 0:
                    ak1 = abs(kArr[k][i-1][j] - kArr[k][i][j])
                if (i + 1) < rows:
                    ak2 = abs(kArr[k][i+1][j] - kArr[k][i][j])
                if ak1 >= ak2:
                    sm += ak1
                else:
                    sm += ak2
                ak1 = 0
                ak2 = 0
                if (j - 1) >= 0:
                    ak1 = abs(kArr[k][i][j-1] - kArr[k][i][j])
                if (j + 1) < cols:
                    ak2 = abs(kArr[k][i][j+1] - kArr[k][i][j])
                if ak1 >= ak2:
                    sm += ak1
                else:
                    sm += ak2
                kErr[k][i][j] = sm
                if sm > 0:
                    counter = counter + 1
                sm = 0
            
    arrkMax = np.zeros((rows,cols))
    for i in range(0,rows):
        for j in range(0,cols):
            arrkMax[i][j] = 2
            eMax = kErr[0][i][j]
            for k in range (1,6):
                if kErr[k][i][j] > eMax:
                    eMax = kErr[k][i][j]
                    arrkMax[i][j] = 2**(k+1)
    return arrkMax