/**
 * Subject: CS-561-Assignment_02
 * Author: Shengjie Hu
 * CWID: 10414302
 * Date: 11/11/2017
 * Programming Language: JAVA
 * Tool: Eclipse
 * 
 * Query 1:
 * Description: For each customer and product, compute (1) the customer's average sale of this product, (2) the customer’s average sale 
 * of the other products (3) the average sale of the product for the other customers and.
 * 
 * Data structure: I used one hash map. HashMap<String, HashMap<String, Vector<Interger>>> hashMap = new HashMap<String, HashMap<String, Vector<String>>>();
 * In this hash map, the key is the name of customer and the value is a hash map which contains the information of products.
 * In the inner hash map, the key is the name of products and the value is a vector which contains the sum of this product which bought by current customer, and how many
 * times that current customer bought this product.
 */

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
public class sdap2_1 {

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
			 * In this hash map, the key is the name of customer and the value is a hash map which contains the information
			 * of products.
			 * In the inner hash map, the key is the name of products and the value is a vector which contains the sum of this product which bought by current customer, and how many
			 * times that current customer bought this product.
			 */
			HashMap<String, HashMap<String, Vector<Integer>>> hashMap = new HashMap<String, HashMap<String, Vector<Integer>>>();
			
			Connection conn = DriverManager.getConnection(url, usr, pwd);
			System.out.println("Success connecting server!");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Sales");

			while (rs.next())
			{
				//rs.getString("cust");
				/* 
				 * In this section, program check whether the customer is the key of hash map.
				 * If it is already a key, just update his data.
				 * If it is not a key, program allocate a new hash map and store related informations.
				 * In the end, put <key, value>(customer's name, hash map) into the hashMap.
				 */
				if (!hashMap.containsKey(rs.getString("cust"))) {
					HashMap<String, Vector<Integer>> tempHash = new HashMap<String, Vector<Integer>>();
					Vector<Integer> tempVec = new Vector<Integer>();
					tempVec.add(Integer.parseInt(rs.getString("quant")));
					tempVec.add(1);
					tempHash.put(rs.getString("prod"), tempVec);
					hashMap.put(rs.getString("cust"), tempHash);
				} else if (hashMap.containsKey(rs.getString("cust")) && !hashMap.get(rs.getString("cust")).containsKey(rs.getString("prod"))) {
					// Customer is the key, but this product is not in the hash map of this customer.
					/*
					 * In this section, the customer is already a key of hashMap.
					 * First, checks the product, if this product is not contained in the hash map, allocate a new vector which store the quantity and time,
					 * then put this product into current custmoer's hash map.
					 */
					Vector<Integer> tempVec = new Vector<Integer>();
					tempVec.add(Integer.parseInt(rs.getString("quant")));
					tempVec.add(1);
					hashMap.get(rs.getString("cust")).put(rs.getString("prod"), tempVec);
				} else {
					/*
					 * In this section, the customer is already a key of hashMap.
					 * First, checks the product, if this product is contained in the hash map, update its data.
					 */
					int tempSum = hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(0) + Integer.parseInt(rs.getString("quant"));
					int tempTime = hashMap.get(rs.getString("cust")).get(rs.getString("prod")).get(1) + 1;
					hashMap.get(rs.getString("cust")).get(rs.getString("prod")).setElementAt(tempSum, 0);
					hashMap.get(rs.getString("cust")).get(rs.getString("prod")).setElementAt(tempTime, 1);
				}
			}
			
			
			System.out.printf("%-8s %-8s %-8s %-15s %-14s",
					"CUSTOMER", "PRODUCT", "THE_AVG", "OTHER_PROD_AVG", "OTHER_CUST_AVG");
			System.out.println();
			System.out.println("======== =======  =======  ==============  ==============");
			Iterator iter = hashMap.keySet().iterator(); //customer name
			while (iter.hasNext()) {
				Object name = iter.next();
				Iterator it = hashMap.get(name).keySet().iterator();
				while (it.hasNext()) {
					Object product = it.next();
					System.out.printf("%-9s", name);
					System.out.printf("%-8s", product);
					int avgSum = hashMap.get(name).get(product).get(0) / hashMap.get(name).get(product).get(1); // calculate the average of this product
					System.out.printf("%8s", avgSum);
					
					//calculate the average of this customer's other products 
					Iterator tempProIt = hashMap.get(name).keySet().iterator();
					int otherSum = 0;
					int otherTime = 0;
					while (tempProIt.hasNext()) {
						Object tempPro= tempProIt.next();
						if (tempPro.equals(product)) {
							continue;
						}
						else {
							otherSum += hashMap.get(name).get(tempPro).get(0);
							otherTime += hashMap.get(name).get(tempPro).get(1);
						}
					}
					System.out.printf("%16s", otherSum / otherTime);
					
					
					//calculate the average of other customers who'd buy this product 
					otherSum = 0;
					otherTime = 0;
					Iterator tempCustIt = hashMap.keySet().iterator(); //customer name
					while (tempCustIt.hasNext()) {
						Object tempCustName = tempCustIt.next();
						if (tempCustName.equals(name)) {
							continue;
						}
						else {
							Iterator tempProIt2 = hashMap.get(tempCustName).keySet().iterator();
							while (tempProIt2.hasNext()) {
								Object tempPro2 = tempProIt2.next();
								if (!tempPro2.equals(product)) {
									continue;
								} else {
									otherSum += hashMap.get(tempCustName).get(tempPro2).get(0);
									otherTime += hashMap.get(tempCustName).get(tempPro2).get(1);
								}
							}
						}
					}
					
					if (otherTime == 0) {
						System.out.println(" " + "NULL");
					} else {
						System.out.printf("%16s", otherSum / otherTime);
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
