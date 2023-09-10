print("Hello World")
while True:
    foobar = input("Enter message...\n")
    print("You entered: " + foobar)
    if foobar == "exit":
        break
    else:
        exec(foobar)
exit()