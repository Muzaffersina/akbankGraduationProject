package com.msa.bankingsystem.services.message;

public class Messages {
	//Accounts
	public static final String CREATEACCOUNT = "Created Account , Account Number :";
	public static final String DELETEACCOUNTBYID = "Account Deleted";
	public static final String LISTEDACCOUNTBYACCOUNTNO = "Listed Account By AccountNumber";
	public static final String LISTEDACCOUNTBYUSERID = "Listed Account By UserId";
	public static final String LISTEDALLACCOUNTS = "Listed Accounts";	
	public static final String INVALIDACCOUNTTYPE = "Invalid Account Type: ";
	public static final String NOTFOUNDACCOUNTNUMBER = "This account number cannot be found";
	public static final String NOTFOUNDACCOUNTBYUSERID= "This user does not have an account";	
	public static final String ACCOUNTBADREQUEST = "Please Check Your Account Information";	
	public static final String INSUFFICIENTBALANCE = "Insufficient balance";
	public static final String SUCCESSDEPOSİTOPERATİON = "Deposit Operation Successful";
	public static final String SUCCESSWITHDRAWOPERATİON = "WithDraw Operation Successful";
	public static final String SUCCESSTRANSFEROPERATİON = "Transfer Operation Successful";	
	public static final String NOTFOUNDLOGDETAILSBYACCOUNTNUMBER = "No Record Found For This Account Number";
	public static final String LISTEDLOGSBYACCOUNTNUMBER = "Listed Logs for accountNumber :";
	
	public static final String ACCESSDENIED= "Access Denied";
	
	public static final String INVALIDACCOUNTNUMBER= "Invalid Account Number";
		
	
	//Banks
	public static final String BANKALREADYEXİTS = "Given BANK Name Already Used :";
	public static final String CREATEDBANK = "Created Bank Successfully";	
	
	// Users
	public static final String CREATEDUSER = "Created User Successfully";	
	public static final String EMAILALREADYEXİTS = "Given email already Used : ";
	public static final String USERNAMEALREADYEXİTS = "Given username already Used : ";
	public static final String CHANGEUSERACTIVATION = "User Activation is : ";	
	public static final String NOTFOUNDUSERBYID = "This user cannot be found : ";
	
	//Log in
	public static final String LOGINSUCCESSFULLY = "Logged-In Successfully";
	public static final String LOGINBADCREDENTIALS = "Bad Credentials";
	public static final String LOGINUSERDISABLED = "User is disabled";
	
	//Validates
	public static final String VALIDATIONERRORS = "Validation.Errors";
	public static final String VALIDATIONAMOUNTERRORS = " value must be positive";	
	public static final String VALIDATIONNOTNULLERRORS = " field cannot be null";
	public static final String VALIDATIONEMAILERRORS = " field must contain '@'";
}
