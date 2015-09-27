function [b_nreg, erro_nreg, b_reg, erro_reg, erro_reg_tr] = resolve_sistema_k_folds(dados,k)

close all;

k = 10;
M = 5;
R = 1;

[X, Y] = gera_dados('dengue_SP.mat', 'resultado.mat', M, R);


Tr = length(X(:,1));
V = round(Tr/k);
Te = 0;

A_nreg = [ X ones(Tr ,1) ];
ATA = (A_nreg'*A_nreg)\eye(M + 1);

b_nreg = ATA*A_nreg'*Y;
erro_nreg = sqrt((norm(A_nreg*b_nreg - Y))^2/Tr);

erro_reg_k_folds = [];
erro_reg_tr_k_folds = [];
b_k_folds = [];

for i = 1:k
    
    k_fold_set = (i - 1)*V + 1 : i*V;
    
    conjunto_treinamento = setdiff(1:Tr, k_fold_set);
    
    Atr = [ X(conjunto_treinamento, :) ones(Tr - V - Te ,1) ];
    ATA = (Atr'*Atr)\eye(M + 1);
    Ytr = Y (conjunto_treinamento,:);
    
    b_nreg = ATA*Atr'*Ytr;
    erro_nreg = sqrt((norm(Atr*b_nreg - Ytr))^2/(Tr - V - Te));
        
    Aval = [ X(k_fold_set, :) ...
        ones(V,1) ];
    Yval = Y(k_fold_set, :);
    
    erro_reg = [];
    erro_reg_tr = [];
    b_reg = [];
    n_b_reg = [];
    
    for c_teste = -24:25
        
        ATA_ = (Atr'*Atr + (2^c_teste)*eye(M + 1)) \ eye(M + 1);
        
        b = ATA_*Atr'*Ytr;
        
        b_reg = [b_reg b];
        n_b_reg = [n_b_reg norm(b)];
        
        erro_reg = [erro_reg  sqrt((norm(Aval*b - Yval)^2)/V)];
        erro_reg_tr = [erro_reg_tr sqrt((norm(Atr*b - Ytr)^2)/V)];
    end
   
    b_k_folds = cat(3, b_k_folds, b_reg);
    erro_reg_k_folds = [erro_reg_k_folds; erro_reg];
    erro_reg_tr_k_folds = [erro_reg_tr_k_folds; erro_reg_tr];
end


figure
semilogx( 2.^(-24:25), mean(erro_reg_k_folds));
hold on;
semilogx( 2.^(-24:25), mean(erro_reg_k_folds),'*');
hold off;
title('Evolucao do erro quadratico medio');
ylabel('Media do erro quadratico medio das 10 execucoes');
xlabel('Parametro c');
grid on

[menor_erro, menor_index] = sort(mean(erro_reg_k_folds), 'ascend');

disp(['Vetores b para c = ' int2str(menor_index(1) - 25) ' e erro medio de ' num2str(menor_erro(1))]);
b_k_folds(:,menor_index(1),:)

end