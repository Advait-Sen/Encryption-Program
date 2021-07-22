# Encryption Program
This is a program which basically encrypts text. The master branch is the java version, as it's the one I'm best with.
I will also create a python and javascript version in other branches of this repo, as well as perhaps translating it into
other programming languages as I see fit.

## Version - specific details
These are specific details and design choices specific to each version of the program, as each programming language is
different, and while the overall gist of it is mathematical and therefore inherently translatable between programming
languages, each one has its own quirks which change the exact details of implementation. Therefore, each version has its
own contributing section, as each version has a different way of accepting contributions.

### Java version
I made the Java version a Gradle project, as I am rather familiar with them from other work I've done. I made the Encoder.java
class be the main interface for anything which encodes in this project, because I found it more logical to do so. I also
have one big Utils.java file in my `utils` package, which I will keep that way due to the other files in there which are
more so experimental datastructures, which I am using to teach myself Computer Science (like the LinkedList, or the DoubleWayMapping,
which is an invention of my own).

#### Contributing
Import the Gradle project into your IDE, and that's about it. Make sure you have the appropriate JDK installed

