jnotify-self-contain
====================
JNotify works on Linux with INotify support (Tested on 2.6.14), Mac OS X 10.5 or
higher (Tested on 10.6.2), and on Windows XP/2K/NT (Tested on XP and on Windows 7).

You can test run JNotify with the command:
java -Djava.library.path=. -jar jnotify-0.94.jar [directory]
which will monitor the specified directory or the current directory if not specified and output events to the console.

To use JNotify, You will need to have the appropriate shared library in your java.library.path

---------------------

##We hack it under v0.94##
     * JNotify work together with a native library file. If we put the jnotify.jar to private maven repository,
     * we will download and set runtime library manually, judge system environment by ourselves.
     * If our application environment beyond our control, we couldn't run it well.
     * The main modifications are below:
     * I packaged the native library files into jnotify.jar.
     * When the relevant class is loading, it will find the library file in jar and write to
     * application "libs" direction. After that, load it.
