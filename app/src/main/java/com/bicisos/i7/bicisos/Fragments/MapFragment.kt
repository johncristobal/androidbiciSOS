package com.bicisos.i7.bicisos.Fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bicisos.i7.bicisos.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.R.raw
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.util.Log
import android.widget.Toast
import com.bicisos.i7.bicisos.Activities.SesionActivity
import com.bicisos.i7.bicisos.Api.ApiClient
import com.bicisos.i7.bicisos.Model.Biker
import com.bicisos.i7.bicisos.Model.Taller
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var justOne: Boolean = false
    private var mapaListo: Boolean = false
    private lateinit var talleres : List<Taller>
    //private lateinit var bikers : List<Biker>
    private var hashMapMarker = HashMap<String,Marker>();

    companion object {
        val bikers = ArrayList<Biker>()
        val stringIds = ArrayList<String>()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val v = inflater.inflate(R.layout.fragment_map, container, false)

        val mapFragment = getChildFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)

    }

    private fun listenerBikers() {
        val prefs = activity!!.getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE)

        val reference = FirebaseDatabase.getInstance().getReference("bikers")
        reference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("error",p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {

                bikers.clear()
                stringIds.clear()
                val keySelf = prefs.getString("keySelf","null")

                p0.children.mapNotNullTo(bikers) {
                    it.getValue<Biker>(Biker::class.java)
                }

                bikers.forEach {

                    stringIds.add(it.id)

                    if (!it.id.equals(keySelf) && !keySelf!!.equals("null")) {
                        //despues de enviar, recupero bikes activas...
                        //agregar marcadores al mapa con los bikers
                        val bici = it.bici
                        var mipmap = 0
                        when(bici){
                            0 -> mipmap = R.mipmap.bicia
                            1 -> mipmap = R.mipmap.bicib
                            2 -> mipmap = R.mipmap.bicic
                            3 -> mipmap = R.mipmap.bicid
                            4 -> mipmap = R.mipmap.bicie
                            5 -> mipmap = R.mipmap.bicif
                        }
                        val height = 50
                        val width = 70
                        val bitmapdraw = getResources().getDrawable(mipmap) as BitmapDrawable
                        val b = bitmapdraw.getBitmap()
                        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        val mark = mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(it.latitud, it.longitude))
                                .title(it.name)
                                //.snippet("Population: 4,627,300")
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        )

                        hashMapMarker.put(it.id,mark)
                    }
                }

                for((key,value) in hashMapMarker){
                    if(!stringIds.contains(key)){
                        val marker = hashMapMarker.get(key)
                        marker!!.remove()
                        hashMapMarker.remove(key)
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()

        Log.e("mapfrgment","onresume map")

        if(mapaListo)
            setUpMap()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun setTalleres(){
        /*
            Anko
            async task, it's so good!!!
        */
        doAsync {
            var result = ApiClient().callTalleres()
            uiThread {

                if (result != null) {

                    Log.e("main", result!![0].coordinates)
                    talleres = result

                    result.forEach { taller ->
                        val datos = taller.coordinates.split(",")
                        val lat = datos[1]
                        val lon = datos[0]

                        val height = 50
                        val width = 50
                        val bitmapdraw = getResources().getDrawable(R.drawable.llaveicono) as BitmapDrawable
                        val b = bitmapdraw.getBitmap()
                        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(lat.toDouble(), lon.toDouble()))
                                .title(taller.name)
                                //.snippet("Population: 4,627,300")
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                        ).showInfoWindow()
                        //.anchor(0.5f, 1F));

                    }
                }
                //Toast.makeText(activity,"listo",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpMap() {

        /*
            Nota Kotlin
            Checa como usar checkSelfPermission y requestPermissions...no usa ActivityCompat
        */

        if (checkSelfPermission(activity!!,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                mapaListo = true
                lastLocation = location
                initListenerBike()
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }

        mMap.setOnInfoWindowClickListener {

            //get direcion
            val geocoder = Geocoder(activity!!, Locale.getDefault());
            val addresses = geocoder.getFromLocation(it!!.position.latitude, it!!.position.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            val address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            var uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f", it!!.position.latitude, it!!.position.longitude);
            //var uri = String.format(Locale.ENGLISH, "google.navigation:q=%s", address);
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            if (intent.resolveActivity(activity!!.packageManager) != null) {
                startActivity(intent)
            }
        }

        /*
        Style to map json
         */
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity!!, R.raw.style_json))

            if (!success) {
                Log.e("TAG", "Style parsing failed.");
            }else{
                Log.e("TAG", "Style parsing success.");

            }
        } catch (e: Exception) {
            Log.e("tag", "Can't find style. Error: ", e);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if(!grantResults.isEmpty() && grantResults[0] !=
                    PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(activity!!,"Permiso denegado.",Toast.LENGTH_SHORT).show()
                }else{
                    setUpMap()
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {

        mMap = p0
        mMap.uiSettings.isZoomControlsEnabled = true
        //mMap.setOnMarkerClickListener(activity!!)
        setTalleres()
        setUpMap()
        //val layer = KmlLayer(mMap, R.raw.talleres, context)
        //layer.addLayerToMap()

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    //al abrir app, se manda ubicacion con el bike
    private fun initListenerBike() {
        val prefs = activity!!.getSharedPreferences(activity!!.getString(R.string.preferences), Context.MODE_PRIVATE)
        val sesion = prefs.getString("sesion","null")
        if (sesion!!.equals("1")){
            val name = prefs.getString("nombre", "null")
            val bici = prefs.getInt("bici", -1)

            //primero enviar mi bike para que este en fierbase
            //si y solo si estoy logueado
            //mando nombre, bike, ubication
            val database = FirebaseDatabase.getInstance()
            val bikersRef = database.getReference("bikers")
            val lat = lastLocation.latitude
            val long = lastLocation.longitude

            val key = bikersRef.push().key
            bikersRef.child(key!!).setValue(Biker(key, name, bici, lat, long)).addOnSuccessListener {
                prefs.edit().putString("enviado", "1").apply()
                prefs.edit().putString("keySelf", key).apply()
                listenerBikers()
            }.addOnFailureListener {
                Log.e("error", "No se pudo subir archivo: " + it.stackTrace)
            }


        }else{
            //no envia nada
        }
    }
}
