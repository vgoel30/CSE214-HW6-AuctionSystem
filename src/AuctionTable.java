/** @author varungoel
 * Name: Varun Goel
 *ID: 109991128
 * email: varun.goel@stonybrook.edu
 * CSE 214 HW 6
 * Recitation Section: 7
 * Recitation TA: Anthony Musco
 * Grading TA: Zhichuang Sun
 */

/**
 * AuctionTable class
 */
import java.net.*;
import java.util.*;
import java.io.*;

import big.data.DataSource;
import big.data.IDataSource;

import java.io.Serializable;

public class AuctionTable extends HashMap implements Serializable{

	public static HashMap<String, Auction> table = new HashMap<String,Auction>();
	public AuctionTable(){

	}

	/**
	 * Uses the BigData library to construct an AuctionTable from a remote data source.
	 * @param URLink String representing the URL fo the remote data source.
	 * @throws IllegalArgumentException: Thrown if the URL does not represent a valid datasource (can't connect or invalid syntax).
	 */
	public static void buildFromURL(String URLink) throws IllegalArgumentException{

		//HashMap<String, Auction> table = new HashMap<String,Auction>();  //hash table with Auction ID (String) as key


		DataSource ds = null;
		//URL validation begins
		InputStream stream = null;
		try {
			URL url = new URL(URLink);
			try {
				stream = url.openStream();
			} catch (UnknownHostException ex) {
				System.out.println("Invalid URL");
				throw new IllegalArgumentException();
			} catch (IOException e) {
				System.out.println("Invalid URL");
				throw new IllegalArgumentException();
			}

			if (stream != null) {
				System.out.println("Valid URL\n");
			}

		} catch (MalformedURLException e) {
			System.out.println("Malformed URL");
			stream = null;
			throw new IllegalArgumentException();
		}//url validation ends

		//if url is valid, connect to it
		if(stream != null){
			ds = DataSource.connect(URLink).load();

			String[] sellers = null;
			String[] bids = null;
			String[] times = null;
			String[] ids = null;
			String[] bidders = null;
			String[] memory = null;
			String[] hardDrive = null;
			String[] cpu = null;

			try{
				sellers = ds.fetchStringArray("listing/seller_info/seller_name");
				bids = ds.fetchStringArray("listing/auction_info/current_bid");
				times = ds.fetchStringArray("listing/auction_info/time_left");
				ids = ds.fetchStringArray("listing/auction_info/id_num");
				bidders = ds.fetchStringArray("listing/auction_info/high_bidder/bidder_name");

				// the following are combined to get the information of the item

				memory = ds.fetchStringArray("listing/item_info/memory");
				hardDrive = ds.fetchStringArray("listing/item_info/hard_drive");
				cpu = ds.fetchStringArray("listing/item_info/cpu");
			}catch(Exception e){
				System.out.println("Tag doesn't exist");
			}



			for(int i = 0; i < ids.length; i++){
				String k = bids[i];

				int timeRemaining = 0;
				double currentBid = 0;
				String ID = "";
				String seller = "";
				String buyer = "";
				String description = "";

				//parses time begins
				if(times[i].indexOf("day") != -1){
					String time = times[i].substring(0,times[i].indexOf('d')-1).trim();
					//int t = 24*Integer.parseInt(time);
					int t = Integer.parseInt(time);
					timeRemaining = t;
				}

				else if(times[i].equals(""))
					timeRemaining = 0;
				else if(times[i].indexOf("h") != -1){
					String time = times[i].substring(0,times[i].indexOf('h')-1).trim();
					int t = Integer.parseInt(time);
					timeRemaining = t; 
				}//parsing for time ends

				//parsing for current bid begins
				if(bids[i].indexOf("$") != -1){
					k = bids[i].replaceAll("[$]","");
				}

				if(k.indexOf(",") != -1){
					k = k.replaceAll("[,]","");
				}
				currentBid = Double.parseDouble(k);
				//parsing for current bid ends

				//parsing for ID. ID will be used as Key. 
				ID = ids[i];

				//parsing for seller
				if(sellers[i].equals(""))
					sellers[i] = "N/A";
				seller = sellers[i];

				//parsing for buyer
				if(bidders[i].equals(""))
					bidders[i] = "N/A";
				buyer = bidders[i];

				//parsing for information begins. This combines multiple data entries.
				if(memory[i].equals(""))
					memory[i] = "N/A";
				if(hardDrive[i].equals(""))
					hardDrive[i] = "N/A";
				if(cpu[i].equals(""))
					cpu[i] = "N/A";

				description = description + memory[i].trim() + " " + hardDrive[i].trim() + " " + cpu[i].trim();

				//parsing for information ends.
				//insert data only if key doesn't already exist
				if(!table.containsKey(ID))
					table.put(ID, new Auction(timeRemaining, currentBid, ID, seller, buyer, description));

			}
		}
	}


	/**
	 * Manually posts an auction, and add it into the table.
	 * @param auctionID
	 * @param auction
	 * @return
	 * @throws IllegalArgumentException
	 */
	public  static boolean put(String auctionID, Auction auction) throws IllegalArgumentException{
		//If the given auction id is already in the table
		if(AuctionTable.table.containsKey(auctionID)){
			//System.out.println("DIDN'T ADD");
			throw new IllegalArgumentException();
		}

		else{
			AuctionTable.table.putIfAbsent(auctionID, auction);
			return true;
		}
	}

	/**
	 * Get the information of an Auction that contains the given ID as key
	 * @param auctionID
	 * @param auction
	 * @return An Auction object with the given key, null otherwise.
	 * @throws IllegalArgumentException
	 */
	public Auction get(String auctionID){
		return AuctionTable.table.get(auctionID);
	}

	/**
	 * Simulates the passing of time. Decrease the timeRemaining of all Auction objects by the amount specified. The value cannot go below 0.
	 * @param numHours
	 * @throws IllegalArgumentException
	 */
	public static void letTimePass(int numHours) throws IllegalArgumentException{

		if(numHours < 0)
			throw new IllegalArgumentException();

		//traversing through the hash map and calling the decrementTime method
		for(Map.Entry<String, Auction> entry : table.entrySet()) {
			entry.getValue().decrementTime(numHours);
		}
	}

	/**
	 * Iterates over all Auction objects in the table and removes them if they are expired (timeRemaining == 0).
	 */
	public static void removeExpiredAuctions(){
		//iterating through the array and removing all 

		Iterator<Map.Entry<String,Auction>> iter = table.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String,Auction> entry = iter.next();
			if(entry.getValue().timeRemaining <= 0){
				iter.remove();
			}
		}
	}

	/**
	 * Prints all the auctions in a tabular form
	 */
	public static void printTable(){
		System.out.print(String.format("%-15s%-20s%-1s%-15s%-15s%-20s%s","ID","BID","","SELLER","BIDDER","TIME","INFO"));
		System.out.println();
		for(Map.Entry<String, Auction> entry : table.entrySet()) {
			if(entry!=null)
				System.out.println(entry.getValue()+"\n");	
		}
	}

}