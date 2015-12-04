%% FUNCTION alpha_beta_solution
% 
% AUTHORS:  Gustavo CIOTTO PINTON
%           Marcelo MARQUES FREIRE DE CARVALHO
function [eval_value move quadrant direction] = alpha_beta_search (state_matrix, emptySlots, depth, alpha, beta, isMaximizing, player)

if depth == 0 || emptySlots == 0
    
    [eval_value test_variable] = evaluate_board(state_matrix, player);
    move = [];
    quadrant = 0;
    direction = 0;
    
else
    
    dir = ['L' 'R'];
    mustStop = 0;
    
    
    if (isMaximizing == 1)
        eval_ = -inf;
    else
        eval_ = +inf;
    end
    
    l = 1;
    while (l <= 6) && (mustStop == 0)
        
        c = 1;
        while (c <= 6) && (mustStop == 0)
            
            if state_matrix (l,c) == 0
                
                quad_index = 1;
                while (quad_index <=4) && (mustStop == 0)
                    
                    dir_index = 1;
                    while (dir_index <= 2) && (mustStop == 0)
                        
                        if isMaximizing == 1
                            state_matrix (l,c) = player;
                        else
                            
                            if player == 'B'
                                state_matrix (l,c) = 'W';
                            else
                                state_matrix (l,c) = 'B';
                            end
                            
                        end
                        
                        % Simulates a rotation.
                        state_matrix = rotate_quadrant (state_matrix, ...
                            quad_index, dir (dir_index) );
                        
                        % Verifies if someone has won.
                        state_game_ = verify_victory(state_matrix);
                        
                        if isMaximizing == 1
                            
                            if state_game_ == player
                                % Top score.
                                eval_MIN = +10^20;
                                mustStop = 1;
                            else
                                [eval_MIN move_MIN quadrant_MIN direction_MIN] = ...
                                    alpha_beta_search(state_matrix, emptySlots - 1, depth - 1, alpha, beta, 0, player);                        
                            end
                            
                            % Refreshes alpha-beta state.
                            if eval_ < eval_MIN
                                
                                eval_ = eval_MIN;
                                eval_value = eval_;
                                move = [l c];
                                quadrant = quad_index;
                                direction = dir (dir_index);
                                
                            end
                            
                            % Verifies pruning.
                            if eval_ >= beta
                                mustStop = 1;
                            end
                            
                            alpha = max(alpha, eval_);
                            
                            
                        elseif isMaximizing == 0
                            
                            if (state_game_ ~= player) && (state_game_ ~= 0) && (state_game_ ~= 'D')
                                eval_MAX = -10^20;
                                mustStop = 1;
                            else
                                [eval_MAX move_MAX quadrant_MAX direction_MAX] = ...
                                    alpha_beta_search(state_matrix, emptySlots - 1, depth - 1, alpha, beta, 1, player);
                            end
                            
                            % Refreshes alpha-beta state.
                            if eval_ > eval_MAX
                                
                                eval_ = eval_MAX;
                                eval_value = eval_;
                                move = [l c];
                                quadrant = quad_index;
                                direction = dir (dir_index);
                                
                            end
                            
                            % Verifies pruning.
                            if alpha >= eval_
                                mustStop = 1;
                            end
                            
                            beta  = min(beta, eval_);
                        end
                        
                        % Undo rotation and actual play.
                        if dir (dir_index) == 'L'
                            state_matrix = rotate_quadrant(state_matrix, quad_index, 'R');
                        else
                            state_matrix = rotate_quadrant(state_matrix, quad_index, 'L');
                        end
                        
                        state_matrix (l,c) = 0;
                        
                        dir_index = dir_index + 1;
                    end
                    
                    quad_index = quad_index + 1;
                end
            end
            
            c = c + 1;
        end
        
        l = l + 1;
    end
end

end
