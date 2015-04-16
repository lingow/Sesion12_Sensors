package com.lingoware.lingow.sensors;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.security.Provider;
import java.util.List;


public class ActivityLocation extends ActionBarActivity implements LocationListener{

    LocationManager locMan;

    TextView txtLatitude;
    TextView txtLongitude;
    TextView txtProvider;
    TextView txtAccuracy;
    TextView txtTimeToFix;
    ListView listEnabledProviders;
    long uptimeAtResume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_location);

        locMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        txtLatitude= (TextView) findViewById(R.id.txtLatitude);
        txtLongitude= (TextView) findViewById(R.id.txtLongitude);
        txtProvider= (TextView) findViewById(R.id.txtProvider);
        txtAccuracy= (TextView) findViewById(R.id.txtAccuracy);
        txtTimeToFix= (TextView) findViewById(R.id.txtTimeToFix);
        listEnabledProviders = (ListView) findViewById(R.id.listEnabledProviders);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        txtLatitude.setText("Latitude " + String.valueOf(location.getLatitude()));
        txtLongitude.setText("Longitude " + String.valueOf(location.getLongitude()));
        txtProvider.setText("Provider " + String.valueOf(location.getProvider()));
        txtAccuracy.setText("Accuracy " + String.valueOf(location.getAccuracy()));
        txtTimeToFix.setText("TimetoFix " + String.valueOf((SystemClock.uptimeMillis() - uptimeAtResume) / 1000));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StringBuffer stringBuffer = new StringBuffer();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        List<String> providers = locMan.getProviders(criteria, true);
        listEnabledProviders.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,providers));

        if (! providers.isEmpty()) {
            for (String enabledProvider : providers) {
                stringBuffer.append(enabledProvider).append(" ");
                locMan.requestSingleUpdate(enabledProvider, this,
                        null);
            }
        }

        uptimeAtResume = SystemClock.uptimeMillis();
        txtLatitude.setText("Latitude: "); txtLongitude.setText("Longitude: ");
        txtProvider.setText("Provider: "); txtAccuracy.setText("Accuracy: ");
        txtTimeToFix.setText("TimeToFix: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        locMan.removeUpdates(this);
    }
}
