close all;
clear all;

G = zeros(50);

G(5:10, 5) = 255;
G(10, 5:8) = 255;

T1 = [ [1, 0, -5], [0, 1, -5], [0, 0, 1]]

G1 = G * T1

figure 
imagesc(G);
colormap(flipud(gray));
grid on;
grid minor;