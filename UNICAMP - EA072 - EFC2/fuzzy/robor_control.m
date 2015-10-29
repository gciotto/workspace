robot_direction = pi;
x_robot = 18;
y_robot = 18;

labyrinth_matrix = zeros(20,20);
labyrinth_matrix (1, :) = '#'; 
labyrinth_matrix (20, :) = '#'; 
labyrinth_matrix (:, 1) = '#'; 
labyrinth_matrix (:, 20) = '#'; 

% Search nearest pixel in d1 direction.

x_d1 = round(x_robot + cos(robot_direction + pi/4));
y_d1 = round(y_robot + sin(robot_direction + pi/4));
while (x_d1 > 0) && (x_d1 <= length(labyrinth_matrix (1,:)) ) && (y_d1 > 0) ...
        && (y_d1 <= length(labyrinth_matrix)) && (labyrinth_matrix (x_d1, y_d1) ~= '#')
    
    x_d1 = round(x_robot + cos(robot_direction + pi/4));
    y_d1 = round(y_robot + sin(robot_direction + pi/4));

end

distance_d1 = sqrt(x_d1^2 + y_d1^2);

% Search nearest pixel in d2 direction.

x_d2 = round(x_robot + cos(robot_direction));
y_d2 = round(y_robot + sin(robot_direction));
while (x_d2 > 0) && (x_d2 <= length(labyrinth_matrix (1,:))) && (y_d2 > 0) ...
        && (y_d2 <= length(labyrinth_matrix )) && (labyrinth_matrix (x_d2, y_d2) ~= '#')
    
    x_d2 = round(x_robot + cos(robot_direction));
    y_d2 = round(y_robot + sin(robot_direction));

end

distance_d2 = sqrt(x_d2^2 + y_d2^2);

% Search nearest pixel in d3 direction.

x_d3 = round(x_robot + cos(robot_direction - pi/4));
y_d3 = round(y_robot + sin(robot_direction - pi/4));
while (x_d3 > 0) && (x_d3 <= length(labyrinth_matrix (1,:)) ) && (y_d3 > 0) ...
        && (y_d3 <= length(labyrinth_matrix )) && (labyrinth_matrix (x_d3, y_d3) ~= '#')
    
    x_d3 = round(x_robot + cos(robot_direction - pi/4));
    y_d3 = round(y_robot + sin(robot_direction - pi/4));

end

distance_d3 = sqrt(x_d3^2 + y_d3^2);
