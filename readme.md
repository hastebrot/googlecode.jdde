# A DDE wrapper for Java

Fork of http://code.google.com/p/jdde/.

## Build Requirements

- Java Development Kit: http://www.oracle.com/technetwork/java/javase/downloads/jdk-6u25-download-346242.html
- ANT build tool: http://code.google.com/p/winant/
- ANT C++ build task: http://sourceforge.net/projects/ant-contrib/files/ant-contrib/cpptasks-1.0-beta5/
- MinGW C++ Compiler: http://dfn.dl.sourceforge.net/project/mingw/Installer/mingw-get-inst/mingw-get-inst-20100831/mingw-get-inst-20100831.exe

## Build Steps

Install all requirements (don't forget to choose the C++ Compiler while installing MinGW), then build and copy cpptasks.

        C:\>cd cpptasks-1.0b5
        C:\>ant
        C:\>copy target\lib\cpptasks.jar "C:\Program Files\WinAnt\lib"
        
Compile the DDE wrapper.

        C:\>cd googlecode.jdde\trunk
        C:\>set PATH=C:\MinGW\bin;%PATH%
        C:\>set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_25
        C:\>ant
        
That's all folks!
