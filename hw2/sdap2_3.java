/**
 * Subject: CS-561-Assignment_02
 * Author: Shengjie Hu
 * CWID: 10414302
 * Date: 11/11/2017
 * Programming Language: JAVA
 * Tool: Eclipse
 * 
 * Query 3:
 * Description: For customer and product, count for each quarter, how many sales of the previous and how many sales of the following quarter had quantities
 * between that quarter’s average sale and minimum sale. Again for this query, the “YEAR” attribute is not considered.
 * 
 * Data structure: I used two hash map. HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>> hashMap = new HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>>();
 * In this hash map, the key is the name of customer and the value is a hash map which contains the information of products.
 * In the inner hash map, the key is the name of products and the value is a hash map which contains every month information of products
 * In the third hash map, the key is month and value is a vector which contains the sum of this product which bought by current customer, and how many
 * times that current customer bought this product.
 * The second hash map is HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>> hashMap2 = new HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>>();
 * In this hash map, the key is the name of customer and the value is a hash map which contains the information of products.
 * In the inner hash map, the key is the name of products and the value is a hash map which contains every month information of products
 * In the third hash map, the key is month and value is a vector which contains the number shows that every time the quantity of this product which bought by current customer.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
public class sdap2_3 {

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
			/* In this hash map, the key is the name of customer and the value is a hash map which contains the information of products.
			 * In the inner hash map, the key is the name of products and the value is a hash map which contains every month information of products
			 * In the third hash map, the key is month and value is a vector which contains the sum of this product which bought by current customer, and how many
			 * times that current customer bought this product.
			 */
			HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>> hashMap = new HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>>();
			/* The second hash map is HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>> hashMap2 = new HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>>();
			 * In this hash map, the key is the name of customer and the value is a hash map which contains the information of products.
			 * In the inner hash map, the key is the name of products and the value is a hash map which contains every month information of products
			 * In the third hash map, the key is month and value is a vector which contains the number shows that every time the quantity of this product which bought by current customer.
			 */
			HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>> hashMap2 = new HashMap<String, HashMap<String, HashMap<String, Vector<Integer>>>>();
			
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
				if (!hashMap.containsKey(rs.getString("cust"))) { // store the sum and sale times of every month
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
				
				/* 
				 * In this section, program check whether the customer is the key of hash map.
				 * If it is already a key, just update his data.
				 * If it is not a key, program allocate a new hash map and store related informations.
				 * In the end, put <key, value>(customer's name, hash map) into the hashMap.
				 */
				if (!hashMap2.containsKey(rs.getString("cust"))) { // store the every sale number of every month
					HashMap<String, HashMap<String, Vector<Integer>>> tempHash = new HashMap<String, HashMap<String, Vector<Integer>>>();
					HashMap<String, Vector<Integer>> monthHash = new HashMap<String, Vector<Integer>>();
					Vector<Integer> sumTime = new Vector<Integer>();
					sumTime.add(Integer.parseInt(rs.getString("quant")));
					monthHash.put(rs.getString("month"), sumTime);
					tempHash.put(rs.getString("prod"), monthHash);
					hashMap2.put(rs.getString("cust"), tempHash);
				} else if (hashMap2.containsKey(rs.getString("cust")) && !hashMap2.get(rs.getString("cust")).containsKey(rs.getString("prod"))) {
					/* 
					 * In this section, program check whether the product is the key of current customer's hash map.
					 * if not, allocate a new hash map and store related informations
					 * in the end, put <key, value> (product's name, hash map) into the hash map.
					 */	
					HashMap<String, Vector<Integer>> monthHash = new HashMap<String, Vector<Integer>>();
					Vector<Integer> sumTime = new Vector<Integer>();
					sumTime.add(Integer.parseInt(rs.getString("quant")));
					monthHash.put(rs.getString("month"), sumTime);
					hashMap2.get(rs.getString("cust")).put(rs.getString("prod"), monthHash);
				} else if (hashMap2.containsKey(rs.getString("cust")) && hashMap2.get(rs.getString("cust")).containsKey(rs.getString("prod")) && 
							!hashMap2.get(rs.getString("cust")).get(rs.getString("prod")).containsKey(rs.getString("month"))) {
					/* 
					 * In this section, program check whether the month of current customer and current product is the key of hash map.
					 * if not, allocate a new hash map and store related informations
					 * in the end, put <key, value> (month, vector) into the hash map. 
					 */					
					Vector<Integer> sumTime = new Vector<Integer>();
					sumTime.add(Integer.parseInt(rs.getString("quant")));
					hashMap2.get(rs.getString("cust")).get(rs.getString("prod")).put(rs.getString("month"), sumTime);
				} else {
					/* 
					 * If current customer, current product and current month have been stored into the hash map.
					 * Then update the information of current vector.  
					 */
					hashMap2.get(rs.getString("cust")).get(rs.getString("prod")).get(rs.getString("month")).add(Integer.parseInt(rs.getString("quant")));
				}
			}
			
			
			
			System.out.printf("%-8s %-8s %-8s %-11s %-9s",
					"CUSTOMER", "PRODUCT", "QUARTER", "BEFORE_TOT", "AFTER_TOT");
			System.out.println();
			System.out.println("======== =======  =======  ==========  =========");
			
			Iterator iter = hashMap.keySet().iterator();  //customer name iterator
			while (iter.hasNext()) {
				Object name = iter.next();  // customer name
				Iterator prodIt = hashMap.get(name).keySet().iterator();
				while (prodIt.hasNext()) {
					Object product = prodIt.next(); // product name
					int Q1A = 0, Q1M = Integer.MAX_VALUE, Q2A = 0, Q2M = Integer.MAX_VALUE, Q3A = 0, Q3M = Integer.MAX_VALUE, Q4A = 0, Q4M = Integer.MAX_VALUE;
					int Q1S = 0, Q2S = 0, Q3S = 0, Q4S = 0, Q1T = 0, Q2T = 0, Q3T = 0, Q4T = 0;
					
					Iterator monthIt = hashMap.get(name).get(product).keySet().iterator(); // To get average number of every month
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
					
					if (Q1T != 0) Q1A = Q1S/ Q1T;
					if (Q2T != 0) Q2A = Q2S/ Q2T;
					if (Q3T != 0) Q3A = Q3S/ Q3T;
					if (Q4T != 0) Q4A = Q4S/ Q4T;
					
					Iterator monthIt2 = hashMap2.get(name).get(product).keySet().iterator(); // To get the minimum of every month
					while (monthIt2.hasNext()) {
						Object month = monthIt2.next();
						if (Integer.parseInt(month.toString()) >= 1 && Integer.parseInt(month.toString()) <= 3) {
							for (int i=0; i< hashMap2.get(name).get(product).get(month).size(); i++) {
							    if (Q1M > hashMap2.get(name).get(product).get(month).get(i)) {
							    		Q1M = hashMap2.get(name).get(product).get(month).get(i);
							    }
							}
						} else if (Integer.parseInt(month.toString()) >= 4 && Integer.parseInt(month.toString()) <= 6) {
							for (int i=0; i< hashMap2.get(name).get(product).get(month).size(); i++) {
							    if (Q2M > hashMap2.get(name).get(product).get(month).get(i)) {
							    		Q2M = hashMap2.get(name).get(product).get(month).get(i);
							    }
							}
						} else if (Integer.parseInt(month.toString()) >= 7 && Integer.parseInt(month.toString()) <= 9) {
							for (int i=0; i< hashMap2.get(name).get(product).get(month).size(); i++) {
							    if (Q3M > hashMap2.get(name).get(product).get(month).get(i)) {
							    		Q3M = hashMap2.get(name).get(product).get(month).get(i);
							    }
							}
						} else {
							for (int i=0; i< hashMap2.get(name).get(product).get(month).size(); i++) {
							    if (Q4M > hashMap2.get(name).get(product).get(month).get(i)) {
							    		Q4M = hashMap2.get(name).get(product).get(month).get(i);
							    }
							}
						}
					}
					
					
					for (int quarter = 1; quarter <= 4; ++quarter) {
						int tempAvg = 0;
						int tempMin = 0;
						int result = 0;
						int frontQua = quarter - 1;
						int afterQua = quarter + 1;
						if (quarter == 1) {
							tempAvg = Q1A;
							tempMin = Q1M;
						} else if (quarter == 2) {
							tempAvg = Q2A;
							tempMin = Q2M;
						} else if (quarter == 3) {
							tempAvg = Q3A;
							tempMin = Q3M;
						} else if (quarter == 4) {
							tempAvg = Q4A;
							tempMin = Q4M;
						}
						System.out.printf("%-8s %-9s", name, product);
						System.out.print("Q");
						System.out.printf("%-8s", quarter);
						if (quarter == 1) {
							System.out.printf("%10s", "NULL");
						}
						else {
							Iterator iterT1 = hashMap2.keySet().iterator();  //customer name iterator
							while (iterT1.hasNext()) {
								Object nameT = iterT1.next();  // customer name
								if (nameT.equals(name)) {
									if (hashMap2.get(nameT).containsKey(product)) {
										Iterator monthItT = hashMap2.get(nameT).get(product).keySet().iterator();
										while (monthItT.hasNext()) {
											Object month = monthItT.next();
											if (Integer.parseInt(month.toString()) == quarter * 3 || Integer.parseInt(month.toString()) == (quarter * 3  - 1)|| 
													Integer.parseInt(month.toString()) == (quarter * 3 - 2)) {
												continue;
											} else if (Integer.parseInt(month.toString()) == frontQua * 3 || Integer.parseInt(month.toString()) == (frontQua * 3  - 1)|| 
													Integer.parseInt(month.toString()) == (frontQua * 3 - 2)){
												for (int i=0; i< hashMap2.get(nameT).get(product).get(month).size(); i++) {
												    if (hashMap2.get(nameT).get(product).get(month).get(i) >= tempMin && hashMap2.get(nameT).get(product).get(month).get(i) <= tempAvg) {
												    		result++;
												    }
												}
											} else continue;
										}
									}
								} else {
									continue;
								}
							}
							System.out.printf("%10s", result);
						}
						
						result = 0;
						if (afterQua == 5) {
							System.out.printf("%11s", "NULL");
							System.out.println();
						}
						else {
							Iterator iterT = hashMap2.keySet().iterator();  //customer name iterator
							while (iterT.hasNext()) {
								Object nameT = iterT.next();  // customer name
								
								if (nameT.equals(name)) {
//									System.out.println(nameT + " " + name);
									if (hashMap2.get(nameT).containsKey(product)) {
										Iterator monthItT = hashMap2.get(nameT).get(product).keySet().iterator();
										while (monthItT.hasNext()) {
											Object month = monthItT.next();
											if (Integer.parseInt(month.toString()) == quarter * 3 || Integer.parseInt(month.toString()) == (quarter * 3  - 1)|| 
													Integer.parseInt(month.toString()) == (quarter * 3 - 2)) {
												continue;
											} else if (Integer.parseInt(month.toString()) == afterQua * 3 || Integer.parseInt(month.toString()) == (afterQua * 3  - 1)|| 
													Integer.parseInt(month.toString()) == (afterQua * 3 - 2)){
												for (int i=0; i< hashMap2.get(nameT).get(product).get(month).size(); i++) {
												    if (hashMap2.get(nameT).get(product).get(month).get(i) >= tempMin && hashMap2.get(nameT).get(product).get(month).get(i) <= tempAvg) {
//												    		System.out.println(nameT+" " + product + " " + month + " " + hashMap2.get(nameT).get(product).get(month).get(i));
												    		result++;
												    }
												}
											}
										}
									}
								}
							}
							System.out.printf("%11s", result);
							System.out.println();
							result = 0;
						}
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
