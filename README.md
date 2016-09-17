# kcap

Privilege escalation by design on OS X. By scriptjunkie https://www.scriptjunkie.us/

Demonstration here: https://vimeo.com/183135795

This program simply uses screen captures and programmatically generated key and mouse events to locally and 
graphically man-in-the-middle an OS X password prompt to escalate privileges.

OS X makes this interception easy because, in contrast with Windows' UAC prompt, 
unprivileged processes are allowed to interact with and spoof input to the password request window.

This program polls the screen to detect a system authentication prompt, 
closes it, replaces it with a cloned version, then when the user enters a password
it saves the password, re-opens the original password prompt, moves it offscreen, 
types in the password to the original password prompt. Then it moves the orignal prompt back to its original position and hits enter.

This may sound like a lot, but it only results in a brief flash in the UI that is hard to see.

The peanut gallery might point out that there is an empty dock item that is created when you run this, 
and it's possible to see a few pixels in the corner briefly show up when you run the attack.
This is only because I was lazy and have never programmed an OS X GUI program before, so I just spent a couple hours and did it in Java.
If you write a native program you can make both of those go away.

I tested it on a Macbook Air at the default resolution 1440x900 against most of the password prompts from the system preferences application.
It probably will get the pixel calculations wrong on other systems so you'll need to test and possibly modify some of the 
offsets if you want to run it elsewhere.

If Apple "fixes" this attack vector by denying unprivileged programs the right to interact with that window, it doesn't matter;
unprivileged programs could simply modify the shortcut that starts the system settings application and have it start a cloned version 
instead that does the same thing except also recording your password.

Common wisdom would have you believe when you run sudo that you are only granting root privileges to one command at one point in time.
In reality, you are granting root privileges to any hacker who has ever run any code in any process at any previous time in your account
and decided they wanted escalated privileges.

Having users escalate privileges from an unprivileged desktop will never be a real or effective security barrier. 
Instead, use separate accounts for admin tasks and day-to-day tasks. Don't browse the internet or check email when 
logged in to your admin account and don't enter your admin password when logged into your day-to-day account.
