import socket
import sys

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_address = ('localhost', 4000)
print >>sys.stderr, 'starting up on %s port %s' % server_address
sock.bind(server_address)

sock.listen(1)

while True:
    # Wait for a connection
    print >>sys.stderr, 'waiting for a connection'
    connection, client_address = sock.accept()

    try:
        print >>sys.stderr, 'connection from', client_address

        # Receive the data in small chunks and retransmit it
        while True:
            data = connection.recv(4)
            x = ord(data[0])
            print >>sys.stderr, 'received "%s"' % data
            if ord(data[0]) == 2:
                print >>sys.stderr, 'IDENT received...'
                
                bytelist = ['\x02', '\x16', '\x01' , '\x02' , '\x03', '\x04', '\x3F', '\x3F', '\x3F', '\x3F', '\x3F', '\x3F', '\x3F', '\x3F', '\x3F', '\x3F', '\x3F', '\x3F','\x3F', '\x3F', '\x3F', '\x3F','\x3F', '\x3F', '\x3F', '\x3F','\x3F', '\x3F', '\x3F', '\x3F','\x3F', '\x3F', '\x3F', '\x3F', '\x01', '\x02', '\x03', '\x04', '\x05', '\x06']
                connection.send(''.join(bytelist).decode('utf-8'))

                #connection.sendall(data)
            else:
                print >>sys.stderr, 'no more data from', client_address
                break
            
    finally:
        # Clean up the connection
        connection.close()