import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnector{
	private Connection conn;
	private PreparedStatement pstmt;
	private ArrayList<String> params;
	private String url = "jdbc:mysql://localhost/capstone";
	private String userId = "root";
	private String userPass = "root";
	private String userInfoStr;
	
	private final int LOGIN = 1;
	private final int DUPCHECK= 2;
	private final int SIGNUP = 3;
	private final int SAVEDATA = 4;
	private final int LOADDATA = 5;
	
	public DatabaseConnector(ArrayList<String> p){
		params = p;
		init();
	}
	
	public String getUserInfoStr() { return userInfoStr; }
	private void init(){
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){
			System.out.println("driver connection error");
		}catch(Exception etc){
			System.out.println(etc.getMessage());
		}		
	}
	private boolean login() throws SQLException{
		System.out.println("LOGIN STEP");
		boolean ret = false;
		String query = "SELECT * FROM member WHERE ID=? AND PW=?";
		System.out.println(params.get(1) + " " + params.get(2));
		pstmt = conn.prepareStatement(query);
				
		pstmt.setString(1, params.get(1));
		pstmt.setString(2, params.get(2));
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			String id = rs.getString("ID");
			String pw = rs.getString("PW");
			ret = true;
		}

		System.out.println("LOGIN COMPLETE");
		return ret;
	}
	
	private boolean dupCheck() throws SQLException{
		System.out.println("DUPCHECK STEP");
		boolean ret = true;
		String query = "SELECT * FROM member WHERE ID=?";
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1, params.get(1));
		
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) ret = false;
		
		System.out.println("DUPCHECK COMPLETE");
		return ret;
	}
	
	private boolean signUp() throws SQLException{
		System.out.println("SIGNUP STEP");
		boolean ret = true;
		String query = "INSERT INTO member VALUES(?,?,?)";
		pstmt = conn.prepareStatement(query);
		pstmt.setString(1,  params.get(1));
		pstmt.setString(2,  params.get(2));
		pstmt.setString(3,  "00000000000000000000");
		
		pstmt.executeUpdate();
		
		System.out.println("SIGNUP COMPLETE");
		return ret;
	}
	private boolean saveData() throws SQLException{
		boolean ret = true;
		String query = "UPDATE member SET INFO=? WHERE ID=?";
		pstmt = conn.prepareStatement(query);
		
		pstmt.setString(1, params.get(2));
		pstmt.setString(2, params.get(1));
		
		pstmt.executeUpdate();
		
		return ret;
	}
	private boolean loadData() throws SQLException{
		boolean ret = false;
		String query = "SELECT INFO FROM member WHERE ID=?";
		pstmt = conn.prepareStatement(query);
		
		pstmt.setString(1, params.get(1));
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()){
			userInfoStr = rs.getString("INFO");
			ret = true;
		}
		
		return ret;
	}
	
	public boolean run(){
		conn = null;
		boolean ret = false;
		try
		{
			conn = DriverManager.getConnection(url, userId, userPass);
			int opt = Integer.parseInt(params.get(0));
			System.out.println(opt);
			
			switch(opt){
			case LOGIN:
				ret = login();
				break;
			case DUPCHECK:
				ret = dupCheck();
				break;
			case SIGNUP:
				ret = signUp();
				break;
			case SAVEDATA:
				ret = saveData();
				break;
			case LOADDATA:
				ret = loadData();
				break;		
			}
					
		}catch(SQLException e)
		{
			System.out.println("SQLException : " + e.getMessage());
			ret = false;
		}finally{
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

}
