function robot_control(matrix, xStart, yStart, rDirection, speed)

labyrinth_matrix = matrix;
robot_direction = rDirection;
x_robot = xStart;
y_robot = yStart;

m_length = length (labyrinth_matrix);

while (labyrinth_matrix (round(m_length + 1 - y_robot), round(x_robot)) ~= 'F')
    
    labyrinth_matrix (round(m_length + 1 - y_robot), round(x_robot)) = 'r';
    
    % Search nearest pixel in d1 direction.
    
    x_d1 = x_robot + cos(robot_direction + pi/4 );
    y_d1 = y_robot + sin(robot_direction + pi/4 );
    while (round(x_d1) >= 1) && (round(x_d1) <= length(labyrinth_matrix (1,:)) ) && (round(m_length + 1 - y_d1) >= 1) ...
            && (round(m_length + 1 - y_d1) <= m_length) && (labyrinth_matrix (round(m_length + 1 - y_d1), round(x_d1)) ~= '#')
        
        x_d1 = x_d1 + cos(robot_direction + pi/4);
        y_d1 = y_d1 + sin(robot_direction + pi/4);
        
    end
    
    distance_d1 = sqrt((x_d1 - x_robot)^2 + (y_d1 - y_robot)^2);
    
    % Search nearest pixel in d2 direction.
    
    x_d2 = x_robot + cos(robot_direction);
    y_d2 = y_robot + sin(robot_direction);
    while (round(x_d2) >= 1) && (round(x_d2) <= length(labyrinth_matrix (1,:)) ) && (round(m_length + 1 - y_d2) >= 1) ...
            && (round(m_length + 1 - y_d2) <= m_length) && (labyrinth_matrix (round(m_length + 1 - y_d2), round(x_d2)) ~= '#')
        
        x_d2 = x_d2 + cos(robot_direction);
        y_d2 = y_d2 + sin(robot_direction);
        
    end
    
    distance_d2 = sqrt((x_d2 - x_robot)^2 + (y_d2 - y_robot)^2);
    
    % Search nearest pixel in d3 direction.
    
    x_d3 = x_robot + cos(robot_direction - pi/4);
    y_d3 = y_robot + sin(robot_direction - pi/4);
    while (round(x_d3) >= 1) && (round(x_d3) <= length(labyrinth_matrix (1,:)) ) && (round(m_length + 1 - y_d3) >= 1) ...
            && (round(m_length + 1 - y_d3) <= m_length) && (labyrinth_matrix (round(m_length + 1 - y_d3), round(x_d3)) ~= '#')
        
        x_d3 = x_d3 + cos(robot_direction - pi/4);
        y_d3 = y_d3 + sin(robot_direction - pi/4);
        
    end
    
    distance_d3 = sqrt((x_d3 - x_robot)^2 + (y_d3 - y_robot)^2);
    
    d1_rules = get_D1_D3_Rule(distance_d1);
    d2_rules = get_D2_Rule(distance_d2);
    d3_rules = get_D1_D3_Rule(distance_d3);
    
    active_rules = get_Angle_Rule(d1_rules, d2_rules, d3_rules);
    
    d_angle = get_Angle (active_rules);
    
    robot_direction = robot_direction + d_angle;
    
    x_robot = x_robot + speed*cos(robot_direction);
    y_robot = y_robot + speed*sin(robot_direction);
    
end

figure 
imagesc(labyrinth_matrix);
colormap(flipud(gray));
grid on;
grid minor;
end