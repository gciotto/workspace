from numpy.random import normal

def estimate (nMonths, iInvestment, iMonth, iYear, stepInvestment ):

    rates = normal (0.7e-2, 0.1e-3, nMonths)
    
    solde = iInvestment
    
    totalInterests = 0
    
    for i in range (nMonths):
        
        print ('After month %2d/%d: %.2f (at rate = %.6f, interest = %.2f)' % 
                (iMonth, iYear, solde, rates [i], rates [i]*solde))
        
        interest = solde * rates [i]
        
        solde = solde + interest + stepInvestment
        
        totalInterests += interest 
        
        iMonth += 1
        if iMonth > 12:
            iMonth = 1
            iYear += 1
            print ('Interests accumulated this year = %.2f' % totalInterests)
            totalInterests = 0

def estimate_required_time (amount, iInvestment, iMonth, iYear, stepInvestment ):
    
    solde = iInvestment
    rate = normal (0.7e-2, 0.1e-3)
    
    while solde*rate < amount :
            
        solde = solde * (1 + rate) + stepInvestment
    
        rate = normal (0.7e-2, 0.1e-3)
        
        iMonth += 1
        if iMonth > 12:
            iMonth = 1
            iYear += 1
            
    
    print ('Interest rate of %.2f will be achieved at %02d/%d (Savings = %.2f)' 
           % (solde * rate, iMonth, iYear, solde)) 
    
    
if __name__ == '__main__':
    iInvestment = 28952.03 + 715 - 350
    stepInvestment = 650 
    
    iMonth = 3
    iYear = 2016
    
    nYears = 8
    nMonths = nYears * 12
    
    estimate(nMonths, iInvestment, iMonth, iYear, stepInvestment)
    estimate_required_time(5e3, iInvestment, iMonth, iYear, stepInvestment)