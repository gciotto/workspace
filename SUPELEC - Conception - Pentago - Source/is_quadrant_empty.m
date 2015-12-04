%%  FUNCTION is_quadrant_empty
%   This functions verifies whether a specific quadrant is empty or not. A
%   quadrant is empty if and only if no move has been done in it.
%   It returns a boolean indicating if that quadrant is empty (true) or
%   not.
%   AUTHOR: Gustavo CIOTTO PINTON
%           Marcelo MARQUES FREIRE DE CARVALHO
function isEmpty = is_quadrant_empty(state_matrix, quadrant)
     
    % takes sub matrix corresponding to that quadrant
    if quadrant == 1
        quadrant_matrix = state_matrix(1:3,1:3);
    elseif quadrant == 2
        quadrant_matrix = state_matrix(1:3,4:6);
    elseif quadrant == 3
        quadrant_matrix = state_matrix(4:6,1:3);
    elseif quadrant == 4
        quadrant_matrix = state_matrix(4:6, 4:6);
    end
    
    % verifies if it is empty
    if all (quadrant_matrix == 0)
        isEmpty = 1;
    else
        isEmpty = 0;
    end

end