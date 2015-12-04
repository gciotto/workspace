function PentagoAI_LB()

close all;
StockLines();
InitializeBoard();


%**************************************************************************
function StockLines()
% Create a 3D matrix of winning line locations on the board

global lines;

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
lines(2:6,1:5,32) = flipud(eye(5));
%**************************************************************************


%**************************************************************************
function InitializeBoard()
% Create the game board and graphics handles

global board
global handles

board = zeros(6,6);
handles(1) = figure;
set(handles(1),'Name','PENTAGO');
set(handles(1),'Toolbar','none');
screensize = get(0,'ScreenSize');
set(handles(1),'Position',[screensize(3)/2-300,screensize(4)/2-300,600,600]);
axis off;
axis square;
% Background:
handles(2) = patch([0 0 8 8],[0 -8 -8 0],[0.9 0.8 0.5]);
% Unoccupied spaces:
for i = 1:6
    for j = 1:6
        cnt = 2+(i-1)*6+j;
        r = 1/2; %radius of the circle
        a = i+r; %x-coordinate of the centre of the circle
        b = -j-r; %y-coordinate of the centre of the circle
        t = 0:2*pi/360:2*pi; %angle that the ray from (a,b) to (x,y) makes with the x-axis
        X = a+r*cos(t);
        Y = b+r*sin(t);
        handles(cnt) = patch(X,Y,[0.7 0.8 0.5]);
        set(handles(cnt),'ButtonDownFcn',{@CallbackPiece,j,i});
    end
end
EnablePieces('off');
% Vertical and horizontal lines:
handles(39) = line([4 4],[-1 -7],'LineWidth',2,'Color',[1 0 0]);
handles(40) = line([1 7],[-4 -4],'LineWidth',2,'Color',[1 0 0]);
% Arrows:
handles(41) = text(7.6,-1.5,'\rightarrow','HorizontalAlignment','center','FontSize',12,'FontWeight','bold','Rotation',-90);
handles(42) = text(6.5,-0.5,'\leftarrow','HorizontalAlignment','center','FontSize',12,'FontWeight','bold');
handles(43) = text(1.5,-0.5,'\rightarrow','HorizontalAlignment','center','FontSize',12,'FontWeight','bold');
handles(44) = text(0.6,-1.5,'\rightarrow','HorizontalAlignment','center','FontSize',12,'FontWeight','bold','Rotation',-90);
handles(45) = text(0.6,-6.5,'\leftarrow','HorizontalAlignment','center','FontSize',12,'FontWeight','bold','Rotation',-90);
handles(46) = text(1.5,-7.5,'\rightarrow','HorizontalAlignment','center','FontSize',12,'FontWeight','bold');
handles(47) = text(6.5,-7.5,'\leftarrow','HorizontalAlignment','center','FontSize',12,'FontWeight','bold');
handles(48) = text(7.6,-6.5,'\leftarrow','HorizontalAlignment','center','FontSize',12,'FontWeight','bold','Rotation',-90);
for i = 1:8
    set(handles(40+i),'ButtonDownFcn',{@CallbackArrow,i});
end
EnableArrows('off');
% Status text:
handles(49) = text(4,-8.5,'','HorizontalAlignment','center','FontSize',12,'FontWeight','bold');
CoinTossing();
Wait();
%**************************************************************************


%**************************************************************************
function Wait()
% Wait for events that occur after a move has been made

global board
global turn

win = TestForWin(board);
if (win==0) %no one has won
    if (turn==-1)
        DisplayStatus('Black''s turn');
%         EasyAI();
        HardAI();
        win = TestForWin(board);
        if (win==0)
            turn = 1;
            DisplayStatus('White''s turn');
            EnablePieces('on');
        end
    else
        DisplayStatus('White''s turn');
        EnablePieces('on');
    end
end
if (win==1) %somebody has won
    button = questdlg('Would you like to play again?','Game over','Yes','No','Yes');
    if (strcmp(button,'Yes'))
        InitializeBoard();
    else
        close all
    end
end
%**************************************************************************


%**************************************************************************
function win = TestForWin(board)
% Test the board matrix for a winning line

global handles
global lines

win = 0;
white_win = 0;
black_win = 0;
% Check all the lines on the board:
for i = 1:32
    line_sum = sum(sum(lines(:,:,i).*board));
    if (line_sum==5)
        white_win = white_win+1;
    elseif (line_sum==-5)
        black_win = black_win+1;
    end
end
if (white_win==black_win)&&(white_win>0)
    DisplayStatus('Tie game');
    win = 1;
elseif (white_win>black_win)
    DisplayStatus('White wins');
    win = 1;
elseif (black_win>white_win)
    DisplayStatus('Black wins');
    win = 1;
end
if (win==0)&&(all(all(board~=0)))
    DisplayStatus('Draw game');
    win = 1;
end
if (win==1)
    % Flash the text:
    for i = 1:3
        pause(0.5);
        set(handles(49),'Color',[1 0 0]);
        pause(0.5);
        set(handles(49),'Color',[0 0 0]);
    end
end
%**************************************************************************


%**************************************************************************
function CallbackPiece(~,~,row,col)
% Update board matrix based on the click

global board
global turn

if (board(row,col)==0) %unoccupied spaces
    board(row,col) = turn;
    DrawBoard();
    EnablePieces('off');
    EnableArrows('on');
    ColorArrows('red');
    DisplayStatus('Rotate one of the sub-boards');
else
    DisplayStatus('This space is not available');
end
%**************************************************************************


%**************************************************************************
function CallbackArrow(~,~,arrow)
% Update board matrix based on the click

global board
global turn

board = RotateBoard(board,arrow);
DrawBoard();
EnableArrows('off');
ColorArrows('black');
turn = -1;
Wait();
%**************************************************************************


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
%**************************************************************************


%**************************************************************************
function DrawBoard()
% Draw the current game board

global handles
global board

colors = {[0 0 0],[0.7 0.8 0.5],[1 1 1]};
for i = 1:6
    for j = 1:6
        cnt = 2+(i-1)*6+j;
        set(handles(cnt),'FaceColor',colors{board(j,i)+2});
    end
end
%**************************************************************************


%**************************************************************************
function ColorArrows(str)
% Changes the color of the arrows on the game board

global handles

switch str
    case 'red'
        arrow_color = [1 0 0];
    case 'black'
        arrow_color = [0 0 0];
end
for i = 1:8
    set(handles(40+i),'Color',arrow_color);
end
%**************************************************************************


%**************************************************************************
function EnableArrows(str)
% Enable or disable the arrows

global handles

for i = 1:8
    set(handles(40+i),'HitTest',str);
end
%**************************************************************************


%**************************************************************************
function EnablePieces(str)
% Enable or disable the pieces

global handles

for i = 1:36
    set(handles(2+i),'HitTest',str);
end
%**************************************************************************


%**************************************************************************
function DisplayStatus(str)
% Update the status text at the bottom of the game board

global handles

set(handles(49),'String',str);
%**************************************************************************


%**************************************************************************
function CoinTossing()
% Use a coin toss to determine the first player

global turn

turn = randi([0 1],1);
if (turn==1)
    DisplayStatus('White goes first');
else
    turn = -1;
    DisplayStatus('Black goes first');
end
pause(1.0);
%**************************************************************************


% %**************************************************************************
% function EasyAI()
% % Game's AI brain
% 
% global board
% 
% pause(1.0);
% ind = find(board==0);
% rnd = randi([1 length(ind)],1);
% board(ind(rnd)) = -1;
% DrawBoard();
% ColorArrows('red');
% pause(1.0);
% rnd = randi([1 8],1);
% board = RotateBoard(board,rnd);
% DrawBoard();
% ColorArrows('black');
% %**************************************************************************


%**************************************************************************
function HardAI()
% Game's AI brain

global board

pause(1.0);
history = [];
for arrow_1 = 1:8
    workboard_1 = board;
    workboard_1 = RotateBoard(workboard_1,arrow_1);
    [white_max_score_1,white_row_empty_spaces_1,white_col_empty_spaces_1,black_max_score_1,black_row_empty_spaces_1,black_col_empty_spaces_1] = EvaluateBoard(workboard_1);
    % Look at what happens when black moves offensively:
    if (black_max_score_1>0) %Skip this step when black has no pieces out
        for i = 1:length(black_row_empty_spaces_1)
            white_score_list = zeros(1,8);
            black_score_list = zeros(1,8);
            for arrow_2 = 1:8
                workboard_2 = workboard_1;
                workboard_2(black_row_empty_spaces_1(i),black_col_empty_spaces_1(i)) = -1; %place a black piece and see what happens
                workboard_2 = RotateBoard(workboard_2,arrow_2);
                [white_max_score_2,~,~,black_max_score_2,~,~] = EvaluateBoard(workboard_2);
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
            [white_max_score_2,~,~,black_max_score_2,~,~] = EvaluateBoard(workboard_2);
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

[i,j, black_best_arrow] = ind2sub(size(black_choice), find(black_choice == -1))

board = board+black_choice;
DrawBoard();
ColorArrows('red');
pause(2.0);
board = RotateBoard(board,black_best_arrow);
DrawBoard();
ColorArrows('black');
%**************************************************************************


%**************************************************************************
function [white_max_score,white_row_empty_spaces,white_col_empty_spaces,black_max_score,black_row_empty_spaces,black_col_empty_spaces] = EvaluateBoard(board)
% Find the best moves for both white and black

global lines

[white_score,black_score] = BoardScore(board);
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
%**************************************************************************


%**************************************************************************
function [white_score,black_score] = BoardScore(board)
% Calculate the score for each winning line

global lines;

white_score = zeros(32,1);
black_score = zeros(32,1);
for i = 1:32
    white_score(i,1) = sum(sum((lines(:,:,i).*board)==1));
    black_score(i,1) = sum(sum((lines(:,:,i).*board)==-1));
end
%**************************************************************************


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
%**************************************************************************