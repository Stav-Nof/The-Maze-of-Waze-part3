package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class score {
	public static int numberOfGames(int id){
		int counter = 0;
		try {
			String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
			String jdbcUser="student";
			String jdbcUserPassword="OOP2020student";
			String allCustomersQuery = "SELECT * FROM Logs where userID="+ id +";";
			ResultSet resultSet = null;
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(allCustomersQuery);
			while (resultSet.next()) {
				counter++;
			}
			resultSet.close();
			statement.close();		
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return counter;
	}


	public static int[] CurrentStage(int id) {
		int []Stages = new int[12];
		try {
			String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
			String jdbcUser="student";
			String jdbcUserPassword="OOP2020student";
			String allCustomersQuery = "SELECT * FROM Logs where userID="+ id + ";";
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				int level = resultSet.getInt("levelID");
				int grade = resultSet.getInt("score");
				int moves = resultSet.getInt("moves");
				if (level == 0) {
					if(grade >= 125) {
						if(moves <= 290) {
							if (grade > Stages[0])Stages[0] = grade;
						}
					}
				}
				if (level == 1) {
					if(grade >= 436) {
						if(moves <= 580){
							if (grade > Stages[1])Stages[1] = grade;
						}
					}
				}
				if (level == 3) {
					if(grade >= 713) {
						if(moves <= 580) {
							if (grade > Stages[2])Stages[2] = grade;
						}
					}
				}
				if (level == 5) {
					if(grade >= 570) {
						if(moves <= 500) {
							if (grade > Stages[3])Stages[3] = grade;
						}
					}
				}
				if (level == 9) {
					if(grade >= 480) {
						if(moves <= 580) {
							if (grade > Stages[4])Stages[4] = grade;
						}
					}
				}
				if (level == 11) {
					if(grade >= 1050) {
						if(moves <= 580) {
							if (grade > Stages[5])Stages[5] = grade;
						}
					}
				}
				if (level == 13) {
					if(grade >= 310) {
						if(moves <= 580) {
							if (grade > Stages[6])Stages[6] = grade;
						}
					}
				}
				if (level == 16) {
					if(grade >= 235) {
						if(moves <= 290) {
							if (grade > Stages[7])Stages[7] = grade;
						}
					}
				}
				if (level == 19) {
					if(grade >= 250) {
						if(moves <= 580) {
							if (grade > Stages[8])Stages[8] = grade;
						}
					}
				}
				if (level == 20) {
					if(grade >= 200) {
						if(moves <= 290) {
							if (grade > Stages[9])Stages[9] = grade;
						}
					}
				}
				if (level == 23) {
					if(grade >= 1000) {
						if(moves <= 1140) {
							if (grade > Stages[10])Stages[10] = grade;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Stages;
	}


	public static int global(int id, int level, int yourScore) {
		int ans = 1;
		int moves = 0, Grade=0;

		if (level == 0) {
			moves = 290;
			Grade = 125;
		}
		if (level == 1) {
			moves = 580;
			Grade = 436;
		}
		if (level == 3) {
			moves = 580;
			Grade = 713;
		}
		if (level == 5) {
			moves = 500;
			Grade = 570;
		}
		if (level == 9) {
			moves = 580;
			Grade = 480;
		}
		if (level == 11) {
			moves = 580;
			Grade = 1050;
		}
		if (level == 13) {
			moves = 580;
			Grade = 310;
		}
		if (level == 16) {
			moves = 290;
			Grade = 235;
		}
		if (level == 19) {
			moves = 580;
			Grade = 250;
		}
		if (level == 20) {
			moves = 290;
			Grade = 200;
		}
		if (level == 23) {
			moves = 1140;
			Grade = 1000;
		}
		try {
			String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
			String jdbcUser="student";
			String jdbcUserPassword="OOP2020student";
			String allCustomersQuery = "SELECT * FROM Logs where levelID="+ level + ";";
			Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next()) {
				int globalscore = resultSet.getInt("score");
				int globalmoves = resultSet.getInt("moves");
				if (globalmoves <= moves && globalscore >= Grade) {
					if(globalscore > yourScore) ans++;
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return ans;
	}
}
