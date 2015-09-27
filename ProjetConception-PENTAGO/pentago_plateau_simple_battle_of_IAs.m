%% FUNCTION pentago_plateux_simple_battle_of_IAs
%  This function is responsible for confronting the two intelligences.
%  AUTHORS: Gustavo CIOTTO PINTON
%           Marcelo MARQUES FREIRE DE CARVALHO
function pentago_plateau_simple_battle_of_IAs

%% LB CODE % ======================================

lines = zeros(6,6,32);
cnt = 0;
% Horizontal lines:
for i = 1:6
    cnt = cnt+1;
    lines(i,1:5,cnt) = ones(1,5);
end
for i = 1:6
    cnt = cnt+1;
    lines(i,2:6,cnt) = ones(1,5);
end
% Vertical lines:
for i = 1:6
    cnt = cnt+1;
    lines(1:5,i,cnt) = ones(5,1);
end
for i = 1:6
    cnt = cnt+1;
    lines(2:6,i,cnt) = ones(5,1);
end
% Diagonal lines:
lines(1:5,1:5,25) = eye(5);
lines(2:6,2:6,26) = eye(5);
lines(1:5,2:6,27) = eye(5);
lines(2:6,1:5,28) = eye(5);
lines(1:5,1:5,29) = flipud(eye(5));
lines(2:6,2:6,30) = flipud(eye(5));
lines(1:5,2:6,31) = flipud(eye(5));

%% CIOTTO CODE

% Variables containing simulation results.
victories = [];
turns = [];

% Number of matches to be realised.
number_of_matchs_each_player = 500;

timeLB = [];
timeLB_aux_ = [];

timePENTAGO = [];
timePENTAGO_aux_ = [];



for i = 1:number_of_matchs_each_player
    
    mustStop = 0;
    
    % Variables simulating a board.
    state_matrix = zeros (6,6);
    empty_Slots = 36;
    
    % Reinitializes variables.
    timeLB_aux_ = [];
    timePENTAGO_aux_ = [];
    
    if (i <= number_of_matchs_each_player/2)
        turn = 'B';
    else
        turn = 'W';
    end
    
    turns = [turns turn];
    
    display (['Game ' int2str(i) ' of ' int2str(number_of_matchs_each_player) ':']);
    display (['Turn of ' turn]);
    
    while (mustStop == 0)
        
        % LB's move
        if (turn == 'B')
            
            % For us, a black piece is represented by the char 'B'. For LB's
            % code, the notation used is -1 for the same situation. We need
            % to change it in order to use it.
            state_matrix_aux = state_matrix;
            state_matrix_aux ( ind2sub(size(state_matrix_aux), find(state_matrix_aux == 'B'))) = -1;
            state_matrix_aux ( ind2sub(size(state_matrix_aux), find(state_matrix_aux == 'W'))) = 1;
            
            tStart = tic;
            [move quadrant direction] = HardAI(state_matrix_aux, lines);
            tElapsed=toc(tStart);
            
            timeLB_aux_ = [ timeLB_aux_ tElapsed ];
            
        % Our move
        elseif (turn == 'W')
            
            tStart = tic;
            [eval move quadrant direction] = alpha_beta_search (state_matrix, empty_Slots, 2, -inf, +inf, 1, 'W');
            tElapsed=toc(tStart);
            
            timePENTAGO_aux_ = [ timePENTAGO_aux_ tElapsed ];
            
        end
        
        % Refreshes matrix.
        empty_Slots = empty_Slots - 1;
        state_matrix (move(1),move(2)) = turn;
        
        state_matrix = rotate_quadrant(state_matrix, quadrant, direction );
        
        % Changes turn.
        if turn == 'B'
            turn = 'W';
        elseif turn == 'W'
            turn = 'B';
        end
        
        % Verifies if the current match has ended
        result = verify_victory(state_matrix);
        
        if result == 'B' || result == 'W' || empty_Slots <= 0
                
                mustStop = 1;
            
                if empty_Slots <= 0
                    display (['Result: Drawn']);
                    victories = [victories 'D'];
                else
                    display (['Result: victory of ' result]);
                    victories = [victories result];
                end
                
                M = zeros(1, 18);
                M(1,1:length(timeLB_aux_)) = timeLB_aux_;
                timeLB = [timeLB ; M];
                
                M = zeros(1, 18);
                M(1,1:length(timePENTAGO_aux_)) = timePENTAGO_aux_;
                timePENTAGO = [timePENTAGO ; M];
            
        end
    
    end

    
    end

    save('/home/gciotto/workspace/ProjetConception-PENTAGO/data.mat', ...
                    'turns', 'victories', 'timeLB', 'timePENTAGO');
end