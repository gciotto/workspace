%% FUNCTION process_information
%  It loads all necessary information in order to analyse the IA's performances
%  from a predetermined file 
clear all;
close all;

load('/home/gciotto/workspace/ProjetConception-PENTAGO/data.mat');

match_count = length(turns);

victories_count = [ length(find(victories(1:match_count/2) == 'W')) length(find(victories(match_count/2 + 1:match_count) == 'W')) ; ...
                    length(find(victories(1:match_count/2) == 'B')) length(find(victories(match_count/2 + 1:match_count) == 'B')) ; ...
                    length(find(victories(1:match_count/2) == 'D')) length(find(victories(match_count/2 + 1:match_count) == 'D')) ]
                         
victories_labels = {'White' 'Black' 'Draws'};
                
figure
victories_bar = bar(victories_count, 'stacked');
legend('Player Black starts playing.','Player White starts playing.');
grid on
ylabel('Number of matches');
set(victories_bar(1),'facecolor','black')
set(victories_bar(2),'facecolor','white')
title ('Final results of all disputed matches');

set(gca, 'XTickLabel',victories_labels, 'XTick',1:numel(victories_labels));

figure

subplot(2,1,1)

for n=1:match_count
    plot(1:18, timeLB(n,:), '.');
    grid on
    hold on    
end


title('Elapsed time of all moves of player Black');
xlabel('Turn');
ylabel('Elapsed Time (s)');

subplot(2,1,2)

plot (1:18, mean(timeLB), 'Marker', '*','Color', 'r', 'LineStyle', 'none');

mean(mean(timeLB))

grid on

title('Average elapsed time of each turn of player Black');
xlabel('Turn');
ylabel('Elapsed Time (s)');

figure

subplot(2,1,1)


for n=1:match_count
    if (max(timePENTAGO(n, :)) <= 100)
        plot(1:18, timePENTAGO(n,:), '.');
        hold on    
        grid on
    end
end

title('Elapsed time of all moves of player White');
xlabel('Turn');
ylabel('Elapsed Time (s)');

timePENTAGO (ind2sub(size(timePENTAGO ), find(timePENTAGO > 100))) = 0;

subplot(2,1,2)
plot (1:18, mean(timePENTAGO), 'Marker', '*','Color', 'r', 'LineStyle', 'none');
grid on

mean(mean(timePENTAGO))

title('Average elapsed time of each turn of player White');
xlabel('Turn');
ylabel('Elapsed Time (s)');
