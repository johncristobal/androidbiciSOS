package com.bicisos.i7.bicisos.Fragments


import android.Manifest
import android.content.*
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bicisos.i7.bicisos.Adapters.CustomInfoWindowGoogleMap
import com.bicisos.i7.bicisos.Api.ApiClient
import com.bicisos.i7.bicisos.Api.ServiceApi
import com.bicisos.i7.bicisos.R
import com.bicisos.i7.bicisos.model.Report
import com.bicisos.i7.bicisos.model.Taller
import com.bicisos.i7.bicisos.repository.Repository
import com.bicisos.i7.bicisos.service.ForegroundOnlyLocationService
import com.bicisos.i7.bicisos.utils.toText
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
https://github.com/android/location-samples/blob/main/CurrentLocationKotlin/app/src/main/java/com/example/android/location/currentlocationkotlin/MainActivity.kt *
 */
class MapFragment : Fragment(), OnMapReadyCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    //private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var justOne: Boolean = false
    private var mapaListo: Boolean = false
    private var listener: OnFragmentMapListener? = null

    private var locationManager : LocationManager? = null

    private var foregroundOnlyLocationServiceBound = false

    // Provides location updates for while-in-use feature.
    private var foregroundOnlyLocationService: ForegroundOnlyLocationService? = null

    // Listens for location broadcasts from ForegroundOnlyLocationService.
    private lateinit var foregroundOnlyBroadcastReceiver: ForegroundOnlyBroadcastReceiver

    private lateinit var sharedPreferences: SharedPreferences

    private var TAG = "fragmentMap"
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34

    private val job = Job()
    private val scopeMainThread = CoroutineScope(job + Dispatchers.Main)
    private val scopeIO = CoroutineScope(job + Dispatchers.IO)

    private inner class ForegroundOnlyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val location = intent.getParcelableExtra<Location>(
                ForegroundOnlyLocationService.EXTRA_LOCATION
            )

            if (location != null) {
                Log.w("location.....", location.toText())
                lastLocation = location
                mapaListo = true
                if (!justOne) {
                    justOne = true
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
    }

    // Monitors connection to the while-in-use service.
    private val foregroundOnlyServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ForegroundOnlyLocationService.LocalBinder
            foregroundOnlyLocationService = binder.service
            foregroundOnlyLocationServiceBound = true

            getLocationService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            foregroundOnlyLocationService = null
            foregroundOnlyLocationServiceBound = false
        }
    }

    companion object {
        val reportes = ArrayList<Report>()
        val stringIds = ArrayList<String>()
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    interface OnFragmentMapListener {
        fun onFragmentInteractionMap(
            latitud: Double,
            longitud: Double,
            sharedElement: View?,
            opt: String
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentMapListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentMapListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_map, container, false)

        locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager?

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        foregroundOnlyBroadcastReceiver = ForegroundOnlyBroadcastReceiver()
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        return v
    }

    fun getLocationService(){

        if (foregroundPermissionApproved()) {
            setUpMap()
            foregroundOnlyLocationService?.subscribeToLocationUpdates()
                ?: Log.d("FragmenLoc", "Service Not Bound")
        } else {
            requestForegroundPermissions()
        }

//        val enabled = sharedPreferences.getBoolean(SharedPreferenceUtil.KEY_FOREGROUND_ENABLED, false)

//        if (enabled) {
//            foregroundOnlyLocationService?.unsubscribeToLocationUpdates()
//        } else {
//            // TODO: Step 1.0, Review Permissions: Checks and requests if needed.
//            if (foregroundPermissionApproved()) {
//                setUpMap()
//                foregroundOnlyLocationService?.subscribeToLocationUpdates()
//                    ?: Log.d("FragmenLoc", "Service Not Bound")
//            } else {
//                requestForegroundPermissions()
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(requireContext(), ForegroundOnlyLocationService::class.java)
        requireContext().bindService(
            serviceIntent,
            foregroundOnlyServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun requestForegroundPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Toast.makeText(
                requireContext(),
                "Ve a configuraciones para activar ubicación",
                Toast.LENGTH_LONG
            ).show()

        } else {
            Log.d(TAG, "Request foreground only permission")
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    // TODO: Step 1.0, Review Permissions: Handles permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult")

        when (requestCode) {
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE -> when {
                grantResults.isEmpty() ->
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.d(TAG, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission was granted.
                    foregroundOnlyLocationService?.subscribeToLocationUpdates()
                    setUpMap()
                }

                else -> {
                    // Permission denied.
                    Toast.makeText(requireActivity(), "Permiso denegado.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("mapfrgment", "onresume map")

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            foregroundOnlyBroadcastReceiver,
            IntentFilter(
                ForegroundOnlyLocationService.ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST
            )
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(
            foregroundOnlyBroadcastReceiver
        )
        super.onPause()
    }

    override fun onStop() {

        if (foregroundOnlyLocationServiceBound) {
            requireContext().unbindService(foregroundOnlyServiceConnection)
            foregroundOnlyLocationServiceBound = false
        }
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

        super.onStop()
    }

    private fun foregroundPermissionApproved(): Boolean {

        return PackageManager.PERMISSION_GRANTED == checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onMapReady(p0: GoogleMap) {

        mMap = p0
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    19.43276264361115,
                    -99.13312616956577
                ), 12.0f
            )
        )
        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker?): Boolean {
                if (p0!!.tag is Report) {

                    val report = p0.tag as Report

                    if (report.tipo == 1) {
                        mMap.setInfoWindowAdapter(null)

                        val prefs = activity!!.getSharedPreferences(
                            getString(R.string.preferences),
                            Context.MODE_PRIVATE
                        )
                        prefs.edit().putString("detalleMapFragment", "1").apply()

                        val detailtFrag = DetailReportFragment.newInstance(report)
                        childFragmentManager.beginTransaction()
                            .addToBackStack("detalles")
                            .replace(R.id.containerAlertas, detailtFrag)
                            .commit()

                        //listener?.onFragmentInteractionMap(lastLocation.latitude,lastLocation.longitude,alertAction,"menu")

                        //childFragmentManager.beginTransaction().add(R.id.reporte,detailtFrag).commit()
                        return true
                    } else {
                        val customInfoWindow = CustomInfoWindowGoogleMap(activity!!)
                        mMap.setInfoWindowAdapter(customInfoWindow)
                    }

                    return false
                } else {
                    mMap.setInfoWindowAdapter(null)
                    return false
                }
            }
        })

        //mMap.setOnMarkerClickListener(activity!!)
        //listenerReports()
        setTalleres()
        //listenerBikers()
    }

    private fun setTalleres(){
        val repo = Repository(ServiceApi())
        scopeIO.launch {
            val talleres = repo.getTalleres()
            scopeMainThread.launch {
                talleres.talleres.map { taller ->
                    //val datos = taller.coordinates.split(",")
                    val lon = taller.latitud
                    val lat = taller.longitud

                    val height = 50
                    val width = 50

                    val bitmapdraw = getResources().getDrawable(R.drawable.llaveicono) as BitmapDrawable
                    val b = bitmapdraw.getBitmap()
                    val smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(lat.toDouble(), lon.toDouble()))
                            .title(taller.nombre)
                            .snippet("Da click para ver ruta...")
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    )//.showInfoWindow()
                }
            }
        }
    }

    private fun listenerReports(){
        //Log.w("facebbbok", AccessToken.getCurrentAccessToken().token)
        val reference = FirebaseDatabase.getInstance().getReference("reportes")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("error", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {

                reportes.clear()
                val newIdS = ArrayList<String>()

                p0.children.mapNotNullTo(reportes) {
                    it.getValue<Report>(Report::class.java)
                }

                reportes.forEach {

                    //nueva lista de strings con los que esten vivos
                    newIdS.add(it.id)

                    //punto nuevo
                    //if(!stringIds.contains(it.id)) {

                    //    stringIds.add(it.id)

                    //if (!it.id.equals(keySelf) && !keySelf!!.equals("null")) {
                    //despues de enviar, recupero bikes activas...
                    //agregar marcadores al mapa con los bikers
                    val bici = it.tipo
                    var mipmap = 0

                    when (bici) {
                        0 -> mipmap = R.mipmap.bicia
                        1 -> mipmap = R.drawable.alertafinal
                        2 -> mipmap = R.drawable.averiaicon
                        3 -> mipmap = R.drawable.cicloviaicon
                        4 -> mipmap = R.drawable.apoyoicon
                        5 -> mipmap = R.drawable.panic
                    }

                    val height = 100
                    val width = 100
                    val bitmapdraw = resources.getDrawable(mipmap) as BitmapDrawable
                    val b = bitmapdraw.bitmap
                    val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

                    /*val mark = mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(it.latitude, it.longitude))
                            .title(it.name)
                            //.snippet("Population: 4,627,300")
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    )*/

                    val markerOptions = MarkerOptions()
                    markerOptions.position(LatLng(it.latitude, it.longitude))
                        .title(it.name)
                        //.snippet("I am custom Location Marker.")
                        .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

                    /*val info = InfoWindowData("Developine", "Islamabad Pakistan",
                        "hammadtariq.me@gmail.com",
                        "92 333 8456598",
                        "8 AM to 6 PM",
                        "0404"
                    )*/

                    val mark = mMap.addMarker(markerOptions)
                    mark.tag = it

                    //hashMapMarker.put(it.id, mark)
                    //}
                    //}
                }

                //los puntos que se eliminaran
                /*val justIds = stringIds.minus(newIdS)
                var keyHere = ""
                if(hashMapMarker.size > 0) {
                    for ((key, value) in hashMapMarker) {
                        if (justIds.contains(key)) {
                            val marker = hashMapMarker.get(key)
                            marker!!.remove()
                            keyHere = key
                            stringIds.remove(key)
                        }
                    }
                    hashMapMarker.remove(keyHere)
                }*/
            }
        })
    }

    private fun setUpMap() {

        if (checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        try {
            mMap.isMyLocationEnabled = true

            mMap.setOnInfoWindowClickListener {

                val tag = it.tag

                if((tag is Report))
                {
                    Log.w("taller", "No es taller")
                }else {
                    //get direcion
                    val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                        it.position.latitude,
                        it.position.longitude,
                        1
                    ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    val address = addresses.get(0)
                        .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                    val uri = String.format(
                        Locale.ENGLISH,
                        "http://maps.google.com/maps?daddr=%f,%f",
                        it.position.latitude,
                        it.position.longitude
                    )
                    //var uri = String.format(Locale.ENGLISH, "google.navigation:q=%s", address);
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    intent.setPackage("com.google.android.apps.maps")
                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    }
                }
            }

            /*
            Style to map json
             */
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity(),
                    R.raw.style_json
                )
            )

            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }else{
                Log.e("TAG", "Style parsing success.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("tag", "Can't find style. Error: ", e)
        }
    }

    fun buildAlertMessageNoGps(){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("¿Deseas habilitar tu ubicacion para mejorar el funcionamiento de la app?")
            .setCancelable(false)
            .setPositiveButton("Si") { dialog, which ->
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }

        val alert = builder.create()
        alert.show()
    }

    fun reloadData(){
        Log.w("tag", "info from main")

        listenerReports()
        //listenerBikers()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {

    }
}
