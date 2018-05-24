// Will Dahl
// 001273655
// ICSI 403
// March 29th, 2018

package csi403;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;
import java.util.*;


// Extend HttpServlet class
public class HashCollisions extends HttpServlet {

  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      doService(request, response); 
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }

  private void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    // Set response content type to be JSON
    response.setContentType("application/json");
    // Send back the response JSON message
    PrintWriter out = response.getWriter();
    try{
  	// Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }
      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);
      
      // Get the singular JSON object (name:value pair) in this message.    
      JsonObject obj = reader.readObject();
      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");

      //Creates the arrays to hold the string and Ascii values for the inputs
      String[] inList = new String[inArray.size()];
      int[] asciiArray = new int[inArray.size()];
      //Linked list of type Node to hold the diffrent key values.
      LinkedList<Node> chain = new LinkedList<Node>();

      //loops through the inArray
      for(int i = 0; i < inArray.size(); i++)
      {
        inList[i] = inArray.getString(i); // puts the contents of inArray into a java array
        String temp = inList[i].toLowerCase(); // puts the sting value of inList at i into temp as all lower case
        int asciiValue = 0; // holds the ascii value of temp
        Node key = new Node(); // node to hold value of key and count of collisions as key

        //loops through the string
        for(int j = 0; j < inList[i].length(); j++){
        	asciiValue += (int)temp.charAt(j);// adds the ascii value of teh char t the string ascii value
        }
       	
       	asciiArray[i] = asciiValue;//adds the ascii value to the an array
       	key.value = asciiValue; // sets the ascii value to the value filed in the key node

       	//checks if the key is already in the liked list of ascii keys
       	if(!inList(chain, key.value)){
       		chain.add(key);//Adds the key to the list of other ascii keys
       	}

       	else{
       		//loops through th keys already in the list
       		for(int j = 0; j < chain.size(); j++){
       			//chekcs if the key value equals the ascii key
       			if(chain.get(j).value == key.value){
       				chain.get(j).count++;//incremtns the count of collisions at that key
       			}
       		}
       	}
      }

      //Creates the Json Array Builders needed for output
      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
      JsonArrayBuilder innerArrayBuilder = Json.createArrayBuilder();

      //loops through list of keys
      for(int i = 0; i < chain.size(); i++){
      	//checks if there was a colision at the key value
      	if(chain.get(i).count > 0){
      		//loops through the array contiang the ascii values for the inputed strings
      		for(int j = 0; j < asciiArray.length; j++){
      			//checks of the value in the ascii array equals the key in the list
      			if(asciiArray[j] == chain.get(i).value){
      				innerArrayBuilder.add(inList[j]);// adds the string value to the array builder
      			}
      		}

      		outArrayBuilder.add(innerArrayBuilder); // adds the innner array to the outer array
      	}
      }

      //prints to output
      out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + " }"); 
    }
    //catches any Exceptinos
    catch(Exception e){
       	out.println("{ \"message\":\"Malformed JSON\" }");
    }
  }

  // Standard Servlet method
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }

  public boolean inList(LinkedList<Node> list, int value){
  	for(int i = 0; i < list.size(); i++){
  		if(list.get(i).value == value){
  			return true;
  		}
  	}

  	return false;
  }
}

class Node{
  	int value;
  	int count;

  	Node(){
  		count = 0;
  	}
}	