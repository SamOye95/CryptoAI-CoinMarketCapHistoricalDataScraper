Coinmarketcap scraper 
---------------------
Implemented in Java, uses libraries JSoup

Build using maven: mvn compile

Usage example: 
./get_history.sh -coin bitcoin -outfile BTC.csv -scrape

options:

-coin      name where name is that in Coinmarketcap with - for space e.g. bitcoin-cash

-start     date to start from 20130428

-end       data to end scrape 20130428

-outfile   csv file to write data to

The output columns are per coinmarketcap data output - as of this moment it is:

Date, Open, High, Low, Close. Volume, Market Cap


