# TritonBot

## Usage

#### Build & Run
```shell
mkdir build
cd build
cmake ..
make -j
./TritonBot [arg1] [arg2] ...
```
run ./TritonBot -h to print the help message

to rebuild proto source
```shell
rm proto/proto_generated.cmake_msg
rm -rf proto/ProtoGenerated
cd build
make proto
```


refer to https://github.com/IEEE-UCSD-RoboCupSSL/TritonsRCSC-Software-Pack for more details

## Notes for student developers

Recommend using CLion, but it's not free. vscode might be a good option too.

if you aren't familiar with using vscode:
* when using vscode
    * vscode intellisense could be quite slow sometimes, be patient and don't get panic if you see lots of error squiggles when openning a source file for the first time. (Especially include errors)
    * recommended plug-in:
        *  c/c++ (microsoft: intellisense, debugging, ...)
        *  Better C++ Syntax
        *  CMake (CMake language support for vscode)
        *  CMake Tools (Extended CMake support in vscode)
        *  vscode-proto3 (Protobuf3 support for vscode)
        *  Header source switch
        *  Include Autocomplete
    *  use right click -> rename symbols to rename anything, similar to intellij/Clion's refactor/rename
    *  Alt + O to toggle between header and source
    *  Alt + left/right arrow to go back or forward
    *  ctrl+shift+P then type in CMake:xxx
        *  use "CMake:Build Target" to build
        *  use "CMake:Debug" to open gdb session (google how to set break point, super easy)
    *  The magnifier icon on the left column is very useful to do global search
    *  Additional recommended vscode plugins:
        *  Git History
        *  GitLens
        *  Markdown All in One
        *  vscode-icons
        *  Live Share (highly recommended for teamwork)
        *  Txt Syntax
