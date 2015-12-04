%% FUNCTION verify_victory
%  This function verifies if there is a winner or a draw.
%  It returns 'B' for BLACK, 'W' for WHITE, 0 for NOTHING and 'D' for DRAW.
%  AUTHORS: Gustavo CIOTTO PINTON
%           Marcelo MARQUES FREIRE DE CARVALHO
function state_of_game = verify_victory (state_matrix)

    state_of_game = 0;

    x = 1;
    while x <= 6 && state_of_game ~= 'D'

        y = 1;

        while y <= 6 && state_of_game ~= 'D'

            state_ = 0;

            if state_matrix (x, y) ~= 0

                if x <= 2

                    % determine if there is a combination in the column
                    t = state_matrix (x:(x+4) , y);
                    if all(t == t(1))
                        state_ = state_matrix(x,y);
                    end


                    % determine if there is a combination in one of the
                    % diagonals
                    if y <= 2
                        t = diag (state_matrix (x:(x+4) , y:(y+4)), 0);
                        if all(t == t(1))
                            state_ = state_matrix(x,y);
                        end

                    elseif y >= 5
                        t = diag (flipud(state_matrix (x:(x+4) , (y-4):y)), 0);
                        if all(t == t(1))
                            state_ = state_matrix(x,y);
                        end

                    end

                end

                % determine if there is a combination in the line
                if y <= 2
                    t = state_matrix (x , y:(y+4));
                    if all(t == t(1))
                        state_ = state_matrix(x,y);
                    end
                end

                % Verifies if there is a winner or a draw.
                if state_ ~= 0
                    if state_of_game == 0
                        state_of_game = state_;

                    elseif state_of_game ~= state_
                        state_of_game = 'D'; % empate

                    end
                end

            end

            y = y + 1;
        end
        x = x + 1;
    end
end
