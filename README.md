# Funghi
A WebIDL offline-parser library.

## A note on WebIDL
Since the WebIDL is a so called living standard (no proper versioning, no changelog, no version in the actual IDL-files, ...) 
parsing IDL files comes with a certain amount of trickyness,
and thus judging a parser error as a syntax error (and if so - for what version of the standard?) or as a bug/fault/error 
in the parser implementation is - to say the least - tricky. Thus I implore you, if you stumble upon an IDL file that 
Funghi does not parse, or do not parse properly please contact the author so that the library can - if need be - be 
rectified and the actual IDL file be added to the testing resources. Especially so if the file is used "in the wild" - 
as in an actual browser!

## Links of interest
* [WebIDLpedia](https://dontcallmedom.github.io/webidlpedia/)
* [The *living* standard WebIDL](https://webidl.spec.whatwg.org/)
* [Worn and torn Regexr](https://regexr.com/)

## Questions to the standard
* Can dictionary members be union types?
* Can getters be defined using unnion type as return type?
* Can getter/setter/deleter arguments use union types?
* Can an identifier be the value of a keyword? (ex. an operation named "any" or a dictionary named "interface" or "getter")
* Can default values be a string consisting of whitespace characters that is defined by not using an escape sequence? (any combination of space, new line, return, tab, ...)
