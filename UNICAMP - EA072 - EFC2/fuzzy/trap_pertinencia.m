function pert = trap_pertinencia( x, a, b, c, d )

if (x < a) || (x > d)
    pert = 0;
    
elseif (x < b)
    
    if (a ~= b)
        pert = (x - a)/(b -a);
    else
        pert = 1;
    end
    
elseif (x < c)
    pert = 1;
   
else 
    
    if (c ~= d)
        pert = (d - x)/(d - c);
    else
        pert = 1;
    end
    
end

end

