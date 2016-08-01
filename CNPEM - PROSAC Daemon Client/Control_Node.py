'''
Created on 01/08/2016

@author: gciotto
'''

import threading

class Control_Node_State():
    DISCONNECTED, CONNECTED, REBOOTING, CMD_OK = range(4)
    
    @staticmethod
    def toString(state):
        
        if state == Control_Node_State.DISCONNECTED:
            return "Disconnected"
        
        elif state == Control_Node_State.CONNECTED:
            return "Connected"
        
        elif state == Control_Node_State.REBOOTING:
            return "Rebooting"
        
        return "Command OK"


class Control_Node():

    def __init__(self, network_id = 0, node_id = 0, name = "r0n0", ip = "10.128.0.0", state = Control_Node_State.DISCONNECTED):
        
        
        self.network_id = network_id
        self.node_id = node_id
        self.dns_name = name
        self.ip_address = ip
        
        self.state = state
        
        self.info_mutex = threading.Lock()
        
    def changeState(self, new_state):
        
        self.info_mutex.acquire()
        
        self.state = new_state
        
        self.info_mutex.release()
        
        
    def isConnected(self):
        
        self.info_mutex.acquire()
        
        r = (self.state == Control_Node_State.CONNECTED)
        
        self.info_mutex.release()
        
        return r
    
    def __str__ (self):
        
        self.info_mutex.acquire()
        
        __r_str =  "Network ID: %d, Node ID: %d, DNS Name: %s, IP Address: %s, Current state: %s" % (self.network_id, self.node_id, self.dns_name, self.ip_address, Control_Node_State.toString(self.state))
        
        self.info_mutex.release()
        
        return __r_str


class Network_Nodes():

    nodes = []
     
    #redes 36 e 38
    nodes.append(Control_Node(36,1,'r36n1','10.128.1.127'))
    nodes.append(Control_Node(38,2,'r38n2','10.128.1.120'))
    
    # rede 45
    # 1 a 14
    nodes.extend([Control_Node(45,i,'r45n%d'%i,'10.128.45.%d'%i) for i in range(1,15)])
    
    #rede 46
    # 1 a 9, impares
    nodes.extend([Control_Node(46,i,'r46n%d'%i,'10.128.46.%d'%i) for i in range(1,10,2)])
    # 10 a 14
    nodes.extend([Control_Node(46,i,'r46n%d'%i,'10.128.46.%d'%i) for i in range(10,15)])
    
    #nodes.append(Control_Node(45,9,'r45n9','10.128.45.9'))
    #nodes.append(Control_Node(45,10,'r45n10','10.128.45.10'))
    #nodes.append(Control_Node(45,11,'r45n11','10.128.45.11'))
    #uvx_network.append(Control_Node(45,11,'r45n11','10.128.45.11'))
    
    # Adicionado em 21/03/2016 (cavidades de RF do anel)
    nodes.append(Control_Node(48, 1, "r48n1", "10.128.48.1"))
    
    # Adicionado em 18/04/2016 (medicoes de temperatura das cavidades de RF)
    nodes.append(Control_Node(48, 4, "r48n4", "10.128.48.4"))
    
    # Adicionado em 21/03/2016 (cavidades de RF do anel)
    nodes.append(Control_Node(48, 5, "r48n5", "10.128.48.5"))
    
    # Adicionado em 05/07/2016 (instalacao de fontes corretoras do Sirius no LINAC)
    nodes.append(Control_Node(49, 1, "r49n1", "10.128.49.1"))
    
    # Adicionado em 14/07/2016 (no com espiao da referencia externa da fonte A6QF01)
    nodes.append(Control_Node(49, 2, "r49n2", "10.128.49.2"))
            