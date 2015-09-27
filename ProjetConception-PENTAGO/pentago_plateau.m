%%  FUNCTION pentago_plateau:
%   This function builds a new board and fills it with the graphical
%   components that represent the main elements of a PENTAGO game.
%   It takes no parameters nor returns any values.
%   AUTHORS:    CIOTTO PINTON Gustavo
%               MARQUES FREIRE DE CARVALHO Marcelo
%   PROJET DE CONCEPTION - CENTRALE SUPELEC GIF
function pentago_plateau

%clear all;
%close all;

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

turn = 'N';
state_matrix = zeros (6,6);
empty_Slots = 36;


hasTurned = 0;
hasPlayed = 0;
isComputer = 0;

% Board's characteristics
width   = 700;
height  = 700;
XMargin = 100;
YMargin = 100;
space = 14;     % Total amount of space between two different circles
colorRectangle = [0.5 0.5 0.5];


% It creates a new figure which is going to host all the components
plateau = figure ('Visible', 'on', 'Position', [500, 300, width, height], ...
    'Name','Le Jeu PENTAGO ','NumberTitle','off', 'Resize', 'off', ...
    'Color', [0.9, 0.9, 1], 'MenuBar', 'none');


% axes1 represents the total space where we can place our
%       components into
axes1 = axes('Units','Pixels','Position',[50,75,(width - XMargin) , (height - YMargin) ], ...
    'XLim', [-30 (width - XMargin + 30) ], ...
    'YLim', [0 (height - YMargin) ], 'Color', 'none', 'Visible', 'off');

% Tells MATLAB to superpose the axes of images and rectangles we are
%       going to need.
set(axes1, 'NextPlot', 'add');


rectangles = zeros (6, 6);

rectWidth = (width - XMargin)/6;
rectHeight = (height - YMargin)/6;


x = space / 2;
for l = 1:6
    
    y = space / 2;
    
    for c = 1:6
        rectangles((6 -(c-1)),l) = rectangle('Curvature', [1.0, 1.0], ...
            'Position', [ x, y , rectWidth-space, rectHeight-space], ...
            'FaceColor', colorRectangle, 'EdgeColor', 'none',...
            'ButtonDownFcn', {@rectangle_pressed,  (6 -(c-1)), l});
        
        y = y + rectHeight;
    end
    
    x = x + rectWidth;
end

% Vertical lines used to separates the four different areas
lineVertical   = line ([(width - XMargin) /2 (width - XMargin)/2  ], ...
    [ (1/6)*(height - YMargin) (5/6)*(height - YMargin)], ...
    'Color', [1.0 0.2 0.2], 'LineStyle', '--');
lineHorizontal = line ([ (1/6)*(width - XMargin) (5/6)*(width - XMargin)  ], ...
    [(height - YMargin)/2 (height - YMargin)/2], ...
    'Color', [1.0 0.2 0.2], 'LineStyle', '--', ...
    'LineWidth', 1.8);

% Adds start and reset buttons
buttonStart = uicontrol ('Position', [50 30 100 20], 'String', 'Start Game', ...
    'Callback', @start_pressed);
buttonReset = uicontrol ('Position', [180 30 100 20], 'String', 'Reset Game', ...
    'Enable', 'off', 'Callback', @reset_pressed);

textState   = uicontrol ('Style', 'text', 'String', 'Click [Start Game] to start a new game',...
    'Position', [300 10 300 40], 'BackgroundColor', get (plateau, 'Color'));

% The following lines are responsible for reading the images from the respective files.
% These images correspond to the arrows that make the boards to turn right or left.
rotate_up = imread('arrow_alt_up.jpeg');
rotate_down = imread('arrow_alt_down.jpeg');

% Adds all the eight arrows
textQ1L   = image(-30,450,rotate_down, 'ButtonDownFcn',{@turn_pressed, 1, 'R'});
textQ1R   = image(-30,400,rotate_up, 'ButtonDownFcn',{@turn_pressed, 1, 'L'});

textQ2L   = image(600,450,rotate_down, 'ButtonDownFcn',{@turn_pressed, 2, 'L'});
textQ2R   = image(600,400,rotate_up, 'ButtonDownFcn',{@turn_pressed, 2, 'R'});

textQ3L   = image(-30,150,rotate_down, 'ButtonDownFcn',{@turn_pressed, 3, 'R'});
textQ3R   = image(-30,100,rotate_up, 'ButtonDownFcn',{@turn_pressed, 3, 'L'});

textQ4L   = image(600,150,rotate_down, 'ButtonDownFcn',{@turn_pressed, 4, 'L'});
textQ4R   = image(600,100,rotate_up, 'ButtonDownFcn',{@turn_pressed, 4, 'R'});

%%  FUNCTION start_pressed
%   This function is called when the button 'START GAME' is
%   pressed. Basically it will set up the main variables required
%   to the game's progress.
%   PARAMETERS: src is the element source which has launched the
%                   interruption.
%               eventdata is the data describing the event.
    function start_pressed(src,eventdata)
        
        set(buttonReset,'Enable', 'on');
        set(buttonStart,'Enable', 'off');
        
        turn_random = randi(2);
        
        if turn_random == 1
            
            turn = 'W';
            isComputer = 0;
            
            set(textState, 'String', [ 'You are lucky! Player White has the turn']);
            
        else
            
            turn = 'B';
            isComputer = 1;
            
            set(textState, 'String', [ 'You are totally unlucky! Player Black has the turn']);
            
            pause(1);
            
            [eval move quadrant direction] = alpha_beta_search (state_matrix, empty_Slots, 2, -inf, +inf, 1, 'B');
            
            rectangle_pressed(rectangles(move(1), move(2)), 'F', move(1), move(2));
            
            set(textState, 'String', ['Computer chose the circle at ( ' int2str(move(1)) ',' int2str(move(2)) ' ).']);
            
            hasPlayed = 1;
            
            set(textState, 'String', ['Computer chose to turn quadrant no. ' int2str(quadrant) ' to the ' direction]);
            
            pause (2);
            
            turn_pressed(0, 'F', quadrant, direction);
            
            isComputer = 0;
            
        end
        
        
    end


%%  FUNCTION reset_pressed
%   This function is called when the button 'RESET GAME' is
%   pressed. Basically it will reset all the game, as it sets up
%   the main variables to their initial states.
%   PARAMETERS: src is the element source which has launched the
%                   interruption.
%               eventdata is the data describing the event.
    function reset_pressed(src,eventdata)
        
        set(buttonStart,'Enable', 'on');
        set(buttonReset,'Enable', 'off');
        
        reset_game();
    end


%%  FUNCTION turn_pressed
%   This function is called when one of the images (which represent
%   the arrows) is clicked. After that event, it will verify if
%   the player can actually do that move. This is equivalent to say
%   that he must choose one of the available spaces before turning
%   the board. If this condition is achieved, the function
%   calculate the new matrix of states and refreshes the board.
%   PARAMETERS: src is the element source which has launched the
%                   interruption.
%               eventdata is the data describing the event.
%               quad is the quadrant where the event was detected
%               direction can assume two possible values :
%                   -'R' : Right (clockwise direction)
%                   -'L' : Left  (counterclockwise direction)
    function turn_pressed(src,eventdata, quad , direction)
        
        quad
        direction
        
        % the game has not been started yet.
        if turn == 'N'
            set(textState,'String', 'You need to start the game before!');
            
        elseif isComputer == 1 && isempty(eventdata) == 1
            set(textState,'String', 'Wait! Computer is thinking...');
            
            % we verify if one of the spaces has already been selected.
        elseif hasPlayed == 0
            set(textState,'String', ['Hey! You need to play before '...
                'doing that.']);
        else
            state_matrix = rotate_quadrant(state_matrix, quad, direction );
            hasTurned = 1; % refresh the variable representating if the
            % player has already played or not.
            refresh_board(); % refreshes the board.
            
            % verifies if the player's turn can be ended.
            verify_turn_changing();
        end
        
    end

%% FUNCTION change_turn 
%  This function changes the actual turn. It takes no parameters
%  and returns no values.
    function change_turn
        
        if turn == 'B'
            set(textState, 'String', [ 'Player White has the turn']);
            turn = 'W';
        elseif turn == 'W'
            set(textState, 'String', [ 'Player Black has the turn']);
            turn = 'B';
        end
        
    end

%% FUNCTION reset_game
%  This function resets the game's variables to their initial
%  states. That means in other words resetting the matrix of states
%  and repainting all the components which represent the available
%  spaces. It takes no parameters and returns no values.
    function reset_game
        
        % resets matrix of states.
        state_matrix = zeros(6,6);
        
        turn = 'N';
        
        hasTurned = 0;
        hasPlayed = 0;
        isComputer = 0;
        empty_Slots = 36;
        
        % repaints all circles.
        for l = 1:6
            for c = 1:6
                set(rectangles(l,c), 'FaceColor', colorRectangle);
            end
        end
        
        set(textState, 'String', 'Click [Start Game] to start a new game');
    end

%% FUNCTION verify_turn_changing
%  This function verifies if the player turn can be changed. It can
%  only be changed if the player has chosen one of the circles and
%  has turned the board exactly once.
    function verify_turn_changing
        
        % player has turned played and turned the board
        if hasTurned == 1 && hasPlayed == 1
            
            result = verify_victory(state_matrix);
            
            if result == 'B' || result == 'W'
                
                set(textState, 'String', ['Oooo! We have a champion here! Player ' ...
                    result ' has won!!']);
                
                turn = 'N';
                
            elseif empty_Slots <= 0
                
                set(textState, 'String', ['No more slots left.  ' ...
                    'It seems we have a draw here...']);
                
                turn = 'N';
                
            else
                
                change_turn;
                
                isComputer = 1;
                
                hasTurned = 0;
                hasPlayed = 0;
                
                if result == 'D'
                    set(textState, 'String', [ 'Dont celebrate yet! We have a draw! The game continues...'  ...
                        get(textState, 'String')]);
                    
                end
                
                if turn == 'B'
                    
                    pause(1);
                    
                    %[move quadrant direction] = random_move(state_matrix);
                    %[move quadrant direction score] = best_of_all_moves(state_matrix, 'B',0);
                    [eval move quadrant direction] = alpha_beta_search (state_matrix, empty_Slots, 2, -inf, +inf, 1, 'B');
                    
                    rectangle_pressed(rectangles(move(1), move(2)), 'F', move(1), move(2));
                    
                    set(textState, 'String', ['Computer chose the circle at ( ' int2str(move(1)) ',' int2str(move(2)) ' ).']);
                    
                    
                    hasPlayed = 1;
                    
                    set(textState, 'String', ['Computer chose to turn quadrant no. ' int2str(quadrant) ' to the ' direction]);
                    
                    pause (2);
                    
                    turn_pressed(0, 'F', quadrant, direction);
                    
                    isComputer = 0;
                end
            end
        end
        
        
    end

%% FUNCTION refresh_board
%  This function repaints the board in order to reflect the matrix of
%  states. It takes no parameters nor returns any values.
    function refresh_board
        
        % Verifies each position of the matrix and paints accordingly to
        % the respective state.
        for l = 1:6
            for c = 1:6
                
                if state_matrix(l,c) == 0
                    set(rectangles(l,c), 'FaceColor', colorRectangle);
                elseif state_matrix(l,c) == 'W'
                    set(rectangles(l,c), 'FaceColor', [1.0 1.0 1.0]);
                elseif state_matrix(l,c) == 'B'
                    set(rectangles(l,c), 'FaceColor', [0.0 0.0 0.0]);
                end
            end
        end
        
    end

%%  Rectangle_pressed : this function is called when a click event is detected.
%  Parameters: src is the component which has launched the event.
%              eventdata contains some data describing the event.
%              l and c are the coordinates (ligne and column) of the clicked
%                  circle.
    function rectangle_pressed(src,eventdata, l, c)
        
        % Verifies if the game has already started.
        if turn == 'N'
            set(textState,'String', 'You need to start the game before!');
            
        % Verifies if the player has tried to push a button without having
        % the turn to do so.
        elseif isComputer == 1 && isempty(eventdata) == 1
            set(textState,'String', 'Wait! Computer is thinking...');
            
            % Verifies if the player has already played in his turn.
        elseif hasPlayed == 1
            set(textState,'String', [ 'You have already played, please ' ...
                'choose one of the arrows to turn.']);
            
            % Verifies if the clicked circle is available.
        elseif state_matrix (l,c) == 0
            
            % if player BLACK has the turn
            if turn == 'B'
                
                state_matrix (l,c) = 'B';
                set(src,'FaceColor', [0 0 0]);
                
                % if player WHITE has the turn
            elseif turn == 'W'
                
                state_matrix (l,c) = 'W';
                set(src,'FaceColor', [1 1 1]);
                
            end
            
            hasPlayed = 1;
            empty_Slots = empty_Slots - 1;
            
        else
            set(textState,'String', [ 'Oops! You cannot take that one, please ' ...
                'choose an available space.']);
            
        end
        
        
    end

end