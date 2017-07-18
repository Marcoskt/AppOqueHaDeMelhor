package appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import appoquehademelhorbsb.studio.com.appoquehademelhorbsb.R;
import appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Database.ConfiguracaoFirebase;
import appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Model.Direcoes;
import appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Model.DirecoesListener;
import appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Model.Restaurantes;
import appoquehademelhorbsb.studio.com.appoquehademelhorbsb.app.Model.Rotas;

public class RestauranteActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, android.location.LocationListener,DirecoesListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final String TAG = "Restaurante";
    private String provider;
    private double log;
    private double lat;
    private static DatabaseReference databaseReference ;
    private DatabaseReference idRestaurante ;
    private LatLng local ;
    private Restaurantes cadastro;
    private Restaurantes restaurantes;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private float zoomLevel;
    private List<Marker> array = new ArrayList<>();








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        cadastrarRestauranteDB("teste","teste",-15.845271, -48.043625,"010");
        recuperarDadosDB();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });


    }
    private void sendRequest() {

        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Coloque seu endereço de origem !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Coloque seu endereço de destino !", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new Direcoes((DirecoesListener) this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {


            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();

            provider = locationManager.getBestProvider(criteria, false);

            Location location = locationManager.getLastKnownLocation(provider);

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            mMap = googleMap;

            mMap.setOnMapClickListener(this);

            mMap.getUiSettings().setZoomControlsEnabled(true);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mMap.setMyLocationEnabled(true);

            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                    boolean enabledGPS = service
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (!enabledGPS) {
                        Toast.makeText(RestauranteActivity.this, "Sem sinal de gps", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }

                    lat = mMap.getMyLocation().getLatitude();
                    log = mMap.getMyLocation().getLongitude();
                    String latAtual = String.valueOf(lat);
                    String logAtual = String.valueOf(log);
                    etOrigin.setText(lat+","+log);

                    LatLng atual = new LatLng(lat, log);


                    /*databaseReference = ConfiguracaoFirebase.getFirebase();
                    idRestaurante = databaseReference.child("Localização").child("valor");
                    localizacao = new Localizacao();
                    localizacao.setLatAtual(lat);
                    localizacao.setLogAtual(log);
                    idRestaurante.setValue(localizacao);*/



                    array = new ArrayList<>();
                    if (array != null) {
                        for (Marker marker : array) {
                            marker.remove();
                        }
                        array.add(mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .title("Você está aqui")
                                .position(atual)));

                    }



                    return false;
                }
            });




            local = new LatLng(-15.826721,-48.056682);
            zoomLevel = (float) 10.0; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(local, zoomLevel));


        } catch (SecurityException e) {
            Log.e(TAG, "Error", e);
        }
    }



    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Provider habilitado ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Provider desabilitado ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    public void colocarMarcador (String nome,String descricao,double latitude, double longitude){
        LatLng local = new  LatLng (latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(nome);
        markerOptions.snippet(descricao);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.iconemapa));
        markerOptions.position(local);
        mMap.addMarker(markerOptions);

    }
    public void cadastrarRestauranteDB(String name,String description ,double lat,double lon,String identificador){

        databaseReference = ConfiguracaoFirebase.getFirebase();

        idRestaurante = databaseReference.child("Restaurantes").child(String.valueOf(identificador));
        cadastro = new Restaurantes();
        cadastro.setNome(name);
        cadastro.setDescricao(description);
        cadastro.setLatitude(lat);
        cadastro.setLongitude(lon);

        idRestaurante.setValue(cadastro);








    }

    public void recuperarDadosDB( ) {
        databaseReference = ConfiguracaoFirebase.getFirebase();
        idRestaurante = databaseReference.child("Restaurantes");

        idRestaurante.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    restaurantes = dados.getValue(Restaurantes.class);


                    colocarMarcador(restaurantes.getNome(), restaurantes.getDescricao(), restaurantes.getLatitude(), restaurantes.getLongitude());
                    Log.i("Nome", "" + restaurantes.getNome());
                    Log.i("Teste ", "Sucesso ao colocar Marcador");



                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onDirecoesStart() {
        progressDialog = ProgressDialog.show(this, "Espere",
                "Pesquisando direção...", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirecoesSuccess(List<Rotas> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Rotas route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.localInicial, zoomLevel));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duracao.texto);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distancia.texto);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(route.enderecoInicial)
                    .position(route.localInicial)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    .title(route.enderecoFinal)
                    .position(route.localFinal)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}


