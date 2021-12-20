package it.generation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	
	private final static String DB_URL = "jdbc:mysql://localhost:3306/dump_nations";
	private final static String DB_USER = "root";
	private final static String DB_PASSWORD = "Thewitchern1!";
	private final static String DB_QUERY = "select\r\n"
			+ "	c.name,\r\n"
			+ "	c.country_id,\r\n"
			+ "	r.name as region,\r\n"
			+ "	c2.name as continent\r\n"
			+ "from\r\n"
			+ "	countries c\r\n"
			+ "join regions r\r\n"
			+ "on\r\n"
			+ "	r.region_id = c.region_id\r\n"
			+ "join continents c2\r\n"
			+ "on\r\n"
			+ "	c2.continent_id = r.continent_id\r\n"
			+ "group by\r\n"
			+ "	c.name\r\n"
			+ "order by\r\n"
			+ "	c.name;";
	private final static String DB_QUERY_RICERCA ="select c.name, c.country_id, r.name as region, c2.name as continent \r\n"
			+ "from countries c\r\n"
			+ "join regions r\r\n"
			+ "ON r.region_id = c.region_id\r\n"
			+ "join continents c2\r\n"
			+ "on c2.continent_id = r.continent_id\r\n"
			+ "where c.name like "
			+ "?\r\n"
			+ "group by c.name\r\n"
			+ "order by c.name;";
	
	// Bonus
	
	private final static String DB_QUERY_PAESE = "select c.name\r\n"
			+ "from countries c \r\n"
			+ "where c.country_id = ?;";
	private final static String DB_QUERY_LINGUA = "select l.`language` \r\n"
			+ "from country_languages cl \r\n"
			+ "join languages l \r\n"
			+ "on l.language_id = cl.language_id\r\n"
			+ "where country_id = ?;";
	private final static String DB_QUERY_STATS = "select max(cs.`year`), cs.population, cs.gdp \r\n"
			+ "from country_stats cs \r\n"
			+ "where country_id = ?;";

	public static void main(String[] args) throws SQLException {
		
		Scanner scanner = new Scanner(System.in);
		
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(DB_QUERY);
			while (result.next()) {
				
				System.out.println("Country name: " + result.getString("c.name"));
				System.out.println("ID n. " + result.getInt("c.country_id"));
				System.out.println("Region name: " + result.getString("region") + ". ");
				System.out.println("Continent name: " + result.getString("continent") + ". \n");
				
			}
			
			System.out.println("Cerca: ");
			String ricerca = scanner.nextLine();
			String ricercaFiltro = "%" + ricerca + "%";
			
			//% tra doppi apici prima e dopo, perchè dbeaver mette già gli apici da solo 
			
			try (PreparedStatement ps = connection.prepareStatement(DB_QUERY_RICERCA)){
				
				ps.setString(1, ricercaFiltro);
				ResultSet resultRicerca = ps.executeQuery();
				
				while (resultRicerca.next()) {

					System.out.println("Country name: " + resultRicerca.getString("c.name"));
					System.out.println("ID n. " + resultRicerca.getInt("c.country_id"));
					System.out.println("Region name: " + resultRicerca.getString("region") + ". ");
					System.out.println("Continent name: " + resultRicerca.getString("continent") + ". \n");

				}  
					
			}
			
			// Bonus
			
			System.out.println("Inserisci ID di un paese: ");
			int id = scanner.nextInt();
			
			try (PreparedStatement psp = connection.prepareStatement(DB_QUERY_PAESE)) {
				
				psp.setInt(1, id);
				ResultSet resultPaese = psp.executeQuery();
				
				while (resultPaese.next()) {
					System.out.println("Country name: " + resultPaese.getString("c.name"));
				}
				
			}
			
			try (PreparedStatement psl = connection.prepareStatement(DB_QUERY_LINGUA)) {
							
							psl.setInt(1, id);
							ResultSet resultLingua = psl.executeQuery();
							
							System.out.println("Country languages: ");
							while (resultLingua.next()) {
								System.out.print(resultLingua.getString("l.language") + " ");
				}
							
			}
			
			try (PreparedStatement psSt = connection.prepareStatement(DB_QUERY_STATS)) {
				
				psSt.setInt(1, id);
				ResultSet resultStats = psSt.executeQuery();
				
				System.out.println("\nMost recent stats:");
				
				while (resultStats.next()) {

					System.out.println("Year: " + resultStats.getString("max(cs.`year`)"));
					System.out.println("Population: " + resultStats.getInt("cs.population"));
					System.out.println("GDP: " + resultStats.getBigDecimal("cs.gdp"));
					

				}  
				
				
			}
			
			
	} catch (SQLException e) {

		e.printStackTrace();

		}
	scanner.close();
	}
}



