# iban-generator
Generates IBAN for given Country names.

# Usage
To generate IBAN for given Country:

```
IBANGenerator ibanGenerator = new IBANGenerator();
String IBAN = ibanGenerator.generateIBAN("Austria");
```

```generateIBAN()``` makes sure that for given instance of ```IBANGenerator()```, Every IBAN returned is Unique.

Need to provide Country name as per https://en.wikipedia.org/wiki/Country_code