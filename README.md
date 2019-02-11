# README #

This repository is a solution for Java Algorithms homework number 7, second year of Tallinn University of Technology.

## Command line examples. Näidete kasutamine käsurealt ##
#### Compilation. Kompileerimine: ####

```
#!bash

javac -cp src src/Huffman.java
```

#### Execution. Käivitamine: ####

```
#!bash

java -cp src Huffman
```


### Usage of tests. Testide kasutamine ###
#### Compilation of a test. Testi kompileerimine: ####

```
#!bash

javac -encoding utf8 -cp 'src:test:test/junit-4.12.jar:test/hamcrest-core-1.3.jar' test/HuffmanTest.java

```
In Windows replace colons by semicolons. Sama Windows aknas (koolonite asemel peavad olema semikoolonid):

```
#!bash

javac -encoding utf8 -cp 'src;test;test/junit-4.12.jar;test/hamcrest-core-1.3.jar' test/HuffmanTest.java


```

#### Running a test. Testi käivitamine: ####

```
#!bash

java -cp 'src:test:test/junit-4.12.jar:test/hamcrest-core-1.3.jar' org.junit.runner.JUnitCore HuffmanTest
```

The same for Windows. Sama Windows aknas (koolonite asemel semikoolonid):

```
#!bash

java -cp 'src;test;test/junit-4.12.jar;test/hamcrest-core-1.3.jar' org.junit.runner.JUnitCore HuffmanTest
```
