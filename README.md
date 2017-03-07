# IPC_TP_POP3
TP Client-Serveur "Post Office Protocol 3"


##QUIT
```diff
+ +OK POP3 server ready
```

##STAT
```diff
+ +OK id bytes
```

##LIST [num]

```diff
+ +OK suivi d’un listing de scan

Example:
+OK 2 messages (320 octets) 
S : 1 120 
S : 2 200 

- -ERR numéro de message invalide  

Example: 
-ERR no such message, only 2 messages in maildrop
```

APOP____________________________________________



RETR____________________________________________



DELE____________________________________________
