###Transport protocol

We have to use TCP.

###Addresses and ports

ports : 2424

##Speak first

The server will speak first and give commands.

##Sequence

s. Welcome to Simple Calculator.

​	You can ask for addition (X + Y)

​	You can ask for subtraction (X - Y)

​	You can ask for multiplication (X * Y)

​	You can ask for division (X / Y)

​	You can leave with BYE

c. 3 + 3

s. 3.0 + 3.0 = 6.0

Do you need something else ? 

If you want to see the command again use CMD

c. BYE 

##FAIL MESSAGE

c. x + y 

s. Wrong syntaxe, try again

c. BYE 

##SYNTAX

We only use parsing from a string in utf-8.

##Closing and When

The client will send the command BYE and it will stop the server.