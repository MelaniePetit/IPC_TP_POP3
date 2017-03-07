# IPC_TP_POP3
_TP Client-Serveur "Post Office Protocol 3"_

## QUIT
```diff
+OK POP3 server signing off
-ERR some deleted messages not removed
```
:memo: _**Examples:**_
```diff
+OK dewey POP3 server signing off (maildrop empty)
+OK dewey POP3 server signing off (2 messages left)
```

## STAT
```diff
+OK id bytes
```

## LIST _[num]_

```diff
+OK suivi d’un listing de scan
```
:memo: _**Example:**_
```diff 
+OK 2 messages (320 octets) 
+1 120 
+2 200 

-ERR numéro de message invalide  
```
:memo: _**Example:**_
```diff 
-ERR no such message, only 2 messages in maildrop
```

 :warning: _**Warning :** un numéro de message (optionnel), qui, s’il est présent, NE peut PAS faire référence à un message marqué comme effacé_

APOP _[user]_ _[password]_
```diff
+OK maildrop has 1 message (369 octets)
-ERR permission refused
```

## RETR _[num]_
```diff
+OK suivi du message
```
:memo: _**Example:**_
```diff 
+OK 120 octets 
+message

-ERR numéro de message invalide
```
:memo: _**Example:**_
```diff 
-ERR no such message, only 2 messages in maildrop
```

##DELE _[num]
```diff
+OK message effacé
```
:memo: _**Example:**_
```diff 
+OK message 1 deleted
+message

-ERR numéro de message invalide
```
:memo: _**Examples:**_
```diff 
-ERR message 2 already deleted
-ERR no such message, only 2 messages in maildrop
```
