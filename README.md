# Koshka
Koshka is a desktop pet program based on Java.
## How to use?
Go to the project root, type `mvn clean package` then go to target/, copy main jar and jars in dependency/ to the same place, now you can execute the main jar.
## How to develop?
First, you should manage to import Koshka.jar.

Then, create a class that extends KoshkaTemplate, just like Koshka do, and now you can develop!

And, finally, create a pull request in https://github.com/silvicky/Koshka-extra to make it public.
## Requirement
Koshka needs Java 8 or higher to run.
## Operating Systems status
In theory it can run on any platform that supports Java and behaves the same, but the truth is cruel.

- Windows (ver. 7~11): Works fine.
- ReactOS (ver. 0.4.15 debug): Obvious visual glitch, see [CORE-18256](https://jira.reactos.org/browse/CORE-18256).
- Linux (ver. Debian Sid): Unable to repaint, I use the resize glitch to temporarily fix it.

If you have tested it, you're welcome to edit this section.
## About
I design it to bring fun to my desktop.

It's inspired by Neko, a pet program that is designed for Windows 9X.

Koshka(Russian: Кошка) means cat, just like Neko(Japanese: 猫) , so don't feel confused about whether it's a cat or a dog.

Contributions are welcomed!

This program is far from it's final stage, I'm adding many things to it.
