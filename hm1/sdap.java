/**
 * Subject: CS-561-Assignment_01
 * Author: Shengjie Hu
 * CWID: 10414302
 * Date: 10/13/2017
 * Programming Language: JAVA
 * Tool: Eclipse
 * 
 * Query 1:
 * Description: For each customer, compute the maximum and minimum sales quantities along with the corresponding products, dates 
 * (i.e., products for those maximum and minimum purchases, and the dates when those maximum and minimum sales quantities were made) and the states in which the 
 * sale transactions took place. If there are >1 occurrences of the max or min, choose one – do not display all.For each customer, also compute the average sales quantity.
 * 
 * Data structure: I used two hash map. In the first hash map, the key is the name of customer and the value is a vector which contains the information of customer.
 * In the vector, program stores max_quantity, product, day, month, year, state, min_quantity, product, day, month, year, state and sum_quantity.
 * In the second hash map, the key is the name of customer and the value is a parameter which stores the count of trading record.
 * Why do I choose hash map. Because when I want to calculate the average quantity, I need to know how many times does the customer purchase. 
 * This hash map is used to store the kinds of product. When this program read in new row, hash map checks whether customer's name is a key. If it is a key, add one time to the value of this customer. 
 * Once it is a new product, hash map store the customer's name.
 */
import java.sql.*;
import java.util.*;
public class sdap {

	public static void main(String[] args)
	{
		String usr ="postgres";
		String pwd ="123";
		String url ="jdbc:postgresql://localhost:5432/sales";
		/* 
		 * In this hash map, the key is the name of customer and the value is a vector which contains the information
		 * of customer.
		 * In the vector, program stores max_quantity, product, day, month, year, state, min_quantity, product, day, month,
		 * year, state and sum_quantity.
		 */
		HashMap<String, Vector<String>> hashMap = new HashMap<String, Vector<String>>();
		/* 
		 * In this hash map, the key is the name of customer and the value is a parameter which stores the count of trading record.
		 * Why do I choose hash map.
		 * Because when I want to calculate the average quantity, I need to know how many times does the customer
		 * purchase. This hash map is used to store the kinds of product. 
		 * When this program read in new row, hash map checks whether customer's name is a key. If it is a key, add one time to the
		 * value of this customer. Once it is a new product, hash map store the customer's name.
		*/
		HashMap<String, Integer> prodCount = new HashMap<String, Integer>();
		try
		{
			Class.forName("org.postgresql.Driver");
			System.out.println("Success loading Driver!");
		}

		catch(Exception e)
		{
			System.out.println("Fail loading Driver!");
			e.printStackTrace();
		}

		try
		{
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Success connecting server!");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");
			
			
			
			while (rs.next())
			{
				if (!hashMap.containsKey(rs.getString("cust"))) {  //Returns false if this map does not contain a mapping for the specified key.
					/* 
					 * In this section, program check whether the customer is the key of hash map.
					 * If it is already a key, just skip this section.
					 * If it is not a key, program allocate a new vector and store related informations.
					 * In the end, put <key, value>(customer's name, vector) into the hashMap.
					 */
					Vector<String> tempVec = new Vector<String>();
					tempVec.add(rs.getString("quant"));
					tempVec.add(rs.getString("prod"));
					tempVec.add(rs.getString("day"));
					tempVec.add(rs.getString("month"));
					tempVec.add(rs.getString("year"));
					tempVec.add(rs.getString("state"));
					tempVec.add(rs.getString("quant"));
					tempVec.add(rs.getString("prod"));
					tempVec.add(rs.getString("day"));
					tempVec.add(rs.getString("month"));
					tempVec.add(rs.getString("year"));
					tempVec.add(rs.getString("state"));
					int amount = Integer.parseInt(rs.getString("quant"));
					tempVec.add(String.valueOf(amount));
					hashMap.put(rs.getString("cust"), tempVec);
					/*
					 * Allocates one new hash set and add kind of products, then puts <key,value>(customer's name, hash set) into the prodCount.
					 */
					Integer i = 1;
					prodCount.put(rs.getString("cust"), i);
				} else {
					/*
					 * In this section, the customer is already a key of hashMap.
					 * First, this function calculates the sum_quantity. Finds customer's name in the hashMap, then adds quantity of this product to
					 * the sum_quantity which is store in the vector of this key.
					 */
					int amount = Integer.parseInt(rs.getString("quant")) + Integer.parseInt(hashMap.get(rs.getString("cust")).get(12));
					hashMap.get(rs.getString("cust")).set(12, String.valueOf(amount));
					/*
					 * This section is that add one to the record of this customer 
					 */
					Integer tempI = prodCount.get(rs.getString("cust")) + 1;
					prodCount.put(rs.getString("cust"), tempI);
					/*
					 * This section checks whether the quantity of current product is the max_quantity of current product or the min_quantity
					 * of current product. If it is maximum or minimum, update the vector data of current customer in the hashMap.
					 * If they are the same number, do not do anything.
					 */
					if (Integer.parseInt(hashMap.get(rs.getString("cust")).get(0)) < Integer.parseInt(rs.getString("quant"))) {
						hashMap.get(rs.getString("cust")).set(0, rs.getString("quant"));
						hashMap.get(rs.getString("cust")).set(1, rs.getString("prod"));
						hashMap.get(rs.getString("cust")).set(2, rs.getString("day"));
						hashMap.get(rs.getString("cust")).set(3, rs.getString("month"));
						hashMap.get(rs.getString("cust")).set(4, rs.getString("year"));
						hashMap.get(rs.getString("cust")).set(5, rs.getString("state"));
					} else if (Integer.parseInt(hashMap.get(rs.getString("cust")).get(6)) > Integer.parseInt(rs.getString("quant"))) {
						hashMap.get(rs.getString("cust")).set(6, rs.getString("quant"));
						hashMap.get(rs.getString("cust")).set(7, rs.getString("prod"));
						hashMap.get(rs.getString("cust")).set(8, rs.getString("day"));
						hashMap.get(rs.getString("cust")).set(9, rs.getString("month"));
						hashMap.get(rs.getString("cust")).set(10, rs.getString("year"));
						hashMap.get(rs.getString("cust")).set(11, rs.getString("state"));
					}
				}
			}
			
			
			System.out.printf("%-9s %-6s %-8s %-11s %-3s %-6s %-8s %-11s %-3s %-8s", 
					"CUSTOMER","MAX_Q","PRODUCT","DATE","ST","MIN_Q","PRODUCT","DATE", "ST", "AVG_Q");
			
			System.out.println();
			System.out.println("========  =====  =======  ==========  ==  =====  =======  ==========  ==  =====");
			Iterator iter = hashMap.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				System.out.printf("%-11s", key);
				System.out.printf("%-6s", hashMap.get(key).get(0));	//quant
				System.out.printf("%-9s", hashMap.get(key).get(1));	//prod
				String day = "", month = "";
				if (hashMap.get(key).get(2).length() < 2) {
					day = "0" + hashMap.get(key).get(2);
				} else day = hashMap.get(key).get(2);
				if (hashMap.get(key).get(3).length() < 2) {
					month = "0" + hashMap.get(key).get(3);
				} else month = hashMap.get(key).get(3);
				
				System.out.printf("%-12s", month + "/" + day + "/" + hashMap.get(key).get(4));
				System.out.printf("%-7s", hashMap.get(key).get(5)); //state
				System.out.printf("%-4s", hashMap.get(key).get(6)); //quant
				System.out.printf("%-9s", hashMap.get(key).get(7)); //prod

				if (hashMap.get(key).get(8).length() < 2) {
					day = "0" + hashMap.get(key).get(8);
				} else day = hashMap.get(key).get(8);
				if (hashMap.get(key).get(9).length() < 2) {
					month = "0" + hashMap.get(key).get(9);
				} else month = hashMap.get(key).get(9);
				
				System.out.printf("%-12s", month + "/" + day + "/" + hashMap.get(key).get(10));
				System.out.printf("%-4s", hashMap.get(key).get(11));//state
				System.out.println((Integer.parseInt(hashMap.get(key).get(12)) / prodCount.get(key))); //amount
			}
		}
		catch(SQLException e)
		{
			System.out.println("Connection URL or username or password errors!");
			e.printStackTrace();
		}

	}

}
