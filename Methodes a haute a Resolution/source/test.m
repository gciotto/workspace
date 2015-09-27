%%   Programme test.m 
%   Gustavo CIOTTO PINTON, Augustin HOFF
%   Supelec 2014
%   Programme testant la fonction P_music

%% Parametres initiaux
clear all; close all; clc;

N = 100;            % nombre de points mesures 
n = 0: 1 : N - 1;

P = 3;              % nombre de frequences differentes
T = 1/N;            
fDebut = 1e0;       % frequence la plus petite recherchee
fFin   = 7e1;       % frequence la plus haute recherchee           
fPas   = 1e-1; 

%% Informations sur les signaux complexes
A = [1 2.5 3];        % amplitudes
F = [4.9e1 5e1 6e1];  % frequences recherchees
Theta = 2*pi*[rand(1) rand(1) rand(1)]; % phases aleatoires 
                                         % uniformement reparties 
                                         % sur [0,2pi[

SNR = [0.001 40 100 500]; %(dB)

bAux = randn(1,N);

for l = 1 : size(SNR, 2) 
    
    varianceNoise = sum (A.*A) / ( 10 ^ (SNR(l) / 10)); % variance p du 
                                                        %1 bruit blanc
                            
    s = zeros (1, N);  

    for k = 1:P
        s = s + A(k) * exp(2*pi*1i*n*T*F(k) + 1i*Theta(k));
    end

    %% Calcul du bruit blanc et du signal d'entree
    b = sqrt(varianceNoise) * bAux;
    
    x = s + b;  % le signal est donc la somme du bruit avec 
                % les parties complexes

         
    %% Calcus des pics
    frequences = fDebut : fPas : fFin; % vecteur des frequences parcourues
    result = P_music (x, frequences, P);

    %% Affichage des resultats
    figure 
    plot(n, abs(x));
    grid on
    title('Signal x[k]');
    xlabel ('k')
    legend (['SNR ' num2str(SNR(l),3) ' dB']);
    ylabel ('x[k]')
    
    figure    
    subplot(2,1,1);
    plot(frequences, result);
    grid on
    title('Pics trouves par la fonction P_{music}');
    xlabel ('Frequence (Hz)')
    legend (['SNR ' num2str(SNR(l),3) ' dB']);
    ylabel ('P_{music} (f)')
    
    subplot(2,1,2)
    plot (0 : 1/N : 1 - 1/N ,abs(fft (x)));
    grid on
    title(['Transformee de Fourier de x[k] pour N = ' num2str(N,3)]);
    xlabel ('Frequence (v)')
    legend (['SNR ' num2str(SNR(l),3) ' dB']);
    ylabel ('| X(v) |')

end 
