import java.util.ArrayList;


public class Helper {

	public static void main(String[] args) {



		String ID = "511601118";
		double bid = 620;
		String seller = "cubsfantony";
		String buyer = "gosha555@excite.com ";
		int time = 110;
		String info = "Pentium III 933 System - 256MB";


		System.out.printf("%-14s$%.2f%-5s%-16s%-26s%d seconds%-5s%s",ID,bid,"",seller,buyer,time,"",info);
		System.out.println("\n\n\n");


		ArrayList<Integer> numbers = new ArrayList<Integer>();

		numbers.add(5);
		//System.out.println(numbers.get(0));
		
		Object[] array = new Object[5];
		
		array[0] = 69;
		array[1] = 420;

		/*
		String k = "$1,33,4.00";
		k = k.replaceAll("[,]","");
		k = k.replaceAll("[$]","");
		System.out.println(k);*/
		
		Integer a = new Integer(5);
		Integer b = new Integer(6);
		
		System.out.println(a.compareTo(b));




	}

}
