from socket import *
import random #allows the server to get a random number
serverPort = 15001
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(('', serverPort)) # port and ip address
serverSocket.listen()# listen to a connection
print('The server is ready to receive')
connectionSocket, addr = serverSocket.accept() # Accepts the connection with the client
print("Client Connected:", addr)
number = random.randint(1,1001)# Gets random nume
print(number)
while True:
    try:
        print("waiting for number.")
        modifier = ''#a checker for the number when its a string
        changer = '' #a checker for the number when its a string
        guess = connectionSocket.recv(2048).decode()# takes the prompt from the client
        while guess.find('*') == -1: # if the server recieve only one letter it goes into a loop
            modifier += guess # incrememnt modifier into the value of guess
            guess = connectionSocket.recv(2048).decode()
        modifier += guess
        newmodifier = int(modifier.strip('*')) # take away the * in the string and turn it into a integer
        print('received: ',newmodifier)
        if newmodifier > 1000 or newmodifier < 1:
            print('out of range')
            connectionSocket.send("That is out of range. '\n".encode()) # sends message to the client
            continue
        if newmodifier == number:
            connectionSocket.send("Correct!, You Win! '\n'".encode())
            response = str(connectionSocket.recv(2048).decode()) # recieves the message from the client as a string
            while response.find('*') == -1:
                changer += response
                response = str(connectionSocket.recv(2048).decode())
            changer += response
            update = changer.strip('*')
            if update == 'yes':
                number = random.randint(1,1001) # creates a new random number for the client to guess in the new game
                print("your new number is: ", number)
                continue # returns back to the top of the loop
            elif update == 'no':
                print("Thank You for Playing")
                #connectionSocket.close()
                #serverSocket.close()
                break # exits the loop and ends the game
        elif newmodifier < number:
            connectionSocket.send(f"{newmodifier} Is Too Low! Try Again. '\n'".encode()) # sends the guess number and message to the client
            continue
        elif newmodifier > number:
            connectionSocket.send(f"{newmodifier} Is Too High! Try Again. '\n'".encode()) # sends the guess number and message to the client
            continue
    #except:
     #   print('Invalid input')
    except ValueError as e:
        connectionSocket.send("Invalid Input!"+'\n'.encode())
        continue
connectionSocket.close()
    