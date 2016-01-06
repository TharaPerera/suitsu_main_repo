package com.mycompany.mavenproject1;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Created with IntelliJ IDEA
 * User: Tharanga Ranaweera
 * Date: 8/07/14
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseUtils {

    private static volatile DataSource ussdDatasource = null;
    
   // private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Endpoints.class.getName());

    //private static Log log = LogFactory.getLog(DatabaseUtils.class);

    public static void initializeDataSource() throws NamingException {
        if (ussdDatasource != null ) {
            return;
        }

        String statdataSourceName = "jdbc/CONNECT_DB";

        if (statdataSourceName != null) {
            try {
                Context ctx = new InitialContext();
                ussdDatasource = (DataSource) ctx.lookup(statdataSourceName);
            } catch (NamingException e) {
               //log.error(e);
               throw e;
            }

        }
    }


    public static void insertMultiplePasswordPIN(String username, String ussdSessionID) throws SQLException{




        Connection connection = null;

        PreparedStatement ps = null;

        ResultSet results = null;



        String sql = "INSERT INTO multiplepasswords (username, attempts, ussdsessionid) VALUES (?, ?, ?);";

        try {

            try {

                connection = getUssdDBConnection();

            } catch (NamingException ex) {

                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);

            }

            ps = connection.prepareStatement(sql);



            ps.setString(1, username);

            ps.setInt(2, 1);

            ps.setString(3, ussdSessionID);


            Endpoints.logInfo(ps.toString());
            ps.execute();



        } catch (SQLException e) {

            System.out.print(e.getMessage());

        } finally {

            connection.close();

        }

    }
     public static void updateUSerStatus(String sessionID, String status) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        
        String sql = "INSERT INTO `clientstatus` (`SessionID`, `Status`) VALUES (?, ?);";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);

                ps.setString(1, sessionID);
                ps.setString(2, status);

                Endpoints.logInfo(ps.toString());
                ps.execute();
                        
		} catch (NamingException ex) {
                 //Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
             } 
            catch (SQLException e) {
                    System.out.print(e.getMessage());
		} finally {
                        connection.close();			
		}
            
    }
     
    public static void updateStatus(String sessionID, String status) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        
        String sql =
		             "update `clientstatus` set "
		                     + "Status=? where " 
                                     + "SessionID=?;" ;
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);

                ps.setString(1, status);
                ps.setString(2, sessionID);
                Endpoints.logInfo(ps.toString());
                ps.execute();
             
                        
            } catch (NamingException ex) {
                // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
             } 
            catch (SQLException e) {
                System.out.print(e.getMessage());
            } finally {
                connection.close();			
            }
            
    }
     
    /**
     * Insert pin to PIN_RESET.
     * @param sessionID sessionId
     * @param status status
     * @param pin PIN
     * @throws SQLException exception
     */
    public static void insertPinResetRequest(String sessionID, String status, String pin) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        
        String sql = "INSERT INTO `clientstatus` (`SessionID`, `Status`, `pin`) VALUES (?, ?, ?);";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);

                ps.setString(1, sessionID);
                ps.setString(2, status);
                ps.setString(3, pin);
                
                Endpoints.logInfo(ps.toString());
                ps.execute();
                        
		} catch (NamingException ex) {
                 //Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
			System.out.println("Error found: "+ex);
             } 
            catch (SQLException e) {
                    System.out.print(e.getMessage());
		} finally {
                        connection.close();			
		}
            
    }
    
    public static String getUSerPIN(String sessionID) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        String pin = null; 
        ResultSet rs = null;
        
        String sql =
		             "select pin "
		                     + "from `pin` where " + "SessionID=?;";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);
            
                ps.setString(1, sessionID);

                rs = ps.executeQuery();
            
                while (rs.next()) {
                    pin = rs.getString("pin");
                }
                        
		} catch (NamingException ex) {
                   // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException e) {
                    System.out.print(e.getMessage());
		} finally {
                        connection.close();
                        
		}
            
            return pin;
     }
    
    //insert initial Entry
    public static void insertMultiplePasswordPIN(String username) throws SQLException{
        
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
 
        
        String sql = "INSERT INTO `multiplepasswords` (`username`, `attempts`) VALUES (?, ?);";
        try {
            try {
                connection = getUssdDBConnection();
            } catch (NamingException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
                        ps = connection.prepareStatement(sql);
			
			ps.setString(1, username);
                        ps.setInt(2, 1);
            Endpoints.logInfo(ps.toString());
			ps.execute();
                        
		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			connection.close();
		}
    }

    
    //Update PIN
    public static void updateMultiplePasswordPIN(String username,int pin) throws SQLException{
        
        Connection connection = null;
        PreparedStatement ps = null;
        
        String sql =
		             "update `multiplepasswords` set "
		                     + "pin=? where " 
                                     + "username=?;" ;
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);

                ps.setInt(1, pin);
                ps.setString(2, username);
                Endpoints.logInfo(ps.toString());
                ps.execute();
             
                        
            } catch (NamingException ex) {
                // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
             } 
            catch (SQLException e) {
                System.out.print(e.getMessage());
            } finally {
                connection.close();			
            }
    }
    
    //Update no of attempts
    public static void updateMultiplePasswordNoOfAttempts(String username,int attempts) throws SQLException{
        
        Connection connection = null;
        PreparedStatement ps = null;
        
        String sql =
		             "update `multiplepasswords` set "
		                     + "attempts=? where " 
                                     + "username=?;" ;
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);

                ps.setInt(1, attempts);
                ps.setString(2, username);
                Endpoints.logInfo(ps.toString());
                ps.execute();
             
                        
            } catch (NamingException ex) {
                // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
             } 
            catch (SQLException e) {
                System.out.print(e.getMessage());
            } finally {
                connection.close();			
            }
    }
      //Read PIN  
      public static int readMultiplePasswordPIN(String username) throws SQLException{
        
       Connection connection = null;
        PreparedStatement ps = null;
        int pin = 0; 
        ResultSet rs = null;
        
        String sql =
		             "select pin "
		                     + "from `multiplepasswords` where " + "username=?;";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);
            
                ps.setString(1, username);

                rs = ps.executeQuery();
            
                while (rs.next()) {
     
                    pin = rs.getInt("pin");
                }
                        
		} catch (NamingException ex) {
                   // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException e) {
                    System.out.print(e.getMessage());
		} finally {
                        connection.close();
                        
		}
            
            return pin;
    }
    
    //Read Attempts
    public static int readMultiplePasswordNoOfAttempts(String username) throws SQLException{
        
        Connection connection = null;
        PreparedStatement ps = null;
        int noOfAttempts = 0; 
        ResultSet rs = null;
        
        String sql =
		             "select attempts "
		                     + "from `multiplepasswords` where " + "username=?;";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);
            
                ps.setString(1, username);

                rs = ps.executeQuery();
            
                while (rs.next()) {
     
                    noOfAttempts = rs.getInt("attempts");
                }
                        
		} catch (NamingException ex) {
                   // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException e) {
                    System.out.print(e.getMessage());
		} finally {
                        connection.close();
                        
		}
            
            return noOfAttempts;
    }
    
    public static boolean isExistingUser(String username) throws SQLException{
        boolean isUser = false;
        
        Connection connection = null;
        PreparedStatement ps = null;
        String usernameDB = "noUser";
        ResultSet rs = null;
        
        String sql =
		             "select username "
		                     + "from `multiplepasswords` where " + "username=?;";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);
            
                ps.setString(1, username);

                rs = ps.executeQuery();
            
                while (rs.next()) {
     
                    usernameDB = rs.getString("username");
                }
                        
		} catch (NamingException ex) {
                   // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException e) {
                    System.out.print(e.getMessage());
		} finally {
                        connection.close();
                        
		}
            
            if (usernameDB.equals(username) ){
                
                isUser = true;
            }else{
                isUser = false;
            }
            return isUser;

      }
    
    //Delete Entry
    public static void deleteUser(String username) throws SQLException{
        
        Connection connection = null;
        PreparedStatement ps = null;
        int noOfAttempts = 0; 
        ResultSet rs = null;
        
        String sql =
		             "delete "
		                     + "from `multiplepasswords` where " + "username=?;";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);
            
                ps.setString(1, username);
                Endpoints.logInfo(ps.toString());
                ps.execute();
                        
		} catch (NamingException ex) {
                   // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException e) {
                    System.out.print(e.getMessage());
		} finally {
                        connection.close();
                        
		}
    }
    
    public static String getUSerStatus(String username) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        String userStatus = null; 
        ResultSet rs = null;
        
        String sql =
		             "select status "
		                     + "from `regstatus` where " + "username=?;";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);
            
                ps.setString(1, username);

                rs = ps.executeQuery();
            
                while (rs.next()) {
                    userStatus = rs.getString("status");
                }
                        
		} catch (NamingException ex) {
                   // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException e) {

		} finally {
                        connection.close();
                        
		}
            
            return userStatus;
     }


    //insert initial Entry
    public static String insertUserStatus(String username,String status) throws SQLException{

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet results = null;
        String uuid="";
        UUID idOne = UUID.randomUUID();
        uuid=idOne.toString();

        String sql = "INSERT INTO `regstatus` (`uuid`,`username`, `status`) VALUES (?,?,?);";
        try {
            try {
                connection = getUssdDBConnection();
            } catch (NamingException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = connection.prepareStatement(sql);
            ps.setString(1, uuid);
            ps.setString(2, username);
            ps.setString(3, status);
            Endpoints.logInfo(ps.toString());
            ps.execute();

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            connection.close();
        }
        return uuid;
    }

     
     
    public static boolean isExistingUserStatus(String username) throws SQLException{
        boolean isUser = false;
        
        Connection connection = null;
        PreparedStatement ps = null;
        String usernameDB = "noUser";
        ResultSet rs = null;
        
        String sql =
		             "select username "
		                     + "from `regstatus` where " + "username=?;";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);
            
                ps.setString(1, username);

                rs = ps.executeQuery();
            
                while (rs.next()) {
     
                    usernameDB = rs.getString("username");
                }
                        
		} catch (NamingException ex) {
                   // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException e) {
                    System.out.print(e.getMessage());
		} finally {
                        connection.close();
                        
		}
            
            if (usernameDB.equals(username) ){
                
                isUser = true;
            }else{
                isUser = false;
            }
            return isUser;

      }
    
    //Delete Entry
    public static void deleteUserStatus(String username) throws SQLException{
        
        Connection connection = null;
        PreparedStatement ps = null;
        int noOfAttempts = 0; 
        ResultSet rs = null;
        
        String sql =
		             "delete "
		                     + "from `regstatus` where " + "username=?;";
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);
            
                ps.setString(1, username);
                Endpoints.logInfo(ps.toString());
                ps.execute();
                        
		} catch (NamingException ex) {
                   // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (SQLException e) {

		} finally {
                        connection.close();
                        
		}
    }
    
     public static void updateRegStatus(String username, String status) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        
        String sql =
		             "update `regstatus` set "
		                     + "status=? where " 
                                     + "username=?;" ;
       
            try {
                connection = getUssdDBConnection();
            
                ps = connection.prepareStatement(sql);

                ps.setString(1, status);
                ps.setString(2, username);
                Endpoints.logInfo(ps.toString());
                ps.execute();
             
                        
            } catch (NamingException ex) {
                // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
             } 
            catch (SQLException e) {
                System.out.print(e.getMessage());
            } finally {
                connection.close();			
            }
    }
    
    static Integer getPendingUSSDRequestType(String msisdn) throws SQLException {
        
        Connection connection = null;
        PreparedStatement ps = null;
        Integer requestType = null;

        String sql = "select requesttype"
                        + " from `suitsu_users` where msisdn=?;";

        try {
            connection = getUssdDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, msisdn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requestType = rs.getInt("requesttype");
                break;
            }
        } catch (NamingException ex) {
            // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
//        String json = "{\"requestType\":" + requestType + ",\"sessionId\":\"" + sessionId + "\"}";
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonElement jelem = gson.fromJson(json, JsonElement.class);
//        return jelem.getAsJsonObject();
        return requestType;
    }

    static int saveRequestType(String msisdn, Integer requestType) throws SQLException,NamingException {
        Connection connection = null;
//        String sql = "insert into pendingussd (msisdn, requesttype) values (?,?)";
        String sql = "insert into suitsu_users (msisdn, requesttype) values (?,?) ON DUPLICATE KEY UPDATE requesttype=VALUES(requesttype)";
        try {
            connection = getUssdDBConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, msisdn);
            ps.setInt(2, requestType);
            ps.executeUpdate();
            return 1;
        } catch (NamingException ex) {
            // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
        return -1;
    }
    
    public static int saveRequestType(String userName, String requestType) throws SQLException,NamingException{
		// TODO Auto-generated method stub
    	Connection connection = null;
    	String sql = "insert into suitsu_users (msisdn, requesttype) values (?,?) ON DUPLICATE KEY UPDATE requesttype=VALUES(requesttype)";
        try {
            connection = getUssdDBConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, requestType);
            ps.executeUpdate();
            return 1;
        } catch (NamingException ex) {
            // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
        return -1;
	}
    
    
    public static int savePivotRequest(String userName, String url, String time, String desc) throws SQLException,NamingException{
		// TODO Auto-generated method stub
    	
    	Connection connection = null;
    	String sql = "insert into pivotrequest (username, url, time, description) values (?,?,?,?) ";
        try {
            connection = getUssdDBConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            ps.setString(2, url);
            ps.setString(3, time);
            ps.setString(4, desc);
            ps.executeUpdate();
            return 1;
        } catch (NamingException ex) {
            // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
        return -1;
	}
    
    

    static void deleteRequestType(String msisdn) throws SQLException {
        Connection connection = null;
        String sql = "delete from pendingussd where msisdn = ?";
        try {
            connection = getUssdDBConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, msisdn);
            ps.executeUpdate();
        } catch (NamingException ex) {
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
    }
    
    public static Connection getUssdDBConnection() throws SQLException,NamingException {
        initializeDataSource();
        if (ussdDatasource != null) {
            return ussdDatasource.getConnection();
        } else {
            throw new SQLException("USSD Datasource not initialized properly");
        }
    }

    public static int saveAuthenticateData(AuthenticationData authenticationData) throws SQLException,NamingException {
        Connection connection = null;

        String sql = "insert into authenticated_login (tokenID,scope,redirect_uri,client_id,response_type,acr_value,msisdn,state,nonce) values (?,?,?,?,?,?,?,?,?) ";
        try {
            connection = getUssdDBConnection();

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, authenticationData.getTokenID());
            ps.setString(2, authenticationData.getScope());
            ps.setString(3, authenticationData.getRedirectUri());
            ps.setString(4, authenticationData.getClientId());
            ps.setString(5, authenticationData.getResponseType());
            ps.setInt(6, authenticationData.getAcrValues());
            ps.setString(7, authenticationData.getMsisdn());
            ps.setString(8, authenticationData.getState());
            ps.setString(9, authenticationData.getNonce());

            ps.executeUpdate();
            return 1;
        } catch (NamingException ex) {
           ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
        return -1;
    }
    
    
    
    
    

    public static AuthenticationData getAuthenticateData(String tokenID) throws SQLException,NamingException {

        Connection connection = null;
        PreparedStatement ps = null;
        String userStatus = null;
        ResultSet rs = null;
        AuthenticationData authenticationData = new AuthenticationData();

        String sql = "select *"
                + " from `authenticated_login` where tokenID=?;";

        try {
            connection = getUssdDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, tokenID);
            rs = ps.executeQuery();
            while (rs.next()) {
                authenticationData.setTokenID(tokenID);
                authenticationData.setScope(rs.getString("scope"));
                authenticationData.setRedirectUri(rs.getString("redirect_uri"));
                authenticationData.setClientId(rs.getString("client_id"));
                authenticationData.setResponseType(rs.getString("response_type"));
                authenticationData.setAcrValues(rs.getInt("acr_value"));
                authenticationData.setMsisdn(rs.getString("msisdn"));
                authenticationData.setState(rs.getString("state"));
                authenticationData.setNonce(rs.getString("nonce"));
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }

            return authenticationData;
        }
    }



    public static void updateAuthenticateData(String msisdn, String status) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        String sql =
                "update `authenticated_login` set "
                        + "status=? where "
                        + "msisdn=?;" ;

        try {
            connection = getUssdDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, msisdn);
            Endpoints.logInfo(ps.toString());
            ps.execute();
            } catch (NamingException ex) {
            ex.printStackTrace();
        }
        catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            connection.close();
        }
    }


    public static void updateAuthenticateDataMsisdn(String tokenId, String msisdn) throws SQLException{
        Connection connection = null;
        PreparedStatement ps = null;
        String sql =
                "update `authenticated_login` set "
                        + "msisdn=? where "
                        + "tokenID=?;" ;

        try {
            connection = getUssdDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, msisdn);
            ps.setString(2, tokenId);
            Endpoints.logInfo(ps.toString());
            ps.execute();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            connection.close();
        }
    }


    //insert initial Entry
    public static String getUserNameById(String uuid) throws SQLException{

        Connection connection = null;
        PreparedStatement ps = null;
        String username= "noUser";
        ResultSet rs = null;

        String sql =
                "select username from `regstatus` where uuid=?;";

        try {
            connection = getUssdDBConnection();

            ps = connection.prepareStatement(sql);

            ps.setString(1, uuid);

            rs = ps.executeQuery();

            while (rs.next()) {

                username = rs.getString("username");
            }

        } catch (NamingException ex) {
            // Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            connection.close();

        }


        return username;
    }


	public static List<PivotRequests> getPivotRequestData(String userName) throws SQLException,NamingException{
		Connection connection = null;
        PreparedStatement ps = null;
        
        ResultSet rs = null;
        PivotRequests pivotrequest=new PivotRequests();
        List<PivotRequests> requestlist=new ArrayList<PivotRequests>();
        String sql = "select *"
                + " from `pivotrequest` where NOT username=?;";

        try {
            connection = getUssdDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            rs = ps.executeQuery();
            
            while (rs.next()) {
            	pivotrequest=new PivotRequests();
                pivotrequest.setUsername(userName);
                pivotrequest.setrID(rs.getString("ID"));
                pivotrequest.setUrl(rs.getString("url"));
                pivotrequest.setTime(rs.getString("time"));
                pivotrequest.setDesc(rs.getString("description"));
                requestlist.add(pivotrequest);
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }

            return requestlist;
        }
		
	}

	public static List<String> getPivotRequestDataList(String userName) throws SQLException,NamingException{
		Connection connection = null;
        PreparedStatement ps = null;
        
        ResultSet rs = null;
        List<String> requestlist=new ArrayList<String>();
        String sql = "select ID"
                + " from `pivotrequest` where NOT username=?;";

        try {
            connection = getUssdDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, userName);
            rs = ps.executeQuery();
            
            while (rs.next()) {
            	
                requestlist.add(rs.getString("ID"));
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }

            return requestlist;
        }
		
	}


	public static void saveUserData(User friend) throws SQLException,NamingException{
		// TODO Auto-generated method stub
		Connection connection = null;

        String sql = "insert into friendlist (username,isFriend,time,marks,likes,comments,age,gender,recList) values (?,?,?,?,?,?,?,?,?) ";
        try {
            connection = getUssdDBConnection();

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, friend.getUsername() );
            ps.setString(2, friend.getIsFriend());
            ps.setString(3, friend.getTime());
            ps.setString(4, friend.getMarks());
            ps.setString(5, friend.getLikeCount());
            ps.setString(6, friend.getCommCount());
            ps.setString(7, friend.getAge());
            ps.setString(8, friend.getGender() );
            ps.setString(9, friend.getRecListArr());

            ps.executeUpdate();
            
        } catch (NamingException ex) {
           ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if(connection != null) {
                connection.close();
            }
        }
        
	}


	public static void saveRankedData(RankList rankList) throws SQLException,NamingException{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				Connection connection = null;

		        String sql = "insert into ranklist (prid,username, rank1, rank2, rank3, rank4, rank5, rank6, rank7, rank8, rank9, rank10) values (?,?,?,?,?,?,?,?,?,?,?,?) ";
		        try {
		            connection = getUssdDBConnection();

		            PreparedStatement ps = connection.prepareStatement(sql);
		            ps.setString(1, rankList.getPrID() );
		            ps.setString(2, rankList.getUsername());
		            ps.setString(3, rankList.getRank1());
		            ps.setString(4, rankList.getRank2());
		            ps.setString(5, rankList.getRank3());
		            ps.setString(6, rankList.getRank4());
		            ps.setString(7, rankList.getRank5());
		            ps.setString(8, rankList.getRank6() );
		            ps.setString(9, rankList.getRank7());
		            ps.setString(10, rankList.getRank8());
		            ps.setString(11, rankList.getRank9());
		            ps.setString(12, rankList.getRank10());

		            ps.executeUpdate();
		            
		        } catch (NamingException ex) {
		           ex.printStackTrace();
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        } finally {
		            if(connection != null) {
		                connection.close();
		            }
		        }
	}


	public static List<String> getPivotRequestDetailsDataList(Integer prid) throws SQLException,NamingException{
		Connection connection = null;
        PreparedStatement ps = null;
        
        ResultSet rs = null;
        List<String> requestlist=new ArrayList<String>();
        String sql = "select *"
                + " from `pivotrequest` where ID=?;";

        try {
            connection = getUssdDBConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, prid);
            rs = ps.executeQuery();
            
            while (rs.next()) {
            	
                requestlist.add(rs.getString("ID"));
                requestlist.add(rs.getString("username"));
                requestlist.add(rs.getString("url"));
                requestlist.add(rs.getString("time"));
                requestlist.add(rs.getString("description"));
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }

            return requestlist;
        }
	}
	


	
    
    
}
