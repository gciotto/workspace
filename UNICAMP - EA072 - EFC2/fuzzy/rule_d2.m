function S = rule_d2( sensor_capture )

S = [];

% Testar para P
relevance_MP = trap_pertinencia (sensor_capture, 0, 0, 1, 2);

if relevance_MP > 0
    S = [relevance_MP 'P'];
end

relevance_PP = trap_pertinencia (sensor_capture, 1, 2, 3, 4);

if relevance_PP > 0
    S = [S ; relevance_PP 'p'];
end

relevance_PG = trap_pertinencia (sensor_capture, 3, 4, 5, 6);

if relevance_PG > 0
    S = [S ; relevance_PG 'g'];
end

relevance_MG = trap_pertinencia (sensor_capture, 5, 6, 7, 7);

if relevance_MG > 0
    S = [S ; relevance_MG 'G'];
end

end


