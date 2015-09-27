function numberNeuronsMLP (N)

close all;

erro_tot_avg_iter = [];
mean_eqmv_min_avg_itr = [];

neurons_interval = 5:N;
iter_interval = [50:50:200  300:100:1000];


for n = iter_interval
    
    mean_eqmv_min_avg_array = [];
    error_tot_avg_array = [];
    
    for i=neurons_interval
        
        disp(['======== ' num2str(n)  ' ITERACOES ========= ' num2str(i) ' NEURONS ======================']);
        
        nn1h_k_folds('matrizes', 10, i, 1, n);
        
        [error_tot_avg mean_eqmv_min_avg eqm_ens] = analysis('matrizes', 1, 10);
        
        mean_eqmv_min_avg_array = [mean_eqmv_min_avg_array mean_eqmv_min_avg];
        error_tot_avg_array = [error_tot_avg_array error_tot_avg];
        disp('===============================================================');
        
    end
    
    erro_tot_avg_iter = [erro_tot_avg_iter error_tot_avg_array'];
    mean_eqmv_min_avg_itr = [mean_eqmv_min_avg_itr mean_eqmv_min_avg_array'];
    
    [A B] = sort(mean_eqmv_min_avg_array, 'ascend');
    disp(sprintf('Numero de neuronios para minimizar erro de validacao: %d', B(1) + min (neurons_interval) - 1));
    
    figure
    plot(neurons_interval, mean_eqmv_min_avg_array);
    xlabel('Numero de neuronios');
    hold on; 
    plot(neurons_interval, error_tot_avg_array, 'r');
    legend('Erro de validacao', 'Erro de teste');
    plot(neurons_interval, mean_eqmv_min_avg_array, '*');
    plot(neurons_interval, error_tot_avg_array, 'r*');
    title(sprintf('Erros para N = 5 .. %d e n. iteracoes = %d', i, n));
    
    grid on;
    hold off;
end

figure 
plot (iter_interval, mean(mean_eqmv_min_avg_itr));
hold on;
plot (iter_interval, mean(erro_tot_avg_iter), 'r');
legend('Media dos erros de validacao', 'Media dos erros de teste');
xlabel('Numero de iteracoes');
plot (iter_interval, mean(mean_eqmv_min_avg_itr),'*');
plot (iter_interval, mean(erro_tot_avg_iter), '*r');
title('Media dos erros para valores de N = 5 .. 20');
hold off;
grid on;

end

