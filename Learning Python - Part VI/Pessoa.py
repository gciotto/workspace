'''
Created on Jan 15, 2016

@author: gciotto
'''
from test.pyclbr_input import Other

class Pessoa(object):
    '''
    classdocs
    '''
    
    x = 'coco'


    def __init__(self, idade, nome = 'Joao',  sexo = 'M'):
        self.nome = nome
        self.idade = idade
        self.sexo = sexo
    
    def __add__ (self, other):
        return self.idade + other.idade
        

p1 = Pessoa(22, 'Gugu',  'M')
p2 = Pessoa(21)

print (p1.x, p2.x, Pessoa.x, p1 + p2)