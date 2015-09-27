function [X, Y] = gera_dados(dados, saida, m, r)

    load(dados);

    data = dengue_SP;

    data = (data - mean(data))/std(data);
    n_data = length (data);

    X = [];
    Y = [];
    Z = [];

    for j=(m+r):n_data,
        Z = [Z; data( (j-m-(r-1)):j, 1 )'];
    end

    X = Z(:, 1:m);
    Y = Z(:, (m+1):(m+r));

    save(saida, 'X', 'Y');

end