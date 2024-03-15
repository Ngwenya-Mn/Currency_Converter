import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) throws IOException {

        HashMap<Integer, String> currencySymbols = new HashMap<Integer, String>();

        currencySymbols.put(1, "USD");
        currencySymbols.put(2, "ZAR");
        currencySymbols.put(3, "RUB");
        currencySymbols.put(4, "EUR");
        currencySymbols.put(5, "GBP");
        currencySymbols.put(6, "MXN");
        currencySymbols.put(7, "JPY");
        currencySymbols.put(8, "CAD");
        currencySymbols.put(9, "INR");
        currencySymbols.put(10, "AUD");
        currencySymbols.put(11, "NZD");
        currencySymbols.put(12, "HKD");

        String fromCode,toCode;
        double amount;


        Scanner scanner = new Scanner(System.in);
        System.out.println("Select from which Currency:");
        System.out.println("1:USD\t 2:ZAR\t 3:RUB\t 4:EUR\t 5:GBP\t 6:MXN\t 7:JPY\t 8:CAD\t 9:INR\t 10:AUD\t 11:NZD\t 12:HKD");
        fromCode = currencySymbols.get(scanner.nextInt());
        System.out.println(fromCode + " ");

        System.out.println("Select to Currency:");
        System.out.println("1:USD\t 2:ZAR\t 3:RUB\t 4:EUR\t 5:GBP\t 6:MXN\t 7:JPY\t 8:CAD\t 9:INR\t 10:AUD\t 11:NZD\t 12:HKD");
        toCode = currencySymbols.get(scanner.nextInt());
        System.out.println(toCode + " ");

        System.out.println("Please enter the amount you wish to convert:");
        amount = scanner.nextFloat();

        sendHttpGetRequest(fromCode,toCode,amount);

    }

    private static final String API_KEY = "cur_live_FHESdsVqntqPhWmOVRyDtZUTxgybwUTR9goBEk7H";
    private static void sendHttpGetRequest(String fromCode, String toCode, double amount) throws IOException {
        String GET_URL = "https://api.currencyapi.com/v3/latest?apikey="+API_KEY+"&base_currency="+fromCode+"&currencies="+toCode;

        DecimalFormat f = new DecimalFormat("00.00");
        URL url = new URL(GET_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseStream = connection.getResponseCode();

        if(responseStream == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

            JSONObject obj = new JSONObject(response.toString());
            JSONObject data = obj.getJSONObject("data");

            // Get the exchange rate for the given 'toCode'
            double exchangeRate = data.getJSONObject(toCode).getDouble("value");

            System.out.println("Exchange Rate: " + exchangeRate);
            System.out.println();
            System.out.println(f.format(amount) + fromCode + " = "+ f.format(amount*exchangeRate) + toCode);
        }
        else{
            System.out.println("GET did not work");
        }
    }
}
