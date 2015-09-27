%% FUNCTION rotate_quadrant
%  This function rotates one of the 4 possibles quadrants composing the 
%  board. 
%  AUTHORS: Gustavo CIOTTO PINTON
%           Marcelo MARQUES FREIRE DE CARVALHO
function matrice = rotate_quadrant (matrice, quadrant, direction)

    % Identifies which quadrant we should apply the rotation over.
    sub = select_submatrice(matrice, quadrant);
    
    % Rotates the respective quadrant.
    sub = subrotate(sub, direction);
    
    % Refreshes board with the new quadrant.
    matrice= transplant(matrice, quadrant,sub);
end


function sub = select_submatrice(matrice, quadrant)
    if quadrant == 1
        sub = matrice(1:3,1:3);
    elseif quadrant == 2
        sub = matrice (1:3,4:6);
    elseif quadrant == 3
        sub = matrice (4:6,1:3);
    elseif quadrant == 4
        sub = matrice (4:6, 4:6);
    end
end

function sub = subrotate(sub, direction)
    if(direction == 'R')
        sub = (flipud(sub))';
    elseif(direction == 'L')
        sub = flipud(sub');
    end
end

function matrice = transplant(matrice, quadrant, sub)
    if quadrant == 1
        matrice(1:3,1:3) = sub;
    elseif quadrant ==2
        matrice (1:3,4:6) = sub;
    elseif quadrant ==3
        matrice (4:6,1:3) = sub;
    elseif quadrant == 4
        matrice (4:6, 4:6) = sub;
    end
end