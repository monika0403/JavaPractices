# Java wait() notifyAll() -- Producer Consumer Example

## Overview

This program demonstrates inter-thread communication in Java using: -
wait() - notifyAll() - synchronized

Threads: - Sender → Producer - Receiver → Consumer - Data → Shared
object

Only one message at a time is transferred.

------------------------------------------------------------------------

## Shared Resource: Data class

``` java
class Data{
    private String packet;
    private boolean transfer=true;
}
```

  transfer   Meaning
  ---------- ----------------------
  true       Sender can send
  false      Receiver can receive

------------------------------------------------------------------------

## Why synchronized is used

``` java
public synchronized String receive()
public synchronized void send(String packet)
```

-   Only one thread can enter at a time.
-   Lock is acquired on the Data object.
-   Required for wait() / notifyAll().

------------------------------------------------------------------------

## send() Method (Producer)

``` java
public synchronized void send(String packet){
    while(!transfer){
        wait();
    }
    transfer=false;
    this.packet=packet;
    notifyAll();
}
```

Steps: 1. Wait until receiver consumes previous data. 2. Store message.
3. Switch turn to receiver. 4. Wake waiting threads.

------------------------------------------------------------------------

## receive() Method (Consumer)

``` java
public synchronized String receive(){
    while(transfer){
        wait();
    }
    transfer=true;
    String returnPacket=packet;
    notifyAll();
    return returnPacket;
}
```

Steps: 1. Wait until sender produces data. 2. Read message. 3. Switch
turn to sender. 4. Wake sender.

------------------------------------------------------------------------

## Why while instead of if

Use:

``` java
while(condition) wait();
```

Reasons: - Spurious wakeups - Multiple threads may wake - Condition must
be rechecked

------------------------------------------------------------------------

## What wait() does

1.  Releases lock
2.  Moves thread to WAITING state
3.  Sleeps until notify
4.  Reacquires lock before continuing

------------------------------------------------------------------------

## What notifyAll() does

-   Wakes all waiting threads
-   They compete for lock
-   Only one proceeds

------------------------------------------------------------------------

## Execution Flow

1.  Sender sends packet → transfer=false
2.  Receiver receives → transfer=true
3.  Repeat until "End"

------------------------------------------------------------------------

## Key Interview Points

-   wait() must be inside synchronized
-   wait() releases lock
-   sleep() does not release lock
-   Use while, not if
-   notify vs notifyAll

------------------------------------------------------------------------

## Modern Alternative

Use BlockingQueue instead:

``` java
BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
```

Cleaner and safer.
