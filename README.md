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
