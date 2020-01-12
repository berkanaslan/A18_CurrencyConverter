package com.berkanaslan.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView usdText;
    TextView jpyText;
    TextView cadText;
    TextView tryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usdText = (TextView)findViewById(R.id.usdText);
        jpyText = (TextView)findViewById(R.id.jpyText);
        cadText = (TextView)findViewById(R.id.cadText);
        tryText = (TextView)findViewById(R.id.tryText);

    }

    public void getRates (View view) {
        /** DownloadData sınıfımızı çağırıyoruz (Butona tıklandığında) */

        DownloadData downloadData = new DownloadData();

        try {
            String url = "http://data.fixer.io/api/latest?access_key=ed5491c84727fd950ded0485402854e5&format=1";
            downloadData.execute(url);

        } catch (Exception e) {

        }

    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {
                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0) {
                    char c = (char) data;
                    result = result + c;

                    data = inputStreamReader.read();
                }

                return result;

            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                /** İlk JSON objemizi tanımladık. Bütün veriler s değişkeninden geliyor.
                 * Ardından bize lazım olan "rates" kısmını değişkene çekiyoruz. İkinci objemizi o değişkenden aldık.
                 * İhtiyacımız olan veri ID'lerini de getString(names) olarak çektik. */

                JSONObject jsonObject = new JSONObject(s);
                String rates = jsonObject.getString("rates");
                JSONObject jsonObject1 = new JSONObject(rates);

                String usd = jsonObject1.getString("USD");
                String jpy = jsonObject1.getString("JPY");
                String cad = jsonObject1.getString("CAD");
                String turkishLira = jsonObject1.getString("TRY");

                /** Sadece deneme amaçlı yaptım. Virgülden sonra kaç rakam göstermek istediğimi belirttim.
                 * 2 rakam: #.## şeklinde - USD'yi böyle gösteriyoruz: */

                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                Double usd1 = Double.parseDouble(jsonObject1.getString("USD"));
                usd = decimalFormat.format(usd1);

                /** TextView'lara çektiğimiz veriyi yansıtıyoruz: */

                usdText.setText("USD: "+usd);
                jpyText.setText("JPY: "+jpy);
                cadText.setText("CAD: "+cad);
                tryText.setText("TRY: "+turkishLira);

            } catch (Exception e) {

            }

        }
    }
}
