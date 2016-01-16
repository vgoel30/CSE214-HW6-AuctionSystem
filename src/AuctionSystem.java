/** @author varungoel
 * Name: Varun Goel
 *ID: 109991128
 * email: varun.goel@stonybrook.edu
 * CSE 214 HW 6
 * Recitation Section: 7
 * Recitation TA: Anthony Musco
 * Grading TA: Zhichuang Sun
 */

import java.util.*;
import java.io.*;

import big.data.DataSource;

import java.io.Serializable;

public class AuctionSystem  implements Serializable{


	/**
	 * Method to load from the deserialized file
	 */
	public  static void load() {
		try
		{
			FileInputStream file = new FileInputStream("auction.obj");
			ObjectInputStream inStream = new ObjectInputStream(file);
			System.out.println("Deserialized auction.obj");
			AuctionTable.table =  (AuctionTable) inStream.readObject();
			inStream.close();
		}catch(Exception e){
			System.out.println("New auction.obj");
			AuctionTable.table = new AuctionTable();
		}
	}

	/**
	 * Saves the contents into a .obj file
	 */
	public static void save(){

		try{
			{
				FileOutputStream fileOut = new FileOutputStream("auction.obj");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(AuctionTable.table);
				out.close();
				fileOut.close();
				System.out.printf("Serialized data is saved in auction.obj");

			}}catch(Exception e){
				System.out.println("Couldn't save data to auction.obj");
			}
	}

	/**
	 * Static method to print the menu from where the user can select what they want to do next
	 */
	public static void printMenu(){
		System.out.println("(D) Import data from URL	(A) Create new auction	(B) Bid on an item");
		System.out.println("(I) Get info on an auction	(R) Remove expired auction	(P) Print auctions");
		System.out.println("(T) Let time pass	(Q) Quit");
	}


	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String username = "";
		String input;

		do{
			System.out.println("Enter a username: ");
			username = s.nextLine();
		}while(username.length() <= 0);


		load();

		do{
			printMenu();
			input = s.nextLine().toUpperCase();

			//extract data from a URL and build the table
			if(input.equals("D")){
				System.out.println("Enter an URL");

				String URL = s.nextLine();

				try{
					AuctionTable.buildFromURL(URL);
				}catch(Exception e){
					System.out.println("Invalid URL\n");
				}
			}

			//create new auction
			else if(input.equals("A")){
				int time = 0;

				System.out.println("Enter auction ID: ");
				String ID = s.nextLine();

				do{
					System.out.println("Enter time for auction: ");
					time = s.nextInt();
				}while(time < 0);

				s.nextLine();
				System.out.println("Enter item info: ");
				String info = s.nextLine();

				try{
					AuctionTable.put(ID, new Auction(time,0,ID,username,"N/A",info));}
				catch(Exception e){
					System.out.println("Auction with this ID already exists");
				}
				//Auction(int time, double bid, String ID, String seller, String buyer, String info)
			}

			//place a new bid
			else if(input.equals("B")){
				double bid = 0;


				System.out.println("How much would you like to bid?");
				bid = s.nextDouble();
				s.nextLine();

				String ID = "";
				System.out.println("Enter auction ID");
				ID = s.nextLine();

				if(AuctionTable.table.containsKey(ID)){
					try{
						AuctionTable.table.get(ID).newBid(username,bid);
					}
					catch(Exception e){
						System.out.println("Sorry, auction is closed");
					}
				}
				else
					System.out.println("Auction with this ID doesn't exist\n");
			}

			//get info on an auction
			else if(input.equals("I")){
				String ID = "";
				System.out.println("Enter auction ID");
				ID = s.nextLine();

				if(AuctionTable.table.containsKey(ID)){
					System.out.println(AuctionTable.table.get(ID)+"\n");
				}
				else
					System.out.println("Auction with this ID doesn't exist\n");
			}

			//print the table
			else if(input.equals("P")){
				if(AuctionTable.table.isEmpty())
					System.out.println("EMPTY!");

				else{
					AuctionTable.printTable();
					System.out.println();
				}
			}

			//remove expired auctions
			else if(input.equals("R")){
				AuctionTable.removeExpiredAuctions();
			}

			//lets time pass
			else if(input.equals("T")){
				int hours = 0;

				do{
					System.out.println("How many hours do you want to pass? (+ve numbers only)");
					hours = s.nextInt();
				}while(hours < 0);

				AuctionTable.letTimePass(hours);
			}

		}while(!input.equals("Q")); 

		save();

	}
}

