%%  FUNCTION random_move
%   This function returns a random move among all the possible ones.
%   It receives a matrix of states and returns the respective move, its
%   quadrant and direction of rotation.
function [move quandrant direction] = random_move (state_matrix)

    % calculates all possible moves.
    moves = available_moves(state_matrix);

    move = moves(randi(length (moves)), :)
    
    quandrant = randi(4);
    
    dir = ['L' 'R'];
    
    direction = dir(randi(2));
    
end