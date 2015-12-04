%% FUNCTION evaluate_board
%  This function gives points for a board according to its configuration.
%  Each line/column/diagonal with 5 filled positions gives 100000, with 4,
%  10000, with 3, 1000 and with 2, 100. Filled positions with no neighbors
%  receive no points. Positions which cannot be turned ( (2,2), (2,5),
%  (5,2) and (5,5) ) receive 100 points.
%  Parameters:  state_matrix is the matrix representating the state of the
%               game and player is the player which the pontuation will be
%               given for.
%  It returns the earned points and a matrix which can used to test the
%  program.
%  AUTHORS: Gustavo CIOTTO PINTON
%           Marcelo MARQUES FREIRE DE CARVALHO
function [value hasBeenDetected] = evaluate_board(state_matrix, player)

value = 0;

% Each element of hasBeenDetected is an integer whose representation in
% binary determines if that element has already been detected taking
% place in at least one line/column or diagonal. 0001 means that it takes place in
% a COLUMN, 0010 in a LINE, 0100 in a DIAGONAL to the right and 1000 in a
% DIAGONAL to the left. For example, if hasBeenDetected(i, j) is 0011, it
% means that position forms a LINE AND a COLUMN at the same time.
hasBeenDetected = zeros(6,6);

% playerScores is an array that stores the scores of all combination the 
% player’s got so far in each one of the possible configurations (COLUMN, LINE 
% and DIAGONALS). For example, if the player has a 3-element line, then 
% playerScores(2,1) receives the score for that combination.
playerScores = zeros (4,1);
% same but for the opponent.
opponentScores = zeros (4,1);
scores = [playerScores  opponentScores];

x = 1;
foundUltraCondition = 0;

if player == 'B'
    opponent = 'W';
else
    opponent = 'B';
end

while (x <= 6) && (foundUltraCondition == 0)
    
    y = 1;
    while (y <= 6) && (foundUltraCondition == 0)
        
        % Verifies if it is a filled position
        if state_matrix (x,y) ~= 0
            
            index_player = 1;
            if state_matrix (x,y) ~= player
                index_player = 2;
            end
            
            % One of the positions that cannot be turned.
            if ( (x == 2) && ((y == 2) || (y == 5) ) ) || ...
                    ( (x == 5) && ((y == 2) || (y == 5) ) )
                
                if state_matrix (x,y) == player
                    value = value + 1;
                else
                    value = value - 1;
                end
                
            end
            
            % Verifies all possible COLUMNS
            offset_index = 4;
            while (offset_index >= 1) && (bitand(hasBeenDetected(x,y), 1) == 0) && ...
                    (foundUltraCondition == 0)                           
                    
                if (x + offset_index) <= 6
                    
                    t = state_matrix (x:(x+offset_index) , y);
                    
                    % Verifies if all elements are equal
                    if all(t == t(1))
                        
                        % Found a winning condition. We can stop already.
                        if offset_index == 4
                            foundUltraCondition = 1;                            
                        end
                        
                        scores(1, index_player) = scores(1, index_player) + 10^(3*(offset_index+1));
                        
                        % fills hasBeenDetected with 0001 in the
                        % respective COLUMN
                        hasBeenDetected (x:(x+offset_index), y) = ...
                            hasBeenDetected (x:(x+offset_index), y) + 1;
                        
                    % We need to prevent the other player to win the
                    % match
                    elseif offset_index == 4
                        
                        if (sum (t == player) == 4) && (sum (t == opponent) == 1)
                            value = value + 10^14;
                        elseif (sum (t == player) == 1) && (sum (t == opponent) == 4)
                            value = value - 10^14;
                        end
                        
                    end
                end
                
                offset_index = offset_index - 1;
            end
            
            
            % Verifies all possible LINES
            offset_index = 4;
            while (offset_index >= 1) && (bitand(hasBeenDetected(x,y), 2) == 0) && ...
                    (foundUltraCondition == 0)
                
                if (y + offset_index) <= 6
                    t = state_matrix (x , y:(y + offset_index));
                    
                    if all(t == t(1))
               
                        % Found a winning condition. We can stop already.
                        if offset_index == 4
                            foundUltraCondition = 1;
                        end
                        
                        scores(2, index_player) = scores(2, index_player) + 10^(3*(offset_index+1));
                        
                        % fills hasBeenDetected with 0010 in the
                        % respective LINE
                        hasBeenDetected (x , y:(y + offset_index)) = ...
                            hasBeenDetected (x , y:(y + offset_index)) + 2;
                        
                    % We need to prevent the other player to win the
                    % match
                    elseif offset_index == 4
                        
                        if (sum (t == player) == 4) && (sum (t == opponent) == 1)
                            value = value + 10^14;
                        elseif (sum (t == player) == 1) && (sum (t == opponent) == 4)
                            value = value - 10^14;
                        end
                        
                    end   
                end
                
                offset_index = offset_index - 1;
            end
            
            % Verifies all possible DIAGONALS to the RIGHT
            offset_index = 4;
            while (offset_index >= 1) && (bitand(hasBeenDetected(x,y), 4) == 0) && ...
                    (foundUltraCondition == 0)
                
                if (x + offset_index <= 6) && (y + offset_index <= 6 )
                    
                    % gets diagonal to be analyzed
                    t = diag(state_matrix (x:(x + offset_index), ...
                        y:(y + offset_index)));
                    
                    if all(t == t(1))
                        
                        % Found a winning condition. We can stop already.
                        if offset_index == 4
                            foundUltraCondition = 1;
                        end

                        scores(3, index_player) = scores(3, index_player) + 10^(3*(offset_index+1));
                        
                        % fills hasBeenDetected with 0100 in the
                        % respective DIAGONAL
                        xAux = x:(x + offset_index);
                        yAux = y:(y + offset_index);
                        
                        % In MATLAB, a position in a matrix is given by
                        % (y-1)* nbCols + x
                        diagonalIdx = (yAux - 1)*6 + xAux;
                        
                        hasBeenDetected (diagonalIdx) = ...
                            hasBeenDetected (diagonalIdx) + 4;
                    
                    % We need to prevent the other player to win the
                    % match
                    elseif offset_index == 4
                        
                        if (sum (t == player) == 4) && (sum (t == opponent) == 1)
                            value = value + 10^14;
                        elseif (sum (t == player) == 1) && (sum (t == opponent) == 4)
                            value = value - 10^14;
                        end
                        
                    end
                end
                
                offset_index = offset_index - 1;
            end
            
            
            % Verifies all possible DIAGONALS to the LEFT
            offset_index = 4;
            while (offset_index >= 1) && (bitand(hasBeenDetected(x,y), 8) == 0) && ...
                    (foundUltraCondition == 0)
                
                if (x + offset_index <= 6) && (y - offset_index >= 1 )
                    
                    % gets diagonal to be analyzed
                    t = diag(flipud(state_matrix (x:(x + offset_index), ...
                        (y - offset_index):y)));
                    
                    if all(t == t(1))
                        
                        % Found a winning condition. We can stop already.
                        if offset_index == 4
                            foundUltraCondition = 1;
                        end
                        
                        scores(4, index_player) =  scores(4, index_player)+ 10^(3*(offset_index+1));
                                                
                        % creates an auxiliary matrix which will be
                        % used to set hasBeenDetected.
                        mAux = 8*flipud(eye(offset_index + 1));
                        
                        % fills hasBeenDetected with 1000 in the
                        % respective DIAGONAL
                        hasBeenDetected (x:(x + offset_index), ...
                            (y - offset_index):y) = ...
                            hasBeenDetected (x:(x + offset_index), ...
                            (y - offset_index):y) + mAux;
                        
                    % We need to prevent the other player to win the
                    % match
                    elseif offset_index == 4
                                                
                        if (sum (t == player) == 4) && (sum (t == opponent) == 1)
                            value = value + 10^14;
                        elseif (sum (t == player) == 1) && (sum (t == opponent) == 4)
                            value = value - 10^14;
                        end
                        
                    end
                end
                
                offset_index = offset_index - 1;
            end
            
        end  
        y = y + 1;
    end
    x = x + 1;
end

value =  value + sum(scores(:, 1) - scores(:, 2));

end
