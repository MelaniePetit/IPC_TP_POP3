# IPC_TP_POP3
_TP Client-Serveur "Post Office Protocol 3"_

## QUIT
```diff
+OK déconnexion au serveur effectuée
-ERR certains messages n'ont pas pu être supprimés
```
:memo: _**Examples:**_
```diff
+OK dewey POP3 server signing off (maildrop empty)
+OK dewey POP3 server signing off (2 messages left)
```

## STAT
```diff
+OK nombre de messages, taille totale
```
:memo: _**Example:**_
```diff
+OK 2 320
```

## LIST _[num]_

```diff
+OK nombre de message, taille totale, suivi d’un listing de scan
-ERR numéro de message invalide  

```
:memo: _**Success Example:**_
```diff 
+OK 2 messages (320 octets) 
+1 120 
+2 200 
```
:memo: _**Error Example:**_
```diff 
-ERR no such message, only 2 messages in maildrop
```

 :warning: _**Warning :** un numéro de message, s’il existe, NE peut PAS faire référence à un message marqué comme effacé._

APOP _[user]_ _[password]_
```diff
+OK maildrop has 1 message (369 octets)
-ERR permission refused
```

## RETR _[num]_
```diff
+OK taille du message, suivi du message
-ERR numéro de message invalide
```
:memo: _**Success Example:**_
```diff 
+OK 120 octets 
+message
```
:memo: _**Error Example:**_
```diff 
-ERR this message doen't exist
```

##DELE _[num]_
```diff
+OK message marqué pour effacement
-ERR numéro de message invalide
```
:memo: _**Success Example:**_
```diff 
+OK message 1 marked
```
:memo: _**Error Examples:**_
```diff 
-ERR message 2 already marked
-ERR no such message, only 2 messages in maildro / this message doesn't exist
```
