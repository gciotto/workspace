
%**************************************************************************
function [move quadrant direction] = HardAI(board, lines)
    % Game's AI brain

    pause(1.0);
    history = [];
    for arrow_1 = 1:8
        workboard_1 = board;
        workboard_1 = RotateBoard(workboard_1,arrow_1);
        [white_max_score_1,white_row_empty_spaces_1,white_col_empty_spaces_1,black_max_score_1,black_row_empty_spaces_1,black_col_empty_spaces_1] = EvaluateBoard(workboard_1,lines);
        % Look at what happens when black moves offensively:
        if (black_max_score_1>0) %Skip this step when black has no pieces out
            for i = 1:length(black_row_empty_spaces_1)
                white_score_list = zeros(1,8);
                black_score_list = zeros(1,8);
                for arrow_2 = 1:8
                    workboard_2 = workboard_1;
                    workboard_2(black_row_empty_spaces_1(i),black_col_empty_spaces_1(i)) = -1; %place a black piece and see what happens
                    workboard_2 = RotateBoard(workboard_2,arrow_2);
                    [white_max_score_2,~,~,black_max_score_2,~,~] = EvaluateBoard(workboard_2, lines);
                    white_score_list(arrow_2) = white_max_score_2;
                    black_score_list(arrow_2) = black_max_score_2;
                end
                history(end+1,:) = [black_row_empty_spaces_1(i),black_col_empty_spaces_1(i),arrow_1,white_max_score_1,black_max_score_1,max(white_score_list),max(black_score_list)];
            end
        end
        % Look at what happens when black moves defensively:
        for i = 1:length(white_row_empty_spaces_1)
            white_score_list = zeros(1,8);
            black_score_list = zeros(1,8);
            for arrow_2 = 1:8
                workboard_2 = workboard_1;
                workboard_2(white_row_empty_spaces_1(i),white_col_empty_spaces_1(i)) = -1; % place a black piece and see what happens
                workboard_2 = RotateBoard(workboard_2,arrow_2);
                [white_max_score_2,~,~,black_max_score_2,~,~] = EvaluateBoard(workboard_2, lines);
                white_score_list(arrow_2) = white_max_score_2;
                black_score_list(arrow_2) = black_max_score_2;
            end
            history(end+1,:) = [white_row_empty_spaces_1(i),white_col_empty_spaces_1(i),arrow_1,white_max_score_1,black_max_score_1,max(white_score_list),max(black_score_list)];
        end
    end
    white_min_score_1st_move = min(history(:,4));
    black_max_score_1st_move = max(history(:,5));
    white_min_score_2nd_move = min(history(:,6));
    black_max_score_2nd_move = max(history(:,7));
    % Find the moves that maximize black's score (aggressive choice):
    black_aggressive_1st_moves = find(history(:,5)==black_max_score_1st_move);
    black_aggressive_2nd_moves = find(history(:,7)==black_max_score_2nd_move);
    black_aggressive_1st_and_2nd_moves = incommon(black_aggressive_1st_moves,black_aggressive_2nd_moves);
    % Find the moves that minimize white's score (conservative choice):
    black_conservative_1st_moves = find(history(:,4)==white_min_score_1st_move);
    black_conservative_2nd_moves = find(history(:,6)==white_min_score_2nd_move);
    black_conservative_1st_and_2nd_moves = incommon(black_conservative_1st_moves,black_conservative_2nd_moves);
    % The best move is the one that simultaneously maximizes black and minimizes white (aggressive):
    black_best_aggressive_moves = incommon(black_aggressive_1st_moves,black_conservative_2nd_moves);
    % Choose the immediate winning move:
    if (black_max_score_1st_move==5)
        black_best_moves = black_aggressive_1st_moves;
    elseif (black_max_score_1st_move==4)&&(black_max_score_2nd_move==5)
        black_best_moves = black_aggressive_1st_and_2nd_moves;
    % Otherwise choose the best aggressive move:
    elseif (~isempty(black_best_aggressive_moves))
        black_best_moves = black_best_aggressive_moves;
    % Otherwise choose the move that minimizes white's score for the longest period of time:
    elseif (~isempty(black_conservative_1st_and_2nd_moves))
        black_best_moves = black_conservative_1st_and_2nd_moves;
    % Otherwise choose the next best conservative move:
    else
        black_best_moves = black_conservative_1st_moves;
    end
    % If more than one best move exists, pick one at random:
    index = ceil(rand*length(black_best_moves));
    black_best_move = black_best_moves(index);
    black_best_row = history(black_best_move,1);
    black_best_col = history(black_best_move,2);
    black_best_arrow = history(black_best_move,3);
    black_choice = zeros(6,6);
    black_choice(black_best_row,black_best_col) = -1;
    if (mod(black_best_arrow,2)==0) %arrows 2, 4, 6 or 8
        black_best_arrow_back = black_best_arrow-1;
    else %arrows 1, 3, 5 or 7
        black_best_arrow_back = black_best_arrow+1;
    end


    black_choice = RotateBoard(black_choice,black_best_arrow_back);

    switch black_best_arrow

        case 1
            quadrant = 2;
            direction = 'R';
        case 2
            quadrant = 2;
            direction = 'L';
        case 3
            quadrant = 1;
            direction = 'R';
        case 4
            quadrant = 1;
            direction = 'L';
        case 5
            quadrant = 3;
            direction = 'R';
        case 6
            quadrant = 3;
            direction = 'L';
        case 7
            quadrant = 4;
            direction = 'R';
        case 8
            quadrant = 4;
            direction = 'L';

    end

    [i, j] = ind2sub(size(black_choice), find(black_choice == -1));
    move = [i, j];


%**************************************************************************

end


%**************************************************************************
function [white_max_score,white_row_empty_spaces,white_col_empty_spaces,black_max_score,black_row_empty_spaces,black_col_empty_spaces] = EvaluateBoard(board, lines)
    % Find the best moves for both white and black

    [white_score,black_score] = BoardScore(board, lines);
    % Calculate the best moves for white:
    white_free_lines = (black_score==0).*(white_score); %only look at possible "five in a row" locations
    white_max_score = max(white_free_lines); %maximum score among the possible locations
    white_best_lines = find(white_free_lines==white_max_score); %only keep the lines with the maximum score
    white_row_empty_spaces = zeros(1,160);
    white_col_empty_spaces = zeros(1,160);
    cnt = 0;
    for i = 1:length(white_best_lines)
        white_empty_spaces = xor(lines(:,:,white_best_lines(i)),(lines(:,:,white_best_lines(i)).*board)~=0); %find empty spaces
        white_num_empty_spaces = sum(sum(white_empty_spaces)); %number of empty spaces
        if (white_num_empty_spaces==0) %immediate win
            white_empty_spaces = (board==0);
        end
        white_loc_empty_spaces = find(white_empty_spaces==1); %locations of empty spaces
        for j = 1:length(white_loc_empty_spaces)
            cnt = cnt+1;
            white_row_empty_spaces(cnt) = mod(white_loc_empty_spaces(j),6);
            if (white_row_empty_spaces(cnt)==0)
                white_row_empty_spaces(cnt) = 6;
            end
            white_col_empty_spaces(cnt) = ceil(white_loc_empty_spaces(j)/6);
        end
    end
    white_row_empty_spaces = white_row_empty_spaces(white_row_empty_spaces~=0);
    white_col_empty_spaces = white_col_empty_spaces(white_col_empty_spaces~=0);
    % Calculate the best moves for black:
    black_free_lines = (white_score==0).*(black_score); %only look at possible "five in a row" locations
    black_max_score = max(black_free_lines); %maximum score among the possible locations
    black_best_lines = find(black_free_lines==black_max_score); %only keep the lines with the maximum score
    black_row_empty_spaces = zeros(1,160);
    black_col_empty_spaces = zeros(1,160);
    cnt = 0;
    for i = 1:length(black_best_lines)
         black_empty_spaces = xor(lines(:,:,black_best_lines(i)),(lines(:,:,black_best_lines(i)).*board)~=0); %find empty spaces
         black_num_empty_spaces = sum(sum(black_empty_spaces)); %number of empty spaces
         if (black_num_empty_spaces==0) %immediate win
             black_empty_spaces = (board==0); %pick any empty space on the board
         end
         black_loc_empty_spaces = find(black_empty_spaces==1); %locations of empty spaces
         for j = 1:length(black_loc_empty_spaces)
             cnt = cnt+1;
             black_row_empty_spaces(cnt) = mod(black_loc_empty_spaces(j),6);
             if (black_row_empty_spaces(cnt)==0)
                 black_row_empty_spaces(cnt) = 6;
             end
             black_col_empty_spaces(cnt) = ceil(black_loc_empty_spaces(j)/6);
         end
    end
    black_row_empty_spaces = black_row_empty_spaces(black_row_empty_spaces~=0);
    black_col_empty_spaces = black_col_empty_spaces(black_col_empty_spaces~=0);
end


%**************************************************************************

function [white_score,black_score] = BoardScore(board, lines)
% Calculate the score for each winning line

    white_score = zeros(32,1);
    black_score = zeros(32,1);
    for i = 1:32
        white_score(i,1) = sum(sum((lines(:,:,i).*board)==1));
        black_score(i,1) = sum(sum((lines(:,:,i).*board)==-1));
    end
end


%**************************************************************************
function new_board = RotateBoard(board,arrow)
% Rotate one of the sub-boards by 90 degrees either clockwise or anti-clockwise (based on which arrow was selected)

new_board = board;
switch arrow
    case 1
        new_board(1:3,4:6) = rot90(board(1:3,4:6),-1);
    case 2
        new_board(1:3,4:6) = rot90(board(1:3,4:6));
    case 3
        new_board(1:3,1:3) = rot90(board(1:3,1:3),-1);
    case 4
        new_board(1:3,1:3) = rot90(board(1:3,1:3));
    case 5
        new_board(4:6,1:3) = rot90(board(4:6,1:3),-1);
    case 6
        new_board(4:6,1:3) = rot90(board(4:6,1:3));
    case 7
        new_board(4:6,4:6) = rot90(board(4:6,4:6),-1);
    case 8
        new_board(4:6,4:6) = rot90(board(4:6,4:6));
end
end
%**************************************************************************

function vect_3 = incommon(vect_1,vect_2)
% Find the values in common between two vectors

L = length(vect_1);
vect_3 = zeros(L,1);
cnt = 0;
for i = 1:L
    if any(vect_2==vect_1(i))
        cnt = cnt+1;
        vect_3(cnt) = vect_1(i);
    end
end
vect_3 = vect_3(vect_3~=0);
end