function S = rule_d1_d3( sensor_capture )

S = [];

% Testar para P
relevance_P = trap_pertinencia (sensor_capture, 0, 0, 1, 2);

if relevance_P > 0
    S = [relevance_P 'P'];
end

relevance_M = trap_pertinencia (sensor_capture, 1, 2, 3, 4);

if relevance_M > 0
    S = [S ; relevance_M 'M'];
end

relevance_G = trap_pertinencia (sensor_capture, 3, 4, 5, 5);

if relevance_G > 0
    S = [S ; relevance_G 'G'];
end

end

