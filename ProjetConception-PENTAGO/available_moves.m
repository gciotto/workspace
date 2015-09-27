%%  FUNCTION available_moves
%   This function calculates all possible moves of a matrix of states and
%   returns another matrix containing all of them.
%   AUTHORS:    Gustavo CIOTTO PINTON
%               Marcelo MARQUES FREIRE DE CARVALHO
function moves = available_moves(matrix_states)

    moves = [];

    for l = 1:6
        for c = 1:6
   
            if matrix_states (l,c) == 0
                moves = [moves ; [l c]];
            end
            
        end
    end


end