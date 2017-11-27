/**
 * Subject: CS-561-Assignment_02
 * Author: Shengjie Hu
 * CWID: 10414302
 * Date: 11/11/2017
 * Programming Language: JAVA
 * Tool: Eclipse
 * 
 * Query 2:
 * Description: For customer and product, show the average sales before and after each quarter (e.g., for Q2, show average sales of Q1 and Q3. 
 * For “before” Q1 and “after” Q4, display <NULL>). The “YEAR” attribute is not considered for this query – for example, both Q1 of 2007 and Q1
 *  of 2008 are considered Q1 regardless of the year.
 * 
 * Data structure: I used one hash map. HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>> hashMap = new HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>>();
 * In this hash map, the key is the name of customer and the value is a hash map which contains the information of products.
 * In the inner hash map, the key is the name of products and the value is a hash map which contains every month information of products
 * In the third hash map, the key is month and value is a vector which contains the sum of this product which bought by current customer, and how many
 * times that current customer bought this product.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
public class sdap2_2 {

	public static void main(String[] args)
	{
		String usr ="postgres";
		String pwd ="123";
		String url ="jdbc:postgresql://localhost:5432/sales";

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
			/* 
			 * In this hash map, the key is the name of customer and the value is a hash map which contains the information of products.
			 * In the inner hash map, the key is the name of products and the value is a hash map which contains every month information of products
			 * In the third hash map, the key is month and value is a vector which contains the sum of this product which bought by current customer, and how many
			 * times that current customer bought this product.
			 */
			HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>> hashMap = new HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>>();
			
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Success connecting server!");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");

			while (rs.next())
			{
				/* 
				 * In this section, program check whether the customer is the key of hash map.
				 * If it is already a key, just update his data.
				 * If it is not a key, program allocate a new hash map and store related informations.
				 * In the end, put <key, value>(customer's name, hash map) into the hashMap.
				 */
				if (!hashMap.containsKey(rs.getString("cust"))) {
					HashMap<String, HashMap<String, Vector<Integer>>> tempHash = new HashMap<String, HashMap<String, Vector<Integer>>>();
					HashMap<String, Vector<Integer>> monthHash = new HashMap<String, Vector<Integer>>();
					Vector<Integer> sumTime = new Vector<Integer>();
					sumTime.add(Integer.parseInt(rs.getString("quant")));
					sumTime.add(1);
					monthHash.put(rs.getString("month"), sumTime);
					tempHash.put(rs.getString("prod"), monthHash);
					hashMap.put(rs.getString("cust"), tempHash);
				} else if (hashMap.containsKey(rs.getString("cust")) && !hashMap.get(rs.getString("cust")).containsKey(rs.getString("prod"))) {
					/* 
					 * In this section, program check whether the product is the key of current customer's hash map.
					 * if not, allocate a new hash map and store related informations
					 * in the end, put <key, value> (product's name, hash map) into the hash map.
					 */	 
					HashMap<String, Vector<Integer>> monthHash = new HashMap<String, Vector<Integer>>();
					Vector<Integer> sumTime = new Vector<Integer>();
					sumTime.add(Integer.parseInt(rs.getString("quant")));
					sumTime.add(1);
					monthHash.put(rs.getString("month"), sumTime);
					hashMap.get(rs.getString("cust")).put(rs.getString("prod"), monthHash);
				} else if (hashMap.containsKey(rs.getString("cust")) && hashMap.get(rs.getString("cust")).containsKey(rs.getString("prod")) && 
							!hashMap.get(rs.getString("cust")).get(rs.getString("prod")).containsKey(rs.getString("month"))) {
					/* 
					 * In this section, program check whether the month of current customer and current product is the key of hash map.
					 * if not, allocate a new hash map and store related informations
					 * in the end, put <key, value> (month, vector) into the hash map. 
					 */
					Vector<Integer> sumTime = new Vector<Integer>();
					sumTime.add(Integer.parseInt(rs.getString("quant")));
					sumTime.add(1);
					hashMap.get(rs.getString("cust")).get(rs.getString("prod")).put(rs.getString("month"), sumTime);
				} else {
					/* 
					 * If current customer, current product and current month have been stored into the hash map.
					 * Then update the information of current vector.  
					 */
					int tempSum = hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(rs.getString("month")).get(0) + Integer.parseInt(rs.getString("quant"));
					int tempTime = hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(rs.getString("month")).get(1) + 1;
					hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(rs.getString("month")).setElementAt(tempSum, 0);
					hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(rs.getString("month")).setElementAt(tempTime, 1);
				}
			}
			
			
			System.out.printf("%-8s %-8s %-8s %-11s %-9s",
					"CUSTOMER", "PRODUCT", "QUARTER", "BEFORE_AVG", "AFTER_AVG");
			System.out.println();
			System.out.println("======== =======  =======  ==========  =========");
			
			Iterator iter = hashMap.keySet().iterator();  //customer name iterator
			while (iter.hasNext()) {
				Object name = iter.next();  // customer name
				Iterator prodIt = hashMap.get(name).keySet().iterator();
				while (prodIt.hasNext()) {
					Object product = prodIt.next();
					int Q1S = 0, Q2S = 0, Q3S = 0, Q4S = 0;
					int Q1T = 0, Q2T = 0, Q3T = 0, Q4T = 0;
					Iterator monthIt = hashMap.get(name).get(product).keySet().iterator();
					while (monthIt.hasNext()) {
						Object month = monthIt.next();
						if (Integer.parseInt(month.toString()) >= 1 && Integer.parseInt(month.toString()) <= 3) {
							Q1S += hashMap.get(name).get(product).get(month).get(0);
							Q1T += hashMap.get(name).get(product).get(month).get(1);
						} else if (Integer.parseInt(month.toString()) >= 4 && Integer.parseInt(month.toString()) <= 6) {
							Q2S += hashMap.get(name).get(product).get(month).get(0);
							Q2T += hashMap.get(name).get(product).get(month).get(1);
						} else if (Integer.parseInt(month.toString()) >= 7 && Integer.parseInt(month.toString()) <= 9) {
							Q3S += hashMap.get(name).get(product).get(month).get(0);
							Q3T += hashMap.get(name).get(product).get(month).get(1);
						} else {
							Q4S += hashMap.get(name).get(product).get(month).get(0);
							Q4T += hashMap.get(name).get(product).get(month).get(1);
						}
					}
					if (Q2T != 0) {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q1", "<NULL>", Q2S/Q2T);
						System.out.println();
					} else {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q1", "<NULL>", "<NULL>");
						System.out.println();
					}
					if (Q1T != 0 && Q3T != 0) {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q2", Q1S/Q1T, Q3S/Q3T);
						System.out.println();
					} else if (Q3T != 0) {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q2", "<NULL>", Q3S/Q3T);
						System.out.println();
					} else {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q2", Q1S/Q1T, "<NULL>");
						System.out.println();
					}
					if (Q2T != 0 && Q4T != 0) {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q3", Q2S/Q2T, Q4S/Q4T);
						System.out.println();
					} else if (Q4T != 0) {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q3", "<NULL>", Q4S/Q4T);
						System.out.println();
					} else {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q3", Q2S/Q2T, "<NULL>");
						System.out.println();
					}
					if (Q3T != 0) {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q4", Q3S/Q3T, "<NULL>");
						System.out.println();
					} else {
						System.out.printf("%-8s %-8s %-8s %11s %9s", name, product, "Q4", "<NULL>","<NULL>");
						System.out.println();
					}
				}
			}
		}

		
		catch(SQLException e)
		{
			System.out.println("Connection URL or username or password errors!");
			e.printStackTrace();
		}

	}

}
