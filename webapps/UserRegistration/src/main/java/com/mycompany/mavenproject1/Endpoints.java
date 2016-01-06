/*
 * Queries.java
 * Apr 2, 2013  11:20:38 AM
 * Dialog Axiata
 *
 * Copyright (C) Dialog Axiata PLC. All Rights Reserved.
 */
package com.mycompany.mavenproject1;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.axis2.AxisFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONException;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.identity.user.registration.stub.UserRegistrationAdminServiceException;
import org.wso2.carbon.identity.user.registration.stub.UserRegistrationAdminServiceIdentityException;
import org.wso2.carbon.identity.user.registration.stub.dto.UserDTO;
import org.wso2.carbon.identity.user.registration.stub.dto.UserFieldDTO;
import org.wso2.carbon.um.ws.api.stub.RemoteUserStoreManagerServiceUserStoreExceptionException;
import org.wso2.carbon.user.core.UserCoreConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gsma.shorten.SelectShortUrl;
import com.mycompany.mavenproject1.sms.OutboundSMSMessageRequest;
import com.mycompany.mavenproject1.sms.OutboundSMSTextMessage;
import com.mycompany.mavenproject1.sms.SendSMSRequest;
import com.mycompany.mavenproject1.utils.ConfigLoader;
import com.mycompany.mavenproject1.utils.LOA;
import com.mycompany.mavenproject1.utils.LOA.MIFEAbstractAuthenticator;
import com.mycompany.mavenproject1.utils.LOAConfig;
import com.mycompany.mavenproject1.utils.ReadMobileConnectConfig;

//import org.json.JSONException;
//import Aloo.AdminServicesInvoker.LoginAdminServiceClient;

/**
 * REST Web Service Dialog Axiata
 * 
 * @version $Id: Queries.java,v 1.00.000
 */
@Path("/endpoint")
public class Endpoints {

	@Context
	private UriInfo context;
	private static Log log = LogFactory.getLog(Endpoints.class);
	private static Map<String, UserRegistrationData> userMap = new HashMap<String, UserRegistrationData>();
	//Map to keep msisdns of PIN_RESET requested.
	private static Map<String, String> pinResetRequest = new HashMap<String, String>();
		
	String successResponse = "\"" + "amountTransaction" + "\"";
	String serviceException = "\"" + "serviceException" + "\"";
	String policyException = "\"" + "policyException" + "\"";
	String errorReturn = "\"" + "errorreturn" + "\"";

	/**
	 * Creates a new instance of QueriesResource
	 */
	public Endpoints() {

	}

	
	
	
	
	@GET
	@Path("/ussd/saverequestuser")
	@Produces("application/json")
	public Response saveRequestType(@QueryParam("username") String userName, @QueryParam("requesttype") String requestType) {

		String responseString = null;
		int status = -1;
		String message = "error";
		try {
			status = DatabaseUtils.saveRequestType(userName, requestType);
			message = "success";
		} catch (Exception ex) {
			Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
		}
		responseString = "{\"status\":\"" + status + "\",\"message\":\"" + message + "\"}";
		return Response.status(200).entity(responseString).build();
	}
	
	@GET
	@Path("/suitsu/savepivotrequest")
	@Produces("application/json")
	public Response saveRequestType(@QueryParam("username") String userName, @QueryParam("url") String url, @QueryParam("time") String time, @QueryParam("desc") String desc) {
		System.out.println(userName);
		String responseString = null;
		int status = -1;
		String message = "error";
		try {
			status = DatabaseUtils.savePivotRequest(userName, url, time, desc);
			message = "success";
		} catch (Exception ex) {
			Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
		}
		responseString = "{\"status\":\"" + status + "\",\"message\":\"" + message + "\"}";
		return Response.status(200).entity(responseString).build();
	}
	
	
	
	
	@GET
	@Path("/user/request/get")
	@Produces("application/json")
	public Response getAuthatication(@QueryParam("username") String userName) throws SQLException,
			RemoteException, Exception {
		//List<PivotRequests> requestlist=new ArrayList<PivotRequests>();
		List<String> requestlist=new ArrayList<String>();
		requestlist=DatabaseUtils.getPivotRequestDataList(userName);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(requestlist);
		return Response.status(200)
				.entity(json.toString())
				.header("Access-Control-Allow-Origin","*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
				.build();
	}
	
	@GET
	@Path("/user/requestdetails/get")
	@Produces("application/json")
	public Response getAuthatication(@QueryParam("prid") Integer prid) throws SQLException,
			RemoteException, Exception {
		//List<PivotRequests> requestlist=new ArrayList<PivotRequests>();
		List<String> requestdetailslist=new ArrayList<String>();
		requestdetailslist=DatabaseUtils.getPivotRequestDetailsDataList(prid);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(requestdetailslist);
		return Response.status(200)
				.entity(json.toString())
				.header("Access-Control-Allow-Origin","*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
				.build();
	}
	
	
	
	

	private static String getHashValue(String value) throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(value.getBytes("UTF-8"));

		StringBuilder hexString = new StringBuilder();

		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}

		logInfo("UserHash");

		String hashString = hexString.toString();

		return hashString;
	}

	private void addUser(String userName, String pin) throws Exception {
		UserRegistrationData userRegistrationData = userMap.get(userName);
		if (isUserExists(userName)) {
			updatePIN(userName, pin);
		} else {
			long waitingTime = Integer.parseInt(FileUtil.getApplicationProperty("waitinTimeInMinutes")) * 1000 * 60;

			long currentTime = System.currentTimeMillis();
			if ((userRegistrationData != null)
					&& (currentTime - userRegistrationData.getUserRegistrationTime()) <= waitingTime) {
				String adminURL = FileUtil.getApplicationProperty("isadminurl")
						+ "/services/UserRegistrationAdminService";
				UserRegistrationAdminServiceClient userRegistrationAdminServiceClient = new UserRegistrationAdminServiceClient(
						adminURL);
				UserFieldDTO[] userFieldDTOs = userRegistrationAdminServiceClient
						.readUserFieldsForUserRegistration(userRegistrationData.getClaim());
				pin = getHashValue(pin);

				String[] fieldValues = userRegistrationData.getFieldValues().split(",");
				for (int count = 0; count < fieldValues.length; count++) {
					userFieldDTOs[count].setFieldValue(fieldValues[count]);
					if (userFieldDTOs[count].getFieldName().equals("pin")) {
						userFieldDTOs[count].setFieldValue(pin);
					}
				}

				UserDTO userDTO = new UserDTO();
				userDTO.setOpenID(userRegistrationData.getOpenId());
				userDTO.setPassword(userRegistrationData.getPassword());
				userDTO.setUserFields(userFieldDTOs);
				userDTO.setUserName(userRegistrationData.getUserName());

				userRegistrationAdminServiceClient.addUser(userDTO);

				DatabaseUtils.updateRegStatus(userName, "Approved");
				DatabaseUtils.updateAuthenticateData(userName, "1");
			}
		}
		// removing user from the static Map , after registering user properly
		userMap.remove(userName);
	}

	private static void updatePIN(String userName, String pin) throws NoSuchAlgorithmException,
			UnsupportedEncodingException, AxisFault, RemoteException, LoginAuthenticationExceptionException {

		LoginAdminServiceClient lAdmin = new LoginAdminServiceClient(FileUtil.getApplicationProperty("isadminurl"));
		String sessionCookie = lAdmin.authenticate(FileUtil.getApplicationProperty("adminusername"),
				FileUtil.getApplicationProperty("adminpassword"));

		RemoteUserStoreServiceAdminClient remoteUserStoreServiceAdminClient = new RemoteUserStoreServiceAdminClient(
				FileUtil.getApplicationProperty("isadminurl"), sessionCookie);

		// Hashing user PIN
		pin = getHashValue(pin);
		try {
			// User claim update
			remoteUserStoreServiceAdminClient.setUserClaim(userName, "http://wso2.org/claims/pin", pin,
					UserCoreConstants.DEFAULT_PROFILE);
		} catch (RemoteException ex) {
			Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
		} catch (RemoteUserStoreManagerServiceUserStoreExceptionException ex) {
			Logger.getLogger(Endpoints.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	protected void postRequest(String url, String requestStr, String operator) throws IOException {

		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(url);

		postRequest.addHeader("accept", "application/json");
		postRequest.addHeader("Authorization", "Bearer " + FileUtil.getApplicationProperty("accesstoken"));
		postRequest.addHeader("operator", operator);
		logInfo("operator :"+operator);
		StringEntity input = new StringEntity(requestStr);
		input.setContentType("application/json");

		postRequest.setEntity(input);

		HttpResponse response = client.execute(postRequest);

		if ((response.getStatusLine().getStatusCode() != 201)) {
			// logInfo("Error occurred while calling end points");
		} else {
			// logInfo("Success Request");
		}

	}

	
	@GET
	@Path("/user/exists")
	// @Consumes("application/json")
	@Produces("application/json")
	public Response sendSMSOneAPI(@QueryParam("username") String userName, String jsonBody) throws SQLException,
			RemoteException, Exception {

		String returnString = String.valueOf(isUserExists(userName));
		return Response.status(200).entity(returnString).build();
	}

	private boolean isUserExists(String userName) throws SQLException, RemoteException, Exception {
		LoginAdminServiceClient lAdmin = new LoginAdminServiceClient(FileUtil.getApplicationProperty("isadminurl"));
		String sessionCookie = lAdmin.authenticate(FileUtil.getApplicationProperty("adminusername"),
				FileUtil.getApplicationProperty("adminpassword"));

		RemoteUserStoreServiceAdminClient remoteUserStoreServiceAdminClient = new RemoteUserStoreServiceAdminClient(
				FileUtil.getApplicationProperty("isadminurl"), sessionCookie);
		boolean userExists = false;
		if (remoteUserStoreServiceAdminClient.isExistingUser(userName)) {
			userExists = true; // user already exists
		}
		return userExists;
	}

	@GET
	@Path("/user/setclaim")
	@Produces("application/json")
	public Response setUserClaimValue(@QueryParam("msisdn") String msisdn, @QueryParam("claimValue") String claimValue)
			throws SQLException, org.codehaus.jettison.json.JSONException, JSONException, RemoteException,
			LoginAuthenticationExceptionException, RemoteUserStoreManagerServiceUserStoreExceptionException {

		LoginAdminServiceClient lAdmin = new LoginAdminServiceClient(FileUtil.getApplicationProperty("isadminurl"));
		String sessionCookie = lAdmin.authenticate(FileUtil.getApplicationProperty("adminusername"),
				FileUtil.getApplicationProperty("adminpassword"));

		RemoteUserStoreServiceAdminClient remoteUserStoreServiceAdminClient = new RemoteUserStoreServiceAdminClient(
				FileUtil.getApplicationProperty("isadminurl"), sessionCookie);
		remoteUserStoreServiceAdminClient.setUserClaim(msisdn, "http://wso2.org/claims/authenticator", claimValue,
				msisdn);
		return Response.status(200).entity("{message:SUCCESS}").build();

	}

	

	

	// get data for in-line authentication
	@GET
	@Path("/user/authenticate/get")
	@Produces("application/json")
	public Response getAuthatication(@QueryParam("tokenid") String tokenid, String jsonBody) throws SQLException,
			RemoteException, Exception {
		AuthenticationData authenticationData = DatabaseUtils.getAuthenticateData(tokenid);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(authenticationData);
		return Response.status(200).entity(json.toString()).build();
	}

	@GET
	@Path("/user/authenticate/updatemsisdn")
	@Produces("application/json")
	public Response updateAuthaticationMsisdn(@QueryParam("tokenid") String tokenid,
			@QueryParam("msisdn") String msisdn, String jsonBody) throws SQLException, RemoteException, Exception {
		AuthenticationData authenticationData = DatabaseUtils.getAuthenticateData(tokenid);
		DatabaseUtils.updateAuthenticateDataMsisdn(tokenid, msisdn);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(authenticationData);
		return Response.status(200).entity(json.toString()).build();
	}

	

	
	
	
	
	
	@GET
	@Path("/user/details/add")
	@Produces("application/json")
	public Response sendAuthatication(@QueryParam("username") String userName, @QueryParam("isFriend") String isFriend,
			@QueryParam("time") String time, @QueryParam("marks") String marks,
			@QueryParam("weight") String weight, @QueryParam("likeCount") String likeCount,@QueryParam("commCount") String commCount,@QueryParam("age") String age,@QueryParam("recListArr") String recListArr,String jsonBody)
			throws SQLException, RemoteException, Exception {
		User friend=new User();
		friend.setUsername(userName);
		friend.setIsFriend(isFriend);
		friend.setTime(time);
		friend.setMarks(marks);
		friend.setWeight(weight);
		friend.setLikeCount(likeCount);
		friend.setCommCount(commCount);
		friend.setAge(age);
		friend.setRecListArr(recListArr);
		DatabaseUtils.saveUserData(friend);
		String responseString = "success";
		return Response.status(200).entity(responseString).build();
	}
	
	@GET
	@Path("/user/ranks/add")
	@Produces("application/json")
	public Response sendAuthatication(@QueryParam("prid") String prId, @QueryParam("username") String userName,
			@QueryParam("rank1") String rank1, @QueryParam("rank2") String rank2, 
			@QueryParam("rank3") String rank3, @QueryParam("rank4") String rank4, 
			@QueryParam("rank5") String rank5, @QueryParam("rank6") String rank6, 
			@QueryParam("rank7") String rank7,  @QueryParam("rank8") String rank8, 
			@QueryParam("rank9") String rank9, @QueryParam("rank10") String rank10, 
			String jsonBody)
			throws SQLException, RemoteException, Exception {
		RankList rankList=new RankList();
		rankList.setPrID(prId);
		rankList.setUsername(userName);
		rankList.setRank1(rank1);
		rankList.setRank2(rank2);
		rankList.setRank3(rank3);
		rankList.setRank4(rank4);
		rankList.setRank5(rank5);
		rankList.setRank6(rank6);
		rankList.setRank7(rank7);
		rankList.setRank8(rank8);
		rankList.setRank9(rank9);
		rankList.setRank10(rank10);
		
		DatabaseUtils.saveRankedData(rankList);
		String responseString = "success";
		return Response.status(200).entity(responseString).build();
	}
	
	

	
	
	public static void logInfo(String log) {
		String fileName = FileUtil.getApplicationProperty("logs");
		try {
			FileWriter fileWriter = new FileWriter(fileName,true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.append(new Date().toString()+"	");
			bufferedWriter.append(log+"\n");
			bufferedWriter.close();
		} catch (IOException ex) {
			System.out.println("Error writing to file '" + fileName + "'");
		}
	}
	
}
