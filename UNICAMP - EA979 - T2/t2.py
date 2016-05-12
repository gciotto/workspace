import numpy as np
import matplotlib.pyplot as plt
from iaffine import iaffine 

G = np.ones((50, 50)) * 255.0
G[5:16, 5] = 0
G[15, 5:12] = 0

plt.figure()
plt.imshow(G, cmap = plt.get_cmap('gray'))

T = np.array([[1, 0, -5], [0, 1, -5], [0, 0, 1] ])

G1 = iaffine(G, T)

plt.show()

plt.figure()
plt.imshow(G1, cmap = plt.get_cmap('gray'))
plt.show()

T2 = np.array([[0.5, 0, 0], [0, 0.5, 0], [0, 0, 1]])
G2 = iaffine(G1, T2)
print(T2)

plt.figure()
plt.imshow(G2, cmap = plt.get_cmap('gray'))
plt.show()

T3 = np.array([[0,-1, 0], [1, 0, 0], [0, 0, 1]])
G3 = iaffine(G2, T3)

plt.figure()
plt.imshow(G3, cmap = plt.get_cmap('gray'))
plt.show()

T4 = np.array([[1, 0, 40], [0, 1, 40], [0, 0, 1] ])
G4 = iaffine(G2, T4)

T_t = T4.dot(T3).dot(T2).dot(T)
T_t2 = T.dot(T2).dot(T3).dot(T4)
print (T_t)
print (T_t2)

G4 = iaffine(G, T_t)

plt.figure()
plt.imshow(G4, cmap = plt.get_cmap('gray'))
plt.show()

np.set_printoptions(threshold=np.nan)
#print(G)
#print(G1)