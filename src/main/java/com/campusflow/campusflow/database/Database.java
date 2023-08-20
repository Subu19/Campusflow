package com.campusflow.campusflow.database;

import Encryption.Encryption;
import com.campusflow.campusflow.EmailSender;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.campusflow.campusflow.qrGenerator.generator.generateQRCode;

public class Database{
    public static Connection con=null;
    static boolean connected = false;
    Database(){}

    public static String addAttendence(String data){
        try{
        JSONParser jsonParser = new JSONParser();
        Object rawJson = jsonParser.parse(data);
        JSONObject json = (JSONObject) rawJson;
        String sid = json.get("sid").toString();
        String bid = json.get("bid").toString();
        String token = json.get("token").toString();
        String date = LocalDate.now().toString();
        String semester = json.get("semester").toString();
            if(Tokens.list.contains(token)){
                String checkAttendence = "SELECT * FROM `"+bid+"attendence` WHERE sid="+sid+" AND date = '"+date+"';";
                PreparedStatement checkStatement = con.prepareStatement(checkAttendence);
                ResultSet result = checkStatement.executeQuery();
                result.next();

                System.out.println(result.getFetchSize());
                if(result.next()){
                    return "Your attendence has already been done; ID:" +sid;

                }else{
                    String sql = "Insert into "+bid+"attendence(sid,date,remarks,ofSemester)"+" values("+sid+",'"+date+"',1, '"+semester+"');";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    return "Added attendence for student ID:" +sid;

                }
            }else{
                return "QR Code has Expired!";
            }
        }catch (SQLException e){
            e.printStackTrace();
            return "Failed to add attendence";
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checkDatabaseDetails(){
        try(FileReader reader = new FileReader("./src/main/resources/config/config.json")){
            System.out.println("file found");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("file not found");
            return false;
        }

    }
    public static boolean postDatabaseDetails(String host, String database,String user,String pass,String port){
        System.out.println("i am here");
        JSONObject dbObject = new JSONObject();
        dbObject.put("host",host);
        dbObject.put("port", Integer.parseInt(port));
        dbObject.put("username",user);
        try {
            dbObject.put("password", Encryption.encrypt(pass));
        }catch (Exception e){
            e.printStackTrace();
        }
        dbObject.put("database",database);
        try{
            FileWriter writer = new FileWriter("./src/main/resources/config/config.json");
            writer.write(dbObject.toJSONString());
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    public static String getConnection(String host, String database,String user,String pass,String port){
        try{
//            Class.forName("com.mysql.jdbc.Driver");
            String url ="jdbc:mysql://localhost:"+port+"/"+database;
            con = DriverManager.getConnection(url,user,pass);
            System.out.println("Successfully connected!!");
            connected=true;
            return "connected";
        }catch(SQLException e){
            e.printStackTrace();
            return e.toString();
        }
    }
    public static boolean checkTable(String table){
        boolean check = false;
        try{
            String sql = "Select * from "+table;
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if(result.getFetchSize()>=0)
                check = true;

        }catch (SQLException e){
            e.printStackTrace();
        }
        return check;
    }

    //////====================home===================//////////
    public static String addDepartment(String id, String name, String hod){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("department")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO department VALUES("+Integer.parseInt(id)+",'"+name+"',"+Integer.parseInt(hod)+")";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE department(did int, name varchar(20), hod int, CONSTRAINT pk_did PRIMARY KEY (did))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO department VALUES("+Integer.parseInt(id)+",'"+name+"',"+Integer.parseInt(hod)+")";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }
    public static String addFaculty(String id, String name, String did){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("faculty")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO faculty VALUES("+Integer.parseInt(id)+",'"+name+"',"+Integer.parseInt(did)+")";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE faculty(fid int, faculty_name varchar(20), did int, CONSTRAINT pk_fid PRIMARY KEY (fid))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO faculty VALUES("+Integer.parseInt(id)+",'"+name+"',"+Integer.parseInt(did)+")";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }

    public static String addBatch( String year, String fid, String semester){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("batch")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO batch(year, fid, semester) VALUES('"+year+"',"+Integer.parseInt(fid)+",'"+semester+"')";
                    PreparedStatement statement = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                    statement.executeUpdate();

                    //create attendence
                    ResultSet keys = statement.getGeneratedKeys();
                    keys.next();
                    System.out.println(keys.getInt(1));
                    String sql2 = "CREATE TABLE "+keys.getInt(1)+"attendence(aid int AUTO_INCREMENT PRIMARY KEY, sid int, date date, remarks boolean, ofSemester varchar(10))";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();

                    //create marksheet
                    String sql4 = "CREATE TABLE "+keys.getInt(1)+"marksheet(mid int AUTO_INCREMENT PRIMARY KEY, sid int, semester varchar(10),term varchar(15), subId int, marks float)";
                    PreparedStatement statement4 = con.prepareStatement(sql4);
                    statement4.executeUpdate();

                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE batch(bid int AUTO_INCREMENT PRIMARY KEY, year varchar(20), fid int, semester varchar(10))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");



                    //now insert data!
                    String sql2 = "INSERT INTO batch(year, fid, semester) VALUES('"+year+"',"+Integer.parseInt(fid)+",'"+semester+"')";
                    PreparedStatement statement2 = con.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");

                    //create attendence
                    ResultSet keys = statement2.getGeneratedKeys();
                    keys.next();
                    String sql3 = "CREATE TABLE "+keys.getInt(1)+"attendence(aid int AUTO_INCREMENT PRIMARY KEY, sid int, date date, remarks boolean, ofSemester varchar(10))";
                    PreparedStatement statement3 = con.prepareStatement(sql3);
                    statement3.executeUpdate();

                    //create marksheet
                    String sql4 = "CREATE TABLE "+keys.getInt(1)+"marksheet(mid int AUTO_INCREMENT PRIMARY KEY, sid int, semester varchar(10),term varchar(15), subId int, marks float)";
                    PreparedStatement statement4 = con.prepareStatement(sql4);
                    statement4.executeUpdate();

                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }
    public static String addStudent(String firstname,String middlename, String lastname, String adderss,
                                    String contact, String email, String entrance,
                                    String fid,String bid, String pid, String sid) throws AddressException {
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("students")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO students VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+adderss+"',"+contact+",'"+email+"',"+entrance+","+fid+","+bid+","+pid+","+sid+")";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();

                    //generate login
                    addLogin(sid,bid,firstname,email);
                    feedback= "Success";

                }catch (Exception e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE students (first_name varchar(20),middle_name varchar(20),last_name varchar(20),address varchar(50),contact long, email varchar(50),entrance_score float, fid int, bid int, pid int, sid int, CONSTRAINT pk_sid PRIMARY KEY (sid))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO students VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+adderss+"',"+contact+",'"+email+"',"+entrance+","+fid+","+bid+","+pid+","+sid+")";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");

                    //generate login
                    addLogin(sid,bid,firstname,email);
                    feedback = "Success";

                }catch (Exception e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }
    private static void addLogin(String sid, String batch, String name,String email) throws Exception {

            if (connected) {
                if (checkTable("login")) {
                    //table exists, insert date
                    insertLogin(sid,batch,name,email);

                } else {
                    //create logins table
                    String sql = "CREATE TABLE login(lid int AUTO_INCREMENT PRIMARY KEY, sid int,username varchar(50), password varchar(50));";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    //insert new logins
                    insertLogin(sid,batch,name,email);
                }
            }

    }
    private static void insertLogin (String sid, String batch, String name,String email) throws Exception {
        String username;
        String password;
        String year;
        String faculty;

        //gather require batch year and faculty name for the student input
        String getInfoSql = "Select batch.year, faculty.faculty_name from batch inner join faculty on faculty.fid = batch.fid where batch.bid =" + batch + ";";
        PreparedStatement statement = con.prepareStatement(getInfoSql);
        ResultSet info = statement.executeQuery();
        info.next();
        year = info.getString("year");
        faculty = info.getString("faculty_name");

        //generate username and password for new student
        username = name+sid+faculty+year;
        password = Encryption.generateRandomPassword(8);
        String Epassword = Encryption.encrypt(password);

        // lets insert new login
        String sql = "INSERT INTO login(sid, username, password) VALUES(" + sid + ", '" + username + "','" + Epassword + "')";
        PreparedStatement insertStatement = con.prepareStatement(sql);
        insertStatement.executeUpdate();

        //send Email to student
        EmailSender sendmail  = new EmailSender();
        String subject= "Welcome to Our University, "+name;
        String url= "campusflow.subasacharya.com.np";
        String text="Your addmission has been done and we would like to welcome you to our wonderful Campus.\n\nHope you will have a great time with new friends and teachers. \n\n Your Student account logins for CampusFlow is listed below: \n Username:"+username+"\n Password: "+password+"\n URL: "+url+" \n\n Thank you!";
        Address[] toAddresses = new Address[] { new InternetAddress(email) };
        EmailSender.sendEmail(toAddresses, subject, text);

    }

    public static String addTeacher(String firstname,String middlename, String lastname, String adderss,
                                    String contact, String email,
                                    String fid, String sid){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("teachers")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO teachers(first_name, middle_name, last_name, address, contact, email, fid, subjectId) VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+adderss+"',"+contact+",'"+email+"',"+fid+","+sid+")";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE teachers (tid int AUTO_INCREMENT PRIMARY KEY , first_name varchar(20),middle_name varchar(20),last_name varchar(20),address varchar(50),contact long, email varchar(50),fid int, subjectId int)";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO teachers(first_name, middle_name, last_name, address, contact, email, fid, subjectId) VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+adderss+"',"+contact+",'"+email+"',"+fid+","+sid+")";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }
    public static String addSubject(String name, String semester){
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("subjects")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO subjects(sub_name, semester) VALUES('"+name+"','"+semester+"')";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    feedback= "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE subjects (subId int AUTO_INCREMENT PRIMARY KEY , sub_name varchar(20), semester varchar(10))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO subjects(sub_name, semester) VALUES('"+name+"','"+semester+"')";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");
                    feedback = "Success";
                }catch (SQLException e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }

    public static String addMarks(String subID, String semester, String marks, String sid, String terminal){
        String feedback= "";
        if(connected){
            try{
                //get batch number
                String sql = "SELECT bid from students where sid ="+sid+";";
                PreparedStatement statement = con.prepareStatement(sql);
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    Integer bid = result.getInt("bid");

                    //add marks to batch marksheet
                    String checkMarks = "SELECT * FROM `"+bid+"marksheet` WHERE sid="+sid+" AND semester = '"+semester+"' AND term ='"+terminal+"' AND subId='"+subID+"';";
                    PreparedStatement checkStatement = con.prepareStatement(checkMarks);
                    ResultSet result2 = checkStatement.executeQuery();
                    result2.next();

                    System.out.println(result.getFetchSize());
                    if(result2.next()){
                        return "Marks has already been added of this terminal of student ID:" +sid;
                    }else{
                        String sql3 = "Insert into "+bid+"marksheet(subId,semester,marks,sid, term)"+" values("+subID+",'"+semester+"',"+marks+", "+sid+",'"+terminal+"');";
                        PreparedStatement statement3 = con.prepareStatement(sql3);
                        statement3.executeUpdate();
                        return "Added marks for student ID:" +sid;
                    }
                }
            }catch (SQLException e){
                feedback = e.toString();
            }
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }



    public static String addParent(String firstname,String middlename, String lastname, String address,
                                    String contact, String email,String pid) throws AddressException {
        String feedback = "";
        //first check if the table exists or not
        if(connected){
            if(checkTable("parents")){
                //table found, lets insert data
                try{
                    String sql = "INSERT INTO parents VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+address+"',"+contact+",'"+email+"','"+pid+"')";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();

                    //generate login
                   //addPLogin(pid,firstname,email);
                    feedback= "Success";

                }catch (Exception e){
                    feedback = e.toString();
                }

            }else{
                //create table
                System.out.println("Creating Table!");
                try{
                    String sql = "CREATE TABLE parents (first_name varchar(20),middle_name varchar(20),last_name varchar(20),address varchar(50),contact long, email varchar(50), pid int, CONSTRAINT pk_sid PRIMARY KEY (pid))";
                    PreparedStatement statement = con.prepareStatement(sql);
                    statement.executeUpdate();
                    System.out.println("table created!");
                    //now insert data!
                    String sql2 = "INSERT INTO parents VALUES('"+firstname+"','"+middlename+"','"+lastname+"','"+address+"',"+contact+",'"+email+"','"+pid+"')";
                    PreparedStatement statement2 = con.prepareStatement(sql2);
                    statement2.executeUpdate();
                    System.out.println("Data Inserted!");

                    //generate login
                    //addLogin(pid,firstname,email);
                    feedback = "Success";

                }catch (Exception e){
                    feedback = e.toString();
                }
            };
        }else{
            feedback = "Database Not Connected!";
        }
        return feedback;
    }
}

