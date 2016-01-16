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
 * Auction class
 */

import java.io.Serializable;
import java.util.*;
public class Auction implements Serializable{

	/**
	 * Default constructor
	 */
	public Auction(){

	}

	/**
	 * Constructor with parameters
	 * @param time
	 * @param bid
	 * @param ID
	 * @param seller
	 * @param buyer
	 * @param info
	 */
	public Auction(int time, double bid, String ID, String seller, String buyer, String info){
		timeRemaining = time;
		currentBid = bid;
		auctionID = ID;
		sellerName = seller;
		buyerName = buyer;
		itemInfo = info;
	}



	int timeRemaining;
	double currentBid;
	String auctionID, sellerName, buyerName, itemInfo;

	/**
	 * Decreases the time remaining for this auction by the specified amount. If time is greater than the current remaining time for the auction, then the time remaining is set to 0 (i.e. no negative times)
	 * @param time
	 */
	public void decrementTime(int time){
		if(timeRemaining < time)
			timeRemaining = 0;
		else
			timeRemaining -= time;
	}

	/**
	 * Makes a new bid on this auction. If bidAmt is larger than currentBid, then the value of currentBid is replaced by bidAmt and buyerName is is replaced by bidderName.
	 * @param bidderName
	 * @param bidAmt
	 * @throws ClosedAuctionException
	 */
	public void newBid(String bidderName, double bidAmt) throws ClosedAuctionException{
		if(timeRemaining <= 0)
			throw new ClosedAuctionException();

		if(bidAmt > currentBid){
			currentBid = bidAmt;
			buyerName = bidderName;
		}
		else
			System.out.println("Must bid higher to be eligible!");
	}

	/**
	 * Overrides the toString method to reprenent the auction's information in a tabular format
	 */
	public String toString(){
		// return String.format("%-11s$%.2f%-2s%-5s%-5s  %d %-5s%s",auctionID,currentBid,"",sellerName,buyerName,timeRemaining,"",itemInfo);
		return String.format("%-15s$%.2f%-15s%-15s%-15s%d hours%-15s%s",auctionID,currentBid,"",sellerName,buyerName,timeRemaining,"",itemInfo);
	}

	/**
	 * Accessor method for timeRemaining
	 * @return time remaining in the auction
	 */
	public int gettimeRemaining(){
		return this.timeRemaining;
	}

	/**
	 * Accessor method for currentBid
	 * @return double value of the current bid in the auction
	 */
	public double getcurrentBid(){
		return this.currentBid;
	}

	/**
	 * Accessor method for auction ID
	 * @return String value of the auction ID
	 */
	public String getauctionID(){
		return this.auctionID;
	}

	/**
	 * Accessor method for seller name
	 * @return string value of seller name
	 */
	public String getsellerName(){
		return this.sellerName;
	}

	/**
	 * Accessor method for buyer name
	 * @return String value of buyer name
	 */
	public String getbuyerName(){
		return this.buyerName;
	}

	/**
	 * Accessor method for item info
	 * @return String representation of item info
	 */
	public String getitemInfo(){
		return this.itemInfo;
	}

}
