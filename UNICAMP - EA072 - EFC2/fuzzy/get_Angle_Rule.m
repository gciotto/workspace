function rule = get_Angle_Rule( d1_rules, d2_rules, d3_rules )

rule = zeros(1, 5);

if d1_rules(1) > 0
    
    if d2_rules(1) > 0
        
        if d3_rules(1) > 0
            rule(1) = min (min (d1_rules(1), d2_rules(1) ), d3_rules(1)); %'MN'
        end
        
        if d3_rules(2) > 0
            rule(1) = min (min (d1_rules(1), d2_rules(1) ), d3_rules(2)); %'MN'
        end
        
        if d3_rules(3) > 0
            rule(1) = min (min (d1_rules(1), d2_rules(1) ), d3_rules(3)); %'MN'
        end
    end
        
    if d2_rules(2) > 0
        
        if d3_rules(1) > 0
            rule(3) = min ( min (d1_rules(1), d2_rules(2) ), d3_rules(1)); %'Z'
        end
            
        if d3_rules(2) > 0
            rule(1) = min ( min (d1_rules(1), d2_rules(2) ), d3_rules(2)); %'MN'
        end
        
        if d3_rules(3) > 0
            rule(1) = min ( min (d1_rules(1), d2_rules(2) ), d3_rules(3)); %'MN'
        end
    end
        
    if d2_rules(3) > 0
        
        if d3_rules(1) > 0
            rule(3) = min (min (d1_rules(1), d2_rules(3) ), d3_rules(1)); %'Z'
        end
            
        if d3_rules(2) > 0
            rule(1) = min ( min (d1_rules(1), d2_rules(3) ), d3_rules(2)); %'MN'
        end
            
        if d3_rules(3) > 0
            rule(1) = min ( min (d1_rules(1), d2_rules(3) ), d3_rules(3)); %'MN'
        end
        
    end
    
    if d2_rules(4) > 0
        
        if d3_rules(1) > 0
            rule(3) = min ( min (d1_rules(1), d2_rules(4) ), d3_rules(1)); %'Z'
        end
            
        if d3_rules(2) > 0
            %rule(2) = min ( min (d1_rules(1), d2_rules(4) ), d3_rules(2)); %'PN'
            rule(1) = min ( min (d1_rules(1), d2_rules(4) ), d3_rules(2)); %'MN'
        end
        
        if d3_rules(3) > 0
            rule(1) = min ( min (d1_rules(1), d2_rules(4) ), d3_rules(3)); %'MN'
        end
        
    end
end
    
if d1_rules(2) > 0
    
    if d2_rules(1) > 0
        
        if d3_rules(1) > 0
           % rule(4) = min ( min (d1_rules(2), d2_rules(1) ), d3_rules(1)); %'PP'
           rule(5) = min ( min (d1_rules(2), d2_rules(1) ), d3_rules(1)); %'PP'
        end
            
        if d3_rules(2) > 0
            rule(5) = min ( min (d1_rules(2), d2_rules(1) ), d3_rules(2)); %'MP'
        end
        
        if d3_rules(3) > 0
            rule(1) = min ( min (d1_rules(2), d2_rules(1) ), d3_rules(3)); %'MN'
        end
    end
        
    if d2_rules(2) > 0
        
        if d3_rules(1) > 0
            %rule(4) = min ( min (d1_rules(2), d2_rules(2) ), d3_rules(1)); %'PP'
            rule(5) = min ( min (d1_rules(2), d2_rules(2) ), d3_rules(1)); %'PP'
        end
            
        if d3_rules(2) > 0
            rule(1) = min ( min (d1_rules(2), d2_rules(2) ), d3_rules(2)); %'MN'
        end
            
        if d3_rules(3) > 0
            %rule(2) = min ( min (d1_rules(2), d2_rules(2) ), d3_rules(3)); %'PN'
            rule(1) = min ( min (d1_rules(2), d2_rules(2) ), d3_rules(3)); %'PN'
        end
    end
        
    if d2_rules(3) > 0
        
        if d3_rules(1) > 0
            %rule(4) = min ( min (d1_rules(2), d2_rules(3) ), d3_rules(1)); %'PP'
            rule(5) = min ( min (d1_rules(2), d2_rules(3) ), d3_rules(1)); %'PP'
        end
            
        if d3_rules(2) > 0
            rule(3) = min ( min (d1_rules(2), d2_rules(3) ), d3_rules(2)); %'Z'
        end
            
        if d3_rules(3) > 0
            %rule(2) = min ( min (d1_rules(2), d2_rules(3) ), d3_rules(3)); %'PN'
            rule(1) = min ( min (d1_rules(2), d2_rules(3) ), d3_rules(3)); %'PN'
        end
    end
    
    if d2_rules(4) > 0
        
        if d3_rules(1) > 0
            %rule(4) = min ( min (d1_rules(2), d2_rules(4) ), d3_rules(1)); %'PP'
            rule(5) = min ( min (d1_rules(2), d2_rules(4) ), d3_rules(1)); %'PP'
        end
            
        if d3_rules(2) > 0
            rule(3) = min ( min (d1_rules(2), d2_rules(4) ), d3_rules(2)); %'Z'
        end
            
        if d3_rules(3) > 0
            %rule(2) = min ( min (d1_rules(2), d2_rules(4) ), d3_rules(3)); %'PN'
            rule(1) = min ( min (d1_rules(2), d2_rules(4) ), d3_rules(3)); %'PN'
        end
        
    end
    
end

% SE D1 É G ...
if d1_rules(3) > 0
    
    if d2_rules(1) > 0
        
        if d3_rules(1) > 0
            rule(5) = min ( min (d1_rules(3), d2_rules(1) ), d3_rules(1)); %'MP'
        end
        
        if d3_rules(2) > 0
            rule(5) = min ( min (d1_rules(3), d2_rules(1) ), d3_rules(2)); %'MP'
        end
            
        if d3_rules(3) > 0
            rule(5) = min ( min (d1_rules(3), d2_rules(1) ), d3_rules(3)); %'MP'
        end
    end
        
    if d2_rules(2) > 0
        
        if d3_rules(1) > 0
            rule(5) = min ( min (d1_rules(3), d2_rules(2) ), d3_rules(1)); %'MP'
        end
            
        if d3_rules(2) > 0
            rule(5) = min ( min (d1_rules(3), d2_rules(2) ), d3_rules(2)); %'MP'
        end
        
        if d3_rules(3) > 0
            %rule(5) = min ( min (d1_rules(3), d2_rules(2) ), d3_rules(3)); %'MP'
            rule(3) = min ( min (d1_rules(3), d2_rules(2) ), d3_rules(3)); %'MP'
        end
    end
        
    if d2_rules(3) > 0
        
        if d3_rules(1) > 0
            rule(5) = min ( min (d1_rules(3), d2_rules(3) ), d3_rules(1)); %'MP'
        end
            
        if d3_rules(2) > 0
            %rule(4) = min ( min (d1_rules(3), d2_rules(3) ), d3_rules(2)); %'PP'
            rule(5) = min ( min (d1_rules(3), d2_rules(3) ), d3_rules(2)); %'PP'
        end
            
        if d3_rules(3) > 0
            rule(3) = min ( min (d1_rules(3), d2_rules(3) ), d3_rules(3)); %'Z'
        end
        
    end
    
    if d2_rules(4) > 0
        
        if d3_rules(1) > 0
            rule(5) = min ( min (d1_rules(3), d2_rules(4) ), d3_rules(1)); %'MP'
        end
            
        if d3_rules(2) > 0
            %rule(4) = min ( min (d1_rules(3), d2_rules(4) ), d3_rules(2)); %'PP'
            rule(5) = min ( min (d1_rules(3), d2_rules(4) ), d3_rules(2)); %'PP'
        end
        
        if d3_rules(3) > 0
            rule(3) = min ( min (d1_rules(3), d2_rules(4) ), d3_rules(3)); %'Z'
        end
        
    end
    
end

end

