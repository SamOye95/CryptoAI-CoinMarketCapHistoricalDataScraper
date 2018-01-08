Implemented in Java, uses libraries JSoup, IOUtils, Lombok, JSoniter

Build using maven: mvn compile

Usage example: ./get_history.sh -coin bitcoin -outfile BTC.csv -scrape

options:

-coin name where name is that in Coinmarketcap with - for space e.g. bitcoin-cash

-start date to start from e.g. and default 20130428

-end data to end scrape e.g. and default 20180107

-outfile csv file to write data to

The output columns are per coinmarketcap data output - as of this moment it is:

Date, Open, High, Low, Close. Volume, Market Cap
