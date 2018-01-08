package com.cryptoai.data;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.jsoniter.JsonIterator;

import lombok.ToString;

public class CoinmarketCap 
{
	@ToString
    public static class Coin {
		String id;	//": "bitcoin", 
		String name;	//": "Bitcoin", 
		String symbol;	//": "BTC", 
		String rank;	//": "1", 
		String price_usd;	//": "573.137", 
		String price_btc;	//": "1.0", 
		String _24h_volume_usd;	//": "72855700.0", 
		String market_cap_usd;	//": "9080883500.0", 
		String available_supply;	//": "15844176.0", 
		String total_supply;	//": "15844176.0", 
		String percent_change_1h;	//": "0.04", 
		String percent_change_24h;	//": "-0.3", 
		String percent_change_7d;	//": "-0.57", 
		String last_updated;	//": "1472762067"    
	}
	
	String coin = "bitcoin";
	String start = "20130428", end = "20180108";
	String dateFormatParse = "MMM dd, yyyy";
	String dateFormatGenerated = "yyyy-MM-dd";
	String url = "https://coinmarketcap.com/currencies/%s/historical-data/?start=%s&end=%s";
	String outfile = "scraped.csv";
	
	
    public static void main( String[] args ) throws Exception {
    	CoinmarketCap cmc = new CoinmarketCap();
		for (int ix=0; ix<args.length; ix++) { 
    		if ("-scrape".equals(args[ix])) 				{cmc.scrape();}
    		else if ("-outfile".equals(args[ix])) 			{cmc.outfile = args[++ix];}
    		else if ("-coins".equals(args[ix])) 			{cmc.coins();}
    		else if ("-coin".equals(args[ix])) 				{cmc.coin = args[++ix];}
    		else if ("-start".equals(args[ix])) 			{cmc.start = args[++ix];}
    		else if ("-end".equals(args[ix])) 				{cmc.end = args[++ix];}
		}
    }

    void coins() throws Exception {
    	String data = IOUtils.toString(new URL("https://api.coinmarketcap.com/v1/ticker/?limit=0"));
    	Coin[] coinList = JsonIterator.deserialize(data, Coin[].class);
    	for (Coin coin: coinList)
    		System.out.println(coin);
    	
    }
    void scrape() throws Exception {
    	DateFormat dfIn = new SimpleDateFormat(dateFormatParse);
    	DateFormat dfOut = new SimpleDateFormat(dateFormatGenerated);
		Document doc = Jsoup.connect(String.format(url, coin, start, end)).get();
    	String title = doc.title();
    	Elements hdr = doc.select("#historical-data > div > div.table-responsive > table > thead > tr");
    	Element h = hdr.first();
    	StringBuilder bh = new StringBuilder();
    	for (Node n: h.getElementsByTag("th")) {
    		if (bh.length()>0) bh.append(",");
    		bh.append(n.childNode(0).toString().toLowerCase());
    	}
    	
    	Elements rows = doc.select("#historical-data > div > div.table-responsive > table > tbody > tr");
    	Element r = rows.first();
    	List<String> output = new ArrayList(); 
    	do {
	    	StringBuilder b = new StringBuilder();
	    	int ix = 0; for (Node n: r.getElementsByTag("td")) {
	    		String mapped = n.childNode(0).toString();
	    		switch (ix++) {
	    		case 0:	mapped = dfOut.format(new Date(dfIn.parse(mapped).getTime())); break;
	    		}
	    		if (b.length()>0) b.append(",");
	    		b.append(mapped.replaceAll(",", ""));
	    	}
	    	output.add(b.toString());
	    	//System.out.println(b);
    	} while ((r = (Element)r.nextElementSibling())!=null);
    	Collections.sort(output);
    	PrintWriter writer = new PrintWriter(new FileWriter(outfile));
    	writer.println(bh);
    	for (String entry: output)
    		writer.println(entry);
    	writer.close();
    	
    }
}
