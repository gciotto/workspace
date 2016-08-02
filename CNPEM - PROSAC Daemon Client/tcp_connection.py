import sys
import socket
import struct

STANDARD_PORT = 4016

class ProsacDaemonConnection(object):
	# Attributes
	server_port = None
	server_ip = None
	s = None
	
	# Methods
	def __init__(self):
		# Initial definitions
		self.server_port = None
		self.server_ip = None
	
	def setupConnection(self, ip):
		# Try to connect with server
		self.server_port = STANDARD_PORT
		self.server_ip = ip

		return 0
		
	def closeConnection(self):
		# Closes the connection and clears the socket
		self.s.close()
		#self.s = None
		
	def sendCommand(self, cmd):
		
		try:
			self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
			#self.s.setblocking(0)
			self.s.settimeout(0.1)
			self.s.connect((self.server_ip,self.server_port))
		except socket.timeout:#, (value, message):
			#print "SOCKET TIMEOUT"
			self.s.close()
			return -1
		except socket.error, msg:#, (value, message):
			print "SOCKET ERROR", msg#, message
			self.s.close()
			return -1
		else:	
			#self.s.setblocking(1)
			self.s.settimeout(1)	
			# Packs the command and size bytes
			send_str = struct.pack('BB',cmd,0)
			
			# Send information to PROSAC
			try:
				nbytes = self.s.send(send_str)
			except socket.error:
				return -1
			except socket.timeout:
				return -1
				
			if (nbytes != len(send_str)):
				print 'Problems sending commands!'
				return -1
			
			return 0	

		
	def recvAnswer(self):
		
		try:
			raw_recv = self.s.recv(2)
		
		except socket.timeout:
			return -1
		
		except socket.error:
			return -1

		header = struct.unpack('BB', raw_recv)	
		self.cmd = raw_recv[1]
		
		return 0


	def CheckStatus(self, ip):
		
		# Primeiro, configura o socket
		self.setupConnection(ip)

		# Cria o socket e envia 		
		if self.sendCommand(0xEE):
			return -1

		# Recebe o byte de resposta da SBC
		if self.recvAnswer():
			return -1

		# Resposta recebida, SBC esta on-line	
		self.s.close()
		return 0

	def RebootSBC(self, ip):
		# Primeiro, configura o socket
		self.setupConnection(ip)

		# Cria o socket e envia comando de reboot		
		if self.sendCommand(0xEF):
			return -1

		# Recebe o byte de resposta da SBC
		if self.recvAnswer():
			return -1
		print "enviou comando de reboot", ip

		# Resposta recebida, SBC esta on-line
		self.s.close()

		return 0
	
