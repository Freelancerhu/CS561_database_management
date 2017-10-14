/**
 * Subject: CS-561-Assignment_01
 * Author: Shengjie Hu
 * CWID: 10414302
 * Date: 10/13/2017
 * Programming Language: JAVA
 * Tool: Eclipse
 * 
 * Query 2:
 * Description: For each combination of customer and product, output the maximum sales quantities for January (regardless of the year, that is, both 1/11/2000 and 1/23/2008 
 * are considered sales transactions for January) and minimum sales quantities for February and March (again, regardless of the year) in 3 separate columns. 
 * Like the first report, display the corresponding dates (i.e., dates of those maximum and minimum sales quantities). Furthermore, for January (MAX), 
 * include only the sales that occurred between 2000 and 2005; for February (MIN) and March (MIN), include all sales.
 * 
 * Data structure: I used one hash map. HashMap<String, HashMap<String, Vector<String>>> hashMap = new HashMap<String, HashMap<String, Vector<String>>>();
 * In this hash map, the key is the name of customer and the value is a hash map which contains the information of products.
 * In the inner hash map, the key is the name of products and the value is a vector which contains "JAN_MAX","DATE","FEB_MIN","DATE","MAR_MIN","DATE"
 */

import java.sql.*;
import java.util.*;
import java.lang.Integer;
public class sdap2 {

	public static void main(String[] args)
	{
		String usr ="postgres";
		String pwd ="123";
		String url ="jdbc:postgresql://localhost:5432/sales";
		/*
		 * In this hash map, the key is the name of customer and the value is a hash map which contains the information
		 * of products.
		 * In the inner hash map, the key is the name of products and the value is a vector which contains "JAN_MAX","DATE","FEB_MIN","DATE","MAR_MIN","DATE"
		 */
		HashMap<String, HashMap<String, Vector<String>>> hashMap = new HashMap<String, HashMap<String, Vector<String>>>();
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
				if (!hashMap.containsKey(rs.getString("cust"))) {//Returns false if this map does not contain a mapping for the specified key.
					/* 
					 * In this section, program check whether the customer is the key of hash map.
					 * If it is already a key, just skip this section.
					 * If it is not a key, program allocate a new hash map and store related informations.
					 * In the end, put <key, value>(customer's name, hash map) into the hashMap.
					 */
					HashMap<String, Vector<String>> tempHashMap = new HashMap<String, Vector<String>>();
					Vector<String> tempVec = new Vector<String>();//store "JAN_MAX","DATE","FEB_MIN","DATE","MAR_MIN","DATE"
					if (Integer.parseInt(rs.getString("month")) == 1 && Integer.parseInt(rs.getString("year")) >= 2000 && Integer.parseInt(rs.getString("year")) <= 2005) {
						tempVec.add(rs.getString("quant"));
						tempVec.add(rs.getString("day"));
						tempVec.add(rs.getString("month"));
						tempVec.add(rs.getString("year"));
						
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
	
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempHashMap.put(rs.getString("prod"), tempVec);
						hashMap.put(rs.getString("cust"), tempHashMap);
					} else if (Integer.parseInt(rs.getString("month")) == 2) {
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						
						tempVec.add(rs.getString("quant"));
						tempVec.add(rs.getString("day"));
						tempVec.add(rs.getString("month"));
						tempVec.add(rs.getString("year"));
	
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempHashMap.put(rs.getString("prod"), tempVec);
						hashMap.put(rs.getString("cust"), tempHashMap);
					} else if (Integer.parseInt(rs.getString("month")) == 3) {
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
	
						tempVec.add(rs.getString("quant"));
						tempVec.add(rs.getString("day"));
						tempVec.add(rs.getString("month"));
						tempVec.add(rs.getString("year"));
						tempHashMap.put(rs.getString("prod"), tempVec);
						hashMap.put(rs.getString("cust"), tempHashMap);
					}
				} else if  (hashMap.containsKey(rs.getString("cust")) && !hashMap.get(rs.getString("cust")).containsKey(rs.getString("prod"))) {
					Vector<String> tempVec = new Vector<String>();
					if(Integer.parseInt(rs.getString("month")) != 1 && Integer.parseInt(rs.getString("month")) != 2 && Integer.parseInt(rs.getString("month")) != 3) {
						continue;
					}
					if (Integer.parseInt(rs.getString("month")) == 1 && Integer.parseInt(rs.getString("year")) >= 2000 && Integer.parseInt(rs.getString("year")) <= 2005) {
						tempVec.add(rs.getString("quant"));
						tempVec.add(rs.getString("day"));
						tempVec.add(rs.getString("month"));
						tempVec.add(rs.getString("year"));
						
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
	
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
					} else if (Integer.parseInt(rs.getString("month")) == 2) {
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						
						tempVec.add(rs.getString("quant"));
						tempVec.add(rs.getString("day"));
						tempVec.add(rs.getString("month"));
						tempVec.add(rs.getString("year"));
	
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
					} else if (Integer.parseInt(rs.getString("month")) == 3) {
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
						tempVec.add("NULL");
	
						tempVec.add(rs.getString("quant"));
						tempVec.add(rs.getString("day"));
						tempVec.add(rs.getString("month"));
						tempVec.add(rs.getString("year"));
					}
					if (!tempVec.isEmpty())
						hashMap.get(rs.getString("cust")).put(rs.getString("prod"), tempVec);
				} else {
					/*
					 * In this section, the customer is already a key of hashMap.
					 * First, checks the month of product, if its month is 1 and its year between 2000 and 2005, check whether its quantity is bigger than value which is
					 * stored in the vector.
					 * Then, if its month is 2, check whether its quantity is smaller than value which is stored in the vector.
					 * Then, if its month is 3, check whether its quantity is smaller than value which is stored in the vector.
					 */
					if (Integer.parseInt(rs.getString("month")) == 1 && Integer.parseInt(rs.getString("year")) <= 2005 
															&& Integer.parseInt(rs.getString("year")) >= 2000) {
						if (hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(0) == "NULL" ||
								Integer.parseInt(rs.getString("quant")) > Integer.parseInt(hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(0))) {
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(0, rs.getString("quant"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(1, rs.getString("day"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(2, rs.getString("month"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(3, rs.getString("year"));
						}
					} else if (Integer.parseInt(rs.getString("month")) == 2) {
						if (hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(4) == "NULL" || 
								Integer.parseInt(rs.getString("quant")) < Integer.parseInt(hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(4))) {
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(4, rs.getString("quant"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(5, rs.getString("day"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(6, rs.getString("month"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(7, rs.getString("year"));
						}
					} else if (Integer.parseInt(rs.getString("month")) == 3 ) {
						if (hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(8) == "NULL" || 
								Integer.parseInt(rs.getString("quant")) < Integer.parseInt(hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(8))) {
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(8, rs.getString("quant"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(9, rs.getString("day"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(10, rs.getString("month"));
							hashMap.get(rs.getString("cust")).get(rs.getString("prod")).set(11, rs.getString("year"));
						}
					}
				}
			}
			
			
			
			
			
			System.out.printf("%-9s %-8s %-8s %-11s %-8s %-11s %-8s %-12s", 
					"CUSTOMER","PRODUST","JAN_MAX","DATE","FEB_MIN","DATE","MAR_MIN","DATE");
			
			System.out.println();
			System.out.println("========  =======  =======  ==========  =======  ==========  =======  ==========");
			Iterator iter = hashMap.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				// HashMap<String, HashMap<String, Vector<String>>> hashMap = new HashMap<String, HashMap<String, Vector<String>>>();
				Iterator it = hashMap.get(key).keySet().iterator();
				while (it.hasNext()) {
					System.out.printf("%-10s", key);
					Object midKey = it.next();
					System.out.printf("%-9s", midKey);
					System.out.printf("%7s", hashMap.get(key).get(midKey).get(0));	//quant
					System.out.print("  ");
					String day = "", month = "";
					if (hashMap.get(key).get(midKey).get(1) == "NULL" ) {
						System.out.printf("%10s", "NULL");
					} else {
						if (hashMap.get(key).get(midKey).get(1).length() < 2) {
							day = "0" + hashMap.get(key).get(midKey).get(1);
						} else day = hashMap.get(key).get(midKey).get(1);
						if (hashMap.get(key).get(midKey).get(2).length() < 2) {
							month = "0" + hashMap.get(key).get(midKey).get(2);
						} else month = hashMap.get(key).get(midKey).get(2);
						System.out.printf("%10s", month + "/" + day + "/" + hashMap.get(key).get(midKey).get(3));
					}

					System.out.printf("%9s", hashMap.get(key).get(midKey).get(4));	//quant
					if (hashMap.get(key).get(midKey).get(5) == "NULL" ) {
						System.out.printf("%12s", "NULL");
					} else {
						if (hashMap.get(key).get(midKey).get(5).length() < 2) {
							day = "0" + hashMap.get(key).get(midKey).get(5);
						} else day = hashMap.get(key).get(midKey).get(5);
						if (hashMap.get(key).get(midKey).get(6).length() < 2) {
							month = "0" + hashMap.get(key).get(midKey).get(6);
						} else month = hashMap.get(key).get(midKey).get(6);
						System.out.printf("%12s", month + "/" + day + "/" + hashMap.get(key).get(midKey).get(7));
					}
					
					System.out.printf("%9s", hashMap.get(key).get(midKey).get(8));	//quant
					if (hashMap.get(key).get(midKey).get(9) == "NULL" ) {
						System.out.printf("%13s", "NULL");
					} else {
						if (hashMap.get(key).get(midKey).get(9).length() < 2) {
							day = "0" + hashMap.get(key).get(midKey).get(9);
						} else day = hashMap.get(key).get(midKey).get(9);
						if (hashMap.get(key).get(midKey).get(10).length() < 2) {
							month = "0" + hashMap.get(key).get(midKey).get(10);
						} else month = hashMap.get(key).get(midKey).get(10);
						System.out.printf("%13s", month + "/" + day + "/" + hashMap.get(key).get(midKey).get(11));
					}
					System.out.println();
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
