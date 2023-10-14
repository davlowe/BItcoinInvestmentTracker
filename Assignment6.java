import java.util.*;
import java.io.*;
import java.net.*;
public class Assignment6 {
    public static ArrayList<String> getData(){
        ArrayList<String> myList = new ArrayList<>();

        try{

            Socket mySock= new Socket("api.coindesk.com", 80);
            OutputStream os=mySock.getOutputStream();
            PrintWriter pw=new PrintWriter(os);
            pw.println("GET http://api.coindesk.com/v1/bpi/currentprice.json HTTP/1.0\n\n");
            pw.flush();
            Scanner scan = new Scanner(mySock.getInputStream());
            while(scan.hasNextLine()){
                myList.add(scan.nextLine());
            }
            scan.close();


        } catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }
        return myList;


        }
        public static void buyBitCoin(float price) {
            try {
                File newFile = new File("clientBC.txt");
                PrintWriter pw=new PrintWriter(newFile);
                File myFile = new File("initialInvestmentUSD.txt");
                Scanner scan = new Scanner(myFile);
                String line="";
                while(scan.hasNextLine()){
                    line=scan.nextLine();
                    String [] arrayOne=line.split(":");
                    float bitcoins=Float.parseFloat(arrayOne[1])/price;
                    pw.println(arrayOne[0]+ ":" +bitcoins);

                }
                scan.close();
                pw.close();

            } catch (IOException ioException) {
                ioException.getMessage();
            }
            catch(NumberFormatException nfe){
                nfe.getMessage();
            }


        }
        public static void getCurrentValue(float value) {
            try {
                File myFile = new File("clientBC.txt");
                Scanner scan = new Scanner(myFile);
                String line="";
                while(scan.hasNextLine()) {
                    line = scan.nextLine();
                    String[] arrayOne = line.split(":");
                    System.out.println(arrayOne[0]+":$"+Float.parseFloat(arrayOne[1])*value);

                }

            } catch (IOException ioe){
                ioe.getMessage();
            }
        }


    public static float getDollarPrice(ArrayList<String> lines) {
        boolean header=true;
        String json="";
        for(String line : lines) {
            if(line.equals("")) {
                header=false;
                continue;
            }
            if(header==false) {
                json=line;
                break;
            }
        }
//System.out.println("Json: "+json);
        String[] jsonParts=json.split(":");
        String priceLine=jsonParts[19];
        String justPrice=priceLine.replace("},\"GBP\"","");
        float price=Float.parseFloat(justPrice);
        return price;
    }
    public static String getPersonFromFile (String name, String file) throws Exception {
        try {
            File myFile = new File(file);
            Scanner scan = new Scanner(myFile);
            String line = "";
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                String[] array = line.split(":");
                if (array[0].equals(name)) {
                    return array[1];
                }
            }
            throw new PersonNotFound("Person not found...");


        } catch (IOException ioe) {
            ioe.getMessage();
        }
        return "Not found!";

    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int userInput=0;
        do{
            float bitPrice=getDollarPrice(getData());
            System.out.println("One BitCoin is currently worth "+bitPrice);
            System.out.println("1. Buy Bitcoin");
            System.out.println("2. See everyone's current value in USD");
            System.out.println("3. See one person's gain/loss");
            System.out.println("4. Quit");
            System.out.println();
            userInput=scan.nextInt();

            switch(userInput){
                case 1: buyBitCoin(bitPrice);
                break;
                case 2: getCurrentValue(bitPrice);
                break;
                case 3: scan.nextLine();
                        System.out.println("Enter a name");
                        System.out.println("");
                        String name=scan.nextLine();
                        try{
                            float original=Float.parseFloat(getPersonFromFile(name, "initialInvestmentUSD.txt"));
                            float numberBit=Float.parseFloat(getPersonFromFile(name, "clientBC.txt"));
                            System.out.println("Number of bitcoins: "+numberBit);
                            System.out.println("Current value "+numberBit*bitPrice);
                            System.out.println("Change in value: "+((numberBit*bitPrice)-original));



                        }catch(Exception e){
                            e.getMessage();
                }
                        break;
                default:
                    System.out.println("Invalid");



            }



        }while(userInput!=4);
    }

    }

