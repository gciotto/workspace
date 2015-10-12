%=========================================================================%
% EL 1 - 2 Example - Speed Measurement of Sound Propagation
%
% 2.2 Theoretical Study
%
%   2.2.1 - s(t) = B.sin(2*pi*f0*t - T) + b(t)
%   2.2.2 - We know that a cross-correlation function Cxy between two other 
%           functions x(t) and y(t) has its maximum value when x(t) and
%           and y(t) are in phase. In this case, this means that Cxy has
%           its maximum at t = T, which is exactly the value we want to
%           find.
%           We have therefore 
%               Cxy(T) = I( sin(2*pi*f*R) * sin(2*pi*f*(t+R) + T)dR + Bxy
%           For t = T/(2*pi*f) + 1/f , we have maximums and
%               t = T/(2*pi*f) + 1/(2*f) minimums
%========================================================================%

%========================================================================%
%   2.3 Writing the code
%========================================================================%

clear all; close all; clc;

distAntenna     = 3;

tSampling       = 10e-6;

f0              = 5e3;
t0              = 1/f0;
amplitude       = 1;
nbPeriods       = 6;

tSignauEmis    = 0: tSampling : (nbPeriods - 1)*t0;
signauEmis     = amplitude* sin( 2 * pi * f0 * tSignauEmis );

[rows, cols] = size(tSignauEmis);

load resacq
[rowsMesure, colsMesure] = size(SigMesure);

signauMesureL = SigMesure(1: cols);

figure
plot (TpsSigMesure, SigMesure)

figure
plot(tSignauEmis, signauEmis)
hold on
plot(tSignauEmis, signauMesureL,  'r' );
xlabel ('temps (s)');
ylabel ('amplitude (V)');
legend ('Emitted Signal', 'Measured Signal');

%========================================================================%
%   2.3.5 Computing of the Cross-Correlation
%========================================================================%

crossCorrelation = xcorr(signauEmis, SigMesure);

%  SUPPORT of CROSS CORRELATION [0 , 2M -1], M is the length of the arrays
%  Since a cross correlation may be written as a convolution product, its
%  support can be written as [a + c, b + d] where [a,b] and [c,d] are the
%  the support of x and y respectively. ** In this case, it is better to
%  use speak in CIRCULAR CONVOLUTION, since the two signals are PERIODIC
tBeginning  = 0;
tEnd        = 2*colsMesure - 1;
tXCorr      = (tBeginning: tSampling: tSampling*(tEnd-1));

figure
plot (tXCorr, crossCorrelation)
grid on
xlabel('phase (s)');
ylabel('amplitude (V^2)');
title ('Cross-Correlation between the Measured Signal and the Emmited Signal')

%========================================================================%
%   2.3.6 Estimation of the Speed
%========================================================================%

[Maxi, IndiceMax]   = max(crossCorrelation);
estimatedDelay      = tXCorr(IndiceMax);
estimatedSpeed      = -distAntenna/estimatedDelay;