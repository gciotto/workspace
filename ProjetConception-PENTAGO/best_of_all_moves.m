%%  FUNCTION best_of_all_moves
%   This function calculates the best moves with the best score (based on
%   our evaluation function).
%   It receives the matrix of states, the player who finds the best
%   solution and the depth of search (number of recursive calls).
%   It returns the best move, along with the direction and quadrant of
%   rotation and its score.
%   AUTHORS:    Gustavo CIOTTO PINTON
%               Marcelo MARQUES FREIRE DE CARVALHO
function [bestMove bestQuadrant bestDirection bestScore] = best_of_all_moves(state_matrix, player, depth)

    bestScore = -inf;
    bestMove = [];
    bestQuadrant = 0;
    bestDirection = 0;
    score_ = 0;
    directions = ['L' 'R'];

    % verifies which player is the opponent
    if player == 'B'
        player_ = 'W';
    else
        player_ = 'B';
    end

    % verifies each possible move.
    for l = 1:6
        for c = 1:6

            if state_matrix(l,c) == 0

                % simulates a move
                state_matrix(l, c) = player;

                % verifies all possible quadrants
                for quadrant = 1:4

                    for dir = directions

                        % simulates a rotation
                        state_matrix = rotate_quadrant(state_matrix, quadrant, dir);

                        if depth == 0
                            score_ = evaluate_board(state_matrix, player);
                        else

                            [auxMove auxQuadrant auxDirection auxScore] = ...
                                best_of_all_moves(state_matrix, player_, depth-1);

                            score_ = score_ + auxScore;

                        end

                        % saves the best score, along with coordinates and
                        % information concerning the rotation.
                        if score_ > bestScore

                            bestScore = score_;
                            bestQuadrant = quadrant;
                            bestDirection = dir;

                            bestMove = [l c];

                        end

                        
                        % restores rotation
                        if dir == 'L'
                            state_matrix = rotate_quadrant(state_matrix, quadrant, 'R');
                        else
                            state_matrix = rotate_quadrant(state_matrix, quadrant, 'L');
                        end

                    end

                end

                % restores move
                state_matrix(l, c) = 0;

            end

        end
    end


end