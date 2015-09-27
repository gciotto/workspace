%%  Fonction P_music.m 
%   Gustavo CIOTTO PINTON, Augustin HOFF
%   Supelec 2014
%   
%   Parametres: signal_x  : signal d'entree (vecteur 1xN)
%               frequences: vecteur des frequences recherchees 
%                           (vecteur 1xM)
%               P         : nombre de signaux complexes
%
%   Sortie:     resultat  : vecteur avec les resultats pour chaque
%                           frequence (vecteur 1xM)

function [ resultat ] = P_music( signal_x,  frequences , P)

N = size(signal_x ,2);
T = 1/N;

%% Calcul de la matrice d'autocorrelation     
z   = corrmtx( signal_x, 0 );
Rxx = toeplitz(z);

%% Calcul des valeurs et vecteurs propres de la matrice d'autocorrelation
[mVecteursPropres, mValeursPropres] = eig(Rxx);

% classement dans l'ordre croissant des valeurs propres  
[valeursPropresClassees,ordre] = sort(abs(diag(mValeursPropres)));

% valeurs propres correspondantes au sous espace du bruit
valeursPropresBruit = valeursPropresClassees (1:N-P);

% indices correspondant aux N-P plus petites vap du bruit
ordreBruit = ordre (1:N-P);

%% Calcul de la somme des produits v*vH (ou v est un des vecteurs propres  
%% qui construisent le sous espace du bruit et vH est son transpose  
%% conjugue) selon equation 18
resultatPartiel = zeros( N, N );

% on sait qu'il en y a N - P. Pour chacun d'eux, on calcule le produit
% correspondant et on l'ajoute au resultat partiel initialise a la matrice
% nulle
for k = 1:N-P

    indice = ordreBruit(k);
    vecteurPropreBruit = mVecteursPropres(:, indice);
 
    % variable auxiliaire pour le calcul v*vH
    aux = vecteurPropreBruit * conj (vecteurPropreBruit');
    
    resultatPartiel = resultatPartiel + aux;
 
end

%% Pour chacune des frequences, on construit le vecteur exponentiel (eq. 12)
%  et le multiplie par la somme des v*vH. Par fin, on applique l'eq. 19.
NFrequences = size(frequences, 2);
resultat = zeros (1, NFrequences );

for n = 1:NFrequences 

    % vecteur exponentiel (eq 2)
    temps = (0 : 1 : (N-1))';
    eFrequences = exp(2*pi*1i*T*frequences(n)*temps);

    % equation 18
    aux = conj(eFrequences') * resultatPartiel * eFrequences;

    resultat (n) = abs (1/aux); % equation 19
end

