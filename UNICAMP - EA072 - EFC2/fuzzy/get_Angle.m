%%  FUNCAO get_Angle
%   Funcao responsavel por calcular o desvio no angulo do robo com base na
%   regras que foram ativadas pelos sensores.
%   Parametros: active_rules possui as regras que foram ativadas e suas
%   respectivas pertinencias.
%   Retorno:    o desvio que devera ser feito na direcao do robo
%   Autor: Gustavo CIOTTO PINTON
function angle = get_Angle (active_rules)

% Discretizacao do intervalo dos possiveis angulos.
available_angles = -20:0.5:20;

% c_values contera 
c_values = zeros (5, length(available_angles));

parameters = [ -20 -20 -15 -10 0 ;
               -15 -10  -5   0 0 ;
               -5    0   0   5 0 ;
                0    5  10  15 0 ;
                10  15  20  20 0 ];

for k = 1: length(available_angles)
    
    for n = 1:5
    
        c_values(n, k) = min (active_rules(n), trap_pertinencia(available_angles(k), ...
            parameters(n ,1),  parameters(n ,2), parameters(n ,3), parameters(n ,4), ...
            parameters(n ,5)));
    end
    
end

max_values = max(c_values);

angle = sum(available_angles.*max_values)/sum(max_values);

angle = angle*pi/180;

end