k = 24.94e-3;
Pr = 0.717;

v = 13.7006e-6;
u = 89.408;

L = 10.16e-2;
delta = 5.08e-2;

Tw = 37.778;
Tinf = -28.89;

Re = u*L/v
Re_laminar = u*(L+delta)/v

Nu_turbulento = 0.037*(Re^0.8)*Pr/(1+2.443*(Re^(-0.1))*(Pr^(2/3) -1))

h_turbulento = Nu_turbulento*k/L
h_laminar = 0.664*k/L*(Re_laminar^0.5)*(Pr^(1/3))*((1 - (L/(L+delta))^(3/4))^(2/3))

q_turbulento = h_turbulento * (Tw - Tinf)
q_laminar = h_laminar * (Tw - Tinf)