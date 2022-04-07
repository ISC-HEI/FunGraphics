# FunGraphics

A library for displaying things.

## Origin

This library has been developed for the [`inf1`](https://inf1.begincoding.net) course at
the [HES-SO Valais//Wallis](https://www.hevs.ch).

## Compiling

* UNIX-like (Linux, MacOS X, ...) : ```./gradlew jar```
* Windows : ```gradlew.bat jar```

result : `lib/build/libs/fungraphics-VERSION.jar`

## Running

```gradle run```

## How to release (and have a nice version number)

0. git commit
0. git tag -a MAJOR.MINOR.SUB -m "tag vMAJOR.MINOR.SUB"
0. git push origin MAJOR.MINOR.SUB
0. [compile](#Compiling)
