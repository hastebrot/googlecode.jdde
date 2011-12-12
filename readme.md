# A DDE wrapper for Java

Fork of http://code.google.com/p/jdde/.

## Build Requirements

- Java Development Kit: http://www.oracle.com/technetwork/java/javase/downloads/jdk-6u25-download-346242.html
- ANT build tool: http://code.google.com/p/winant/downloads/detail?name=winant-install-v4.1.exe&can=2&q=
- ANT C++ build task: http://sourceforge.net/projects/ant-contrib/files/ant-contrib/cpptasks-1.0-beta5/
- MinGW C++ Compiler: http://dfn.dl.sourceforge.net/project/mingw/Installer/mingw-get-inst/mingw-get-inst-20100831/mingw-get-inst-20100831.exe

## Build Steps

Install all requirements (don't forget to choose the C++ Compiler while installing MinGW), then build and copy cpptasks.

        C:\>cd cpptasks-1.0b5
        C:\>set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_25
        C:\>ant
        C:\>copy target\lib\cpptasks.jar "C:\Program Files\WinAnt\lib"
        
Compile the DDE wrapper.

        C:\>cd googlecode.jdde\trunk
        C:\>set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_25
        C:\>set PATH=C:\MinGW\bin;%PATH%
        C:\>gcc --version
        gcc (GCC) 4.5.0
        C:\>ant
        
That's all folks!

## Alternative Build with MinGW-W64 compiler

- http://mingw-w64.sourceforge.net/
  - WIN64 Downloads > Personal Builds > sezero_4.5_20111101
  - WIN32 Downloads > Personal Builds > sezero_4.5_20111101

### Compiling on an x86-windows host

- jdde_32.dll (native compiler)
  - mingw-w32-bin_i686-mingw_20111101_sezero.zip
- jdde_64.dll (cross compiler)
  - mingw-w64-bin_i686-mingw_20111101_sezero.zip

### Compiling on an x64-windows host

- jdde_32.dll (using cross compiler)
  - <?no compiler available?>
- jdde_64.dll (using native compiler)
  - mingw-w64-bin_x86_64-mingw_20111101_sezero.zip
  
### Compile the DDE wrapper.

        C:\>set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_25
        C:\>set MINGW_32=C:\_MINGW\mingw-w32-bin_i686-mingw_20111101_sezero\mingw32
        C:\>set MINGW_64=C:\_MINGW\mingw-w64-bin_i686-mingw_20111101_sezero\mingw64
        C:\>set PATH=%MINGW_32%\bin;%PATH%
        C:\>set PATH=%MINGW_64%\bin;%PATH%
        C:\>ant
